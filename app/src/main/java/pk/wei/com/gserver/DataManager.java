package pk.wei.com.gserver;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import pk.wei.com.gserver.Common.ContextHolder;

public class DataManager {

    private static volatile DataManager instance;
    private Context context;
    private IBinderPool remoteBinderPool;
    private IRemoteBookManager remoteBookManager;

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    public int login(String name) {
        int check = 0;
        try {
            if (remoteBookManager != null) {
                check = remoteBookManager.login(name);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return check;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                IBinder iBinder = remoteBinderPool.queryBinder(200);
                remoteBookManager = IRemoteBookManager.Stub.asInterface(iBinder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            remoteBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            remoteBinderPool = null;
            bindRemoteService();
        }
    };

    private void bindRemoteService() {
        Intent bindIntent = new Intent(context, BookService.class);
        context.bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private void unBindRemoteService() {
        context.unbindService(connection);
    }

    private DataManager() {
        context = ContextHolder.getContext();
        bindRemoteService();
    }


}
