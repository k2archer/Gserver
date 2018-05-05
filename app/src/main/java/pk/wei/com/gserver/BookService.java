package pk.wei.com.gserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Notification;
import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;
import android.app.PendingIntent;

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
    public void onCreate() {
        super.onCreate();
    }

    public void createNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("图书馆管理系统")// 设置通知栏标题
                .setContentText("运行中")         // 设置通知栏显示内容
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) // 设置通知栏点击意图
                .setTicker("正在启动中...")          // 通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis()) // 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) // 设置该通知优先级
                .setOngoing(true)                  // true，设置为正在进行的通知。表示后台任务
                .setDefaults(Notification.DEFAULT_VIBRATE) // 向通知添加声音、闪灯和振动效果
                .setSmallIcon(R.mipmap.ic_launcher);       // 设置通知小ICON

        mNotificationManager.notify(1, mBuilder.build());
    }

    public PendingIntent getDefalutIntent(int flags) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, flags);
        return pendingIntent;
    }

    @Override
    public IBinder onBind(Intent intent) {
//        int check = checkCallingOrSelfPermission("pk.wei.com.library.access.BOOK_SERVICE");
//        if (check == PackageManager.PERMISSION_DENIED) {
//            return null;
//        }

        return iBinderPool;
    }

}
