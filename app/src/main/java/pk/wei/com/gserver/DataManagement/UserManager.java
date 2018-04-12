package pk.wei.com.gserver.DataManagement;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManager {

    private static volatile UserManager manager;
    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase rdb;

    private UserManager(Context context) {
        dbHelper = DatabaseOpenHelper.getInstance(context);
        rdb = dbHelper.getWritableDatabase();

        setDefaultUser();
    };

    public static UserManager getInstance(Context context) {
        if (manager == null) {
            synchronized (UserManager.class) {
                if (manager == null) {
                    manager = new UserManager(context);
                }
            }
        }
        return manager;
    }

    public boolean isRegistered(String name) {
        String sql = "SELECT "
                + TableConfig.UserInfo.NAME
                + " FROM " + TableConfig.UserInfo.TABLE_NAME
                + " WHERE " + TableConfig.UserInfo.NAME + " = \"" + name + "\"";
        Cursor cursor = rdb.rawQuery(sql, null);
        if (cursor.getCount() == 1) {
            return true;
        }
        return false;
    }

    private void setDefaultUser() {
        boolean check = isRegistered("admin");
        if (!check) {
            String sql = "INSERT INTO "
                    + TableConfig.UserInfo.TABLE_NAME
                    + " (" + TableConfig.UserInfo.NAME + ") "
                    + "VALUES (" + " \"admin\" " + ")";
            rdb.execSQL(sql);
        }

    }
}
