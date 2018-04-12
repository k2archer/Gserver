package pk.wei.com.gserver.DataManagement;


import android.content.Context;

import pk.wei.com.gserver.Common.BaseApplication;

public class DatabaseManager {

    private static volatile DatabaseManager manager;
    private DatabaseOpenHelper dbHelper;

    private DatabaseManager() {
        Context context = BaseApplication.getContext();
        dbHelper = DatabaseOpenHelper.getInstance(context);
    }

    public static DatabaseManager getInstance() {
        if (manager == null) {
            synchronized (DatabaseManager.class) {
                if (manager == null) {
                    manager = new DatabaseManager();
                }
            }
        }
        return manager;
    }

}
