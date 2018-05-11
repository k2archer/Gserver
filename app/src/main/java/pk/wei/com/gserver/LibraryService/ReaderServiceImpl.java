package pk.wei.com.gserver.LibraryService;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;

import pk.wei.com.gserver.Common.ContextHolder;
import pk.wei.com.gserver.Book;
import pk.wei.com.gserver.DataManagement.BookManager;
import pk.wei.com.gserver.DataManagement.UserManager;
import pk.wei.com.gserver.INewBookPushListener;
import pk.wei.com.gserver.IRemoteBookManager;

public class ReaderServiceImpl extends IRemoteBookManager.Stub {
    private Context mContext;
    private BookManager mBookManager;
    private UserManager mUserManager;

    private RemoteCallbackList<INewBookPushListener> mListener = new RemoteCallbackList<>();

    @Override
    public int login(String name) throws RemoteException {
        return mUserManager.isRegistered(name) ? 1 : 0;
    }

    @Override
    public List<Book> findBook(String name) throws RemoteException {
        return mBookManager.findBook(name);
    }

    @Override
    public List<Book> getRecommend() throws RemoteException {
        return mBookManager.getBookList();
    }

    @Override
    public int subscribeBook(String user, String book) throws RemoteException {
        int check = mBookManager.orderBook(user, book);
        notifyParticipate(book, NOTIFY_TYPE_NEW_ORDER);
        return check;
    }

    public int unSubscribeBook(String user, String book) throws RemoteException {
        int check = mBookManager.unSubscribeBook(user, book);
        notifyParticipate(book, NOTIFY_TYPE_UNORDERED);
        return check;
    }
    @Override
    public List<Book> getOrderedList(String user) throws RemoteException {
        return mBookManager.getOrderedList(user);
    }

    @Override
    public List<String> getMessagesList(String user) throws RemoteException {
        return mBookManager.getMessagesList(user);
    }

    @Override
    public void registerNewBookPush(INewBookPushListener listener) throws RemoteException {
        mListener.register(listener);
    }

    @Override
    public void unRegisterNewBookPush(INewBookPushListener listener) throws RemoteException {
        mListener.unregister(listener);
    }

    private static final int NOTIFY_TYPE_NEW_ORDER = 10;
    private static final int NOTIFY_TYPE_UNORDERED = 11;

    private void notifyParticipate(String name, int notifyType) {
        final int len = mListener.beginBroadcast();
        try {
            // 通知回调
            for (int i = 0; i < len; i++) {
                switch (notifyType) {
                    case NOTIFY_TYPE_UNORDERED:
                    case NOTIFY_TYPE_NEW_ORDER:
                        mListener.getBroadcastItem(i).NewOrderArrived(new Book(name));
                        break;
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mListener.finishBroadcast();
    }

    @Override
    public IBinder asBinder() {
        return (IBinder) this;
    }

    public ReaderServiceImpl() {
        mContext = ContextHolder.getContext();
        mBookManager = BookManager.getInstance(mContext);
        mUserManager = UserManager.getInstance(mContext);
    }
}
