package pk.wei.com.gserver.DataManagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pk.wei.com.gserver.Book;

public class BookManager {

    private static volatile BookManager manager;
    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase db;

    private BookManager(Context context) {
        dbHelper = DatabaseOpenHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();

        setDefaultBooks();
    }


    public static BookManager getInstance(Context context) {
        if (manager == null) {
            synchronized (BookManager.class) {
                if (manager == null) {
                    manager = new BookManager(context);
                }
            }
        }
        return manager;
    }

    public void setDefaultBooks() {
        Book[] books = {
                new Book("Android 开发艺术探索"),
                new Book("Android 第一行代码(第2版)"),
                new Book("Android群英传"),
        };

        for (Book it : books) {
            addBook(it);
        }
    }

    public boolean inStored(Book book) {
        String sql = "SELECT "
                + TableConfig.Book.NAME
                + " FROM " + TableConfig.Book.TABLE_NAME
                + " WHERE " + TableConfig.Book.NAME + " = \"" + book.name + "\"";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 1) {
            return true;
        }
        return false;

    }

    public void addBook(Book newBook) {
        if (!inStored(newBook)) {
            String sql = "INSERT INTO "
                    + TableConfig.Book.TABLE_NAME
                    + " (" + TableConfig.Book.NAME + ") "
                    + "VALUES ( " + "\"" + newBook.name + "\"" + " )";
            db.execSQL(sql);
        }
    }

    public List<Book> getBookList() {
        ArrayList<Book> list = new ArrayList<>();
        String sql = "SELECT "
                + TableConfig.Book.NAME
                + " FROM " + TableConfig.Book.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            list.add(new Book(name));
        }

        return list;
    }

    public List<Book> findBook(String s) {
        ArrayList<Book> list = new ArrayList<>();

//        select * from books where name like "%Android%";
        String sql = "SELECT "
                + TableConfig.Book.NAME
                + " FROM " + TableConfig.Book.TABLE_NAME
                + " WHERE " + TableConfig.Book.NAME
                + " LIKE " + "\"%" + s + "%\"";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            list.add(new Book(name));
        }

        return list;
    }

    public int orderBook(String user, String book) {
        int check = 0;
        int userId = getUserId(user);
        if (userId != 0) {
            int bookId = getBookId(book);
            if (bookId != 0) {
                // 检查是否已订阅
                String ordered_sql = "SELECT "
                        + TableConfig.Order.ID
                        + " FROM " + TableConfig.Order.TABLE_NAME
                        + " WHERE " + TableConfig.Order.USER_ID + " = " + userId
                        + " AND " + TableConfig.Order.BOOK_ID + " = " + bookId;
                Cursor cursor = db.rawQuery(ordered_sql, null);
                if (!cursor.moveToNext()) {
                    // 插入订阅记录
                    String insert_sql = "INSERT INTO "
                            + TableConfig.Order.TABLE_NAME
                            + " (" + TableConfig.Order.USER_ID
                            + ", " + TableConfig.Order.BOOK_ID
                            + ") VALUES (" + userId + ", " + bookId + ")";
                    db.execSQL(insert_sql);

                    userMessage(userId, "你订阅了\n《" + book + "》。");
                    check = 1;
                }
            }
        }

        return check;
    }

    public int unSubscribeBook(String user, String book) {
        int userId = getUserId(user);
        if (userId != 0) {
            int bookId = getBookId(book);
            if (bookId != 0) {
                // 检查是否已订阅
                String ordered_sql = "SELECT "
                        + TableConfig.Order.ID
                        + " FROM " + TableConfig.Order.TABLE_NAME
                        + " WHERE " + TableConfig.Order.USER_ID + " = " + userId
                        + " AND " + TableConfig.Order.BOOK_ID + " = " + bookId;
                Cursor cursor = db.rawQuery(ordered_sql, null);
                if (cursor.moveToNext()) {
                    String delete_sql = "DELETE FROM "
                            + TableConfig.Order.TABLE_NAME
                            + " WHERE " + TableConfig.Order.BOOK_ID + " = " + bookId;
                    db.execSQL(delete_sql);

                    userMessage(userId, "你退订了\n《" + book + "》。");
                }
            }
        }

        return 0;
    }

    public void userMessage(int userId, String message) {
        // 插入订阅记录
        String insert_sql = "INSERT INTO "
                + TableConfig.Messages.TABLE_NAME
                + " (" + TableConfig.Messages.USER_ID
                + ", " + TableConfig.Messages.MESSAGE
                + ") VALUES (" + userId + ", " + "\"" + message + "\"" + ")";
        db.execSQL(insert_sql);
    }

    public List<String> getMessagesList(String user) {
        List<String> messageList = new ArrayList<>();
        int userId = getUserId(user);
        if (userId != 0) {
            String message_list_sql = "SELECT "
                    + TableConfig.Messages.MESSAGE
                    + " FROM " + TableConfig.Messages.TABLE_NAME
                    + " WHERE " + TableConfig.Messages.USER_ID + " = " + userId + " limit 10";
            Cursor cursor = db.rawQuery(message_list_sql , null);
            while (cursor.moveToNext()) {
                String message = cursor.getString(0);
                messageList.add(message);
            }
        }
        return messageList;
    }

    public List<Book> getOrderedList(String user) {
        List<Book> orderedList = new ArrayList<>();
        int userId = getUserId(user);
        if (userId != 0) {
            String ordered_list_sql = "SELECT "
                    + TableConfig.Book.NAME
                    + " FROM " + TableConfig.Book.TABLE_NAME
                    + " WHERE " + TableConfig.Book.ID + " in ( "
                    + " SELECT " + TableConfig.Order.BOOK_ID
                    + " FROM " + TableConfig.Order.TABLE_NAME
                    + " WHERE " + TableConfig.Order.USER_ID + " = " + userId + " )";
            Cursor cursor = db.rawQuery(ordered_list_sql, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                orderedList.add(new Book(name));
            }
        }

        return orderedList;
    }

    private int getUserId(String user) {
        int userId = 0;
        String user_id_sql = "SELECT "
                + TableConfig.UserInfo.ID
                + " FROM " + TableConfig.UserInfo.TABLE_NAME
                + " WHERE " + TableConfig.UserInfo.NAME + " = \"" + user + "\"";
        Cursor cursor = db.rawQuery(user_id_sql, null);

        if (cursor.moveToNext()) {
            userId = cursor.getInt(0);
        }

        return userId;
    }

    private int getBookId(String book) {
        int bookId = 0;
        String book_id_sql = "SELECT "
                + TableConfig.Book.ID
                + " FROM " + TableConfig.Book.TABLE_NAME
                + " WHERE " + TableConfig.Book.NAME + " = \"" + book + "\"";
        Cursor cursor = db.rawQuery(book_id_sql, null);

        if (cursor.moveToNext()) {
            bookId = cursor.getInt(0);
        }

        return bookId;
    }

}
