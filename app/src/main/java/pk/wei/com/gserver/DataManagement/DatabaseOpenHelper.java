package pk.wei.com.gserver.DataManagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "library.db";

    private static volatile DatabaseOpenHelper helper;

    private DatabaseOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableConfig.createBookTable);
        db.execSQL(TableConfig.createUserInfoTable);
        db.execSQL(TableConfig.createOrderListTable);
        db.execSQL(TableConfig.createMessagesTable);
    }

    public static DatabaseOpenHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (DatabaseOpenHelper.class) {
                if (helper == null) {
                    helper = new DatabaseOpenHelper(context, DATABASE_NAME);
                }
            }
        }
        return helper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
