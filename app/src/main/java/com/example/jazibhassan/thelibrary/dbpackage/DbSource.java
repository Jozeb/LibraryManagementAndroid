package com.example.jazibhassan.thelibrary.dbpackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jazibhassan.thelibrary.HistoryClass;
import com.example.jazibhassan.thelibrary.NotificationClass;

import java.util.ArrayList;

/**
 * Created by Jazib Hassan on 22-Nov-15.
 */
public class DbSource {

    SQLiteOpenHelper dbopenhelper;
    SQLiteDatabase database;

    private String[] NOTIFICATION_COLUMN = {
            DBOpenHelper.NOTI_SENDER,
            DBOpenHelper.NOTI_MSG,
            DBOpenHelper.NOTI_DATE
    };

    private String[] HISTORY_COLUMN = {
            DBOpenHelper.HISTORY_TITLE,
            DBOpenHelper.HISTORY_AUTHOR,
            DBOpenHelper.HISTORY_EDTION,
            DBOpenHelper.HISTORY_ISSUEDATE,
            DBOpenHelper.HISTORY_RETURNDATE,
            DBOpenHelper.HISTORY_DUEDATE
    };

    private String[] ISSUE_COLUMN = {
            DBOpenHelper.ISSUE_TITLE,
            DBOpenHelper.ISSUE_AUTHOR,
            DBOpenHelper.ISSUE_EDTION,
            DBOpenHelper.ISSUE_ISSUEDATE,
            DBOpenHelper.ISSUE_RETURNDATE,
            DBOpenHelper.ISSUE_DUEDATE
    };

    public DbSource(Context c) {
        dbopenhelper = new DBOpenHelper(c);
        Log.i("debug", "Dbopenhelper contructor!");
        open();
    }

    public void open() {
        database = dbopenhelper.getWritableDatabase();
        Log.i("debug", "Database opened Successfully!");


    }

    public void close() {
        database.close();
        Log.i("debug", "Database closed Successfully!");
    }

    public boolean put_hist_data(ArrayList<HistoryClass> list) {

        for (HistoryClass item : list) {
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.HISTORY_TITLE, item.title);
            values.put(DBOpenHelper.HISTORY_AUTHOR, item.author);
            values.put(DBOpenHelper.HISTORY_EDTION, item.edition);
            values.put(DBOpenHelper.HISTORY_DUEDATE, item.duedate);
            values.put(DBOpenHelper.HISTORY_ISSUEDATE, item.issuedate);
            values.put(DBOpenHelper.HISTORY_RETURNDATE, item.returndate);
            database.insert(DBOpenHelper.TABLE_HISTORY, null, values);
        }
        return true;
    }

    public boolean put_issue_data(ArrayList<HistoryClass> list) {

        for (HistoryClass item : list) {
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.ISSUE_TITLE, item.title);
            values.put(DBOpenHelper.ISSUE_AUTHOR, item.author);
            values.put(DBOpenHelper.ISSUE_EDTION, item.edition);
            values.put(DBOpenHelper.ISSUE_DUEDATE, item.duedate);
            values.put(DBOpenHelper.ISSUE_ISSUEDATE, item.issuedate);
            values.put(DBOpenHelper.ISSUE_RETURNDATE, item.returndate);
            database.insert(DBOpenHelper.TABLE_ISSUE, null, values);
        }
        return true;
    }

    public ArrayList<HistoryClass> get_hist_data() {
        ArrayList<HistoryClass> logClassArrayList = new ArrayList<HistoryClass>();

        String[] temp = new String[6];
        Cursor cursor = database.query(DBOpenHelper.TABLE_HISTORY, HISTORY_COLUMN, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                temp[0] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.HISTORY_TITLE));
                temp[1] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.HISTORY_AUTHOR));
                temp[2] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.HISTORY_EDTION));
                temp[3] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.HISTORY_ISSUEDATE));
                temp[4] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.HISTORY_DUEDATE));
                temp[5] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.HISTORY_RETURNDATE));
                
                logClassArrayList.add(new HistoryClass(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]));
            }
        }
        return logClassArrayList;
    }

    public ArrayList<HistoryClass> get_issue_data() {
        ArrayList<HistoryClass> logClassArrayList = new ArrayList<HistoryClass>();

        String[] temp = new String[6];
        Cursor cursor = database.query(DBOpenHelper.TABLE_ISSUE, ISSUE_COLUMN, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                temp[0] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ISSUE_TITLE));
                temp[1] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ISSUE_AUTHOR));
                temp[2] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ISSUE_EDTION));
                temp[3] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ISSUE_ISSUEDATE));
                temp[4] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ISSUE_DUEDATE));
                temp[5] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ISSUE_RETURNDATE));

                logClassArrayList.add(new HistoryClass(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]));
            }
        }
        return logClassArrayList;
    }
    public boolean put_noti_data(ArrayList<NotificationClass> list) {

        for (NotificationClass item : list) {
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.NOTI_SENDER, item.sendto);
            values.put(DBOpenHelper.NOTI_MSG, item.message);
            values.put(DBOpenHelper.NOTI_DATE, item.date);
            database.insert(DBOpenHelper.TABLE_NOTIFICATION, null, values);
        }
        return true;
    }


    public ArrayList<NotificationClass> get_noti_data() {
        ArrayList<NotificationClass> logClassArrayList = new ArrayList<NotificationClass>();

        String[] temp = new String[3];
        Cursor cursor = database.query(DBOpenHelper.TABLE_NOTIFICATION, NOTIFICATION_COLUMN, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                temp[0] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTI_SENDER));
                temp[1] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTI_MSG));
                temp[2] = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTI_DATE));


                logClassArrayList.add(new NotificationClass(temp[0], temp[1], temp[2]));
            }
        }
        return logClassArrayList;
    }


    public void delete_noti_data() {
        Log.i("debug", "deleting notifications");
        database.delete(DBOpenHelper.TABLE_NOTIFICATION, null, null);

    }

    public void delete_hist_data(){
        Log.i("debug", "deleting history");
        database.delete(DBOpenHelper.TABLE_HISTORY, null, null);
    }

    public void delete_issue_data(){
        Log.i("debug", "deleting issue books");
        database.delete(DBOpenHelper.TABLE_ISSUE, null, null);
    }

}
