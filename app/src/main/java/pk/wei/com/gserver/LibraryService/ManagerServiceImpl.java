package pk.wei.com.gserver.LibraryService;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import pk.wei.com.gserver.Book;
import pk.wei.com.gserver.Common.ContextHolder;
import pk.wei.com.gserver.DataManagement.BookManager;
import pk.wei.com.gserver.DataManagement.UserManager;
import pk.wei.com.gserver.INewBookPushListener;
import pk.wei.com.gserver.IRemoteBookManager;


public class ManagerServiceImpl extends IRemoteBookManager.Stub {
    private Context mContext;
    private BookManager mBookManager;
    private UserManager mUserManager;

    @Override
    public int login(String name) throws RemoteException {
        return mUserManager.isRegistered(name) ? 1 : 0;
    }

    @Override
    public List<Book> findBook(String name) throws RemoteException {
        return null;
    }

    @Override
    public List<Book> getRecommend() throws RemoteException {
        return null;
    }

    @Override
    public int subscribeBook(String user, String book) throws RemoteException {
        return 0;
    }

    @Override
    public int unSubscribeBook(String user, String book) throws RemoteException {
        return 0;
    }

    @Override
    public List<Book> getOrderedList(String user) throws RemoteException {
        return null;
    }

    @Override
    public List<String> getMessagesList(String user) throws RemoteException {
        return mBookManager.getMessagesList(user);
    }

    @Override
    public void registerNewBookPush(INewBookPushListener listener) throws RemoteException {

    }

    @Override
    public void unRegisterNewBookPush(INewBookPushListener listener) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return (IBinder) this;
    }

    public ManagerServiceImpl() {
        mContext = ContextHolder.getContext();
        mBookManager = BookManager.getInstance(mContext);
        mUserManager = UserManager.getInstance(mContext);
    }
}
