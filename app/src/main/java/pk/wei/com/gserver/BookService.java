package pk.wei.com.gserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import pk.wei.com.gserver.LibraryService.ReaderServiceImpl;

public class BookService extends Service {

    static final int READER_SERVICE_BINDER_CODE = 100;

    private IBinder iBinderPool = new IBinderPool.Stub() {
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case READER_SERVICE_BINDER_CODE:
                    binder = new ReaderServiceImpl();
                    break;
            }
            return binder;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
//        int check = checkCallingOrSelfPermission("pk.wei.com.library.access.BOOK_SERVICE");
//        if (check == PackageManager.PERMISSION_DENIED) {
//            return null;
//        }

        return iBinderPool;
    }

}
