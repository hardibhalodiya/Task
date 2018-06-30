package com.example.h.wissiontask.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressWarnings("all")
public class SqlController {


    private MyHelper m;
    private SQLiteDatabase s;
    private String TAG = SqlController.class.getName();

    public SqlController(Context c) {
        m = new MyHelper(c, "myDB.db", null, 1);
    }


    private void openDb() {
        s = m.getWritableDatabase();
    }

    public void close() {
        m.close();
    }


    public void insert(ContentValues contentValues, String table) {
        openDb();
        s.insert(table, null, contentValues);
        close();
        Log.i(TAG, table + "  data inserted");
    }

    public void deleteAllRecords(String tableName) {
        openDb();
        s.delete(tableName, null, null);
        close();
    }

    public Cursor getAll(String table) {
        openDb();
        String selectQuery = "SELECT  * FROM " + table;
        Cursor cursor = s.rawQuery(selectQuery, null);
        return cursor;
    }

    public long noOfRows(String table) {
        openDb();
        long numberOfRows = DatabaseUtils.queryNumEntries(s, table);
        close();
        return numberOfRows;
    }

    public Cursor get(String table, String where, String value) {
        openDb();
        Cursor c = null;
        c = s.query(table, null, where + "=?", new String[]{value}, null, null, null);
        return c;
    }

    private class MyHelper extends SQLiteOpenHelper {

        MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


            db.execSQL("create table feeds(id text primary key,title text,description text,thumbnail text,channelTitle text,videoId text );");
            db.execSQL("create table videolikecounts(_id INTEGER PRIMARY KEY AUTOINCREMENT, videoId text, viewCount text, likeCount text, dislikeCount text, favoriteCount text );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}