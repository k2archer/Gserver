package pk.wei.com.gserver.DataManagement;


public class TableConfig {
    public class UserInfo {
        public static final String TABLE_NAME = "userInfo";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static final String createUserInfoTable =
            "CREATE TABLE IF NOT EXISTS "
                    + UserInfo.TABLE_NAME + "("
                    + UserInfo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + UserInfo.NAME + " TEXT"
                    + ")";
    public static final String deleteUserInfoTable = "TRUNCATE " + UserInfo.TABLE_NAME + ";";


    public class Book {
        public static final String TABLE_NAME = "books";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static final String createBookTable =
            "CREATE TABLE IF NOT EXISTS "
                    + Book.TABLE_NAME + "("
                    + Book.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Book.NAME + " TEXT"
                    + ")";
    public static final String deleteBookTable = "TRUNCATE " + Book.TABLE_NAME + ";";


    public class Order {
        public static final String TABLE_NAME = "orders";
        public static final String ID = "id";
        public static final String USER_ID = "user_id";
        public static final String BOOK_ID = "book_id";
    }

    public static final String createOrderListTable =
            "CREATE TABLE IF NOT EXISTS "
                    + Order.TABLE_NAME + "("
                    + Order.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Order.USER_ID + " INTEGER, "
                    + Order.BOOK_ID + " INTEGER"
                    + ")";
    public static final String deleteOrderListTable = "TRUNCATE " + Order.TABLE_NAME + ";";

    public class Messages {
        public static final String TABLE_NAME = "messages";
        public static final String ID = "id";
        public static final String USER_ID = "user_id";
        public static final String MESSAGE = "message";
    }

    public static final String createMessagesTable =
            "CREATE TABLE IF NOT EXISTS "
                    + Messages.TABLE_NAME + "("
                    + Messages.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Messages.USER_ID + " INTEGER, "
                    + Messages.MESSAGE + " TEXT"
                    + ")";
    public static final String deleteMessagesTable = "TRUNCATE " + Messages.TABLE_NAME + ";";

    public static String deleteTableSQL(String name) {
        return "TRUNCATE " + name + ";";
    }
}
