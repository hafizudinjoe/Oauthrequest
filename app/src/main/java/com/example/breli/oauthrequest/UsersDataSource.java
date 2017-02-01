package com.example.breli.oauthrequest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UsersDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TOKEN, MySQLiteHelper.COLUMN_SECRET, MySQLiteHelper.COLUMN_USERID };

    public UsersDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User createUser(String token, String secret, String userid) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TOKEN, token);
        values.put(MySQLiteHelper.COLUMN_SECRET, secret);
        values.put(MySQLiteHelper.COLUMN_USERID, userid);

        long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns,
                MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
                null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);

        cursor.close();
        return newUser;
    }

    public void deleteUser(User user) {
        long id = user.getId();
        System.out.println("user deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }

        cursor.close();
        return users;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setToken(cursor.getString(1));
        user.setSecret(cursor.getString(2));
        user.setUserId(cursor.getString(3));
        return user;
    }
}
