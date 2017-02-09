package com.example.jazibhassan.thelibrary.dbpackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jazib Hassan on 22-Nov-15.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "library.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTIFICATION = "messages";
    public static final String NOTI_SENDER = "sender";
    public static final String NOTI_MSG = "msg";
    public static final String NOTI_DATE = "date_send";

    public static final String TABLE_HISTORY = "historytable";
    public static final String HISTORY_TITLE = "histitle";
    public static final String HISTORY_AUTHOR = "hisauthor";
    public static final String HISTORY_ISSUEDATE = "issuedate";
    public static final String HISTORY_EDTION = "edition";
    public static final String HISTORY_DUEDATE = "duedate";
    public static final String HISTORY_RETURNDATE = "returndate";

    public static final String TABLE_ISSUE = "issuetable";
    public static final String ISSUE_TITLE = "title";
    public static final String ISSUE_AUTHOR = "authr";
    public static final String ISSUE_EDTION = "edition";
    public static final String ISSUE_ISSUEDATE = "issuedate";
    public static final String ISSUE_DUEDATE = "duedate";
    public static final String ISSUE_RETURNDATE = "returndate";

    public static final String NOTI_CREATE = "Create table if not exists " + TABLE_NOTIFICATION
            + " (" + NOTI_SENDER + " Varchar, "
            + NOTI_MSG + " varchar, "
            + NOTI_DATE + " varchar)";

    public static final String HISTORY_CREATE = "Create table if not exists " + TABLE_HISTORY
            + " (" + HISTORY_TITLE + " Varchar, "
            + HISTORY_AUTHOR + " varchar, "
            + HISTORY_EDTION + " varchar, "
            + HISTORY_ISSUEDATE + " varchar, "
            + HISTORY_DUEDATE + " varchar, "
            + HISTORY_RETURNDATE + " varchar)";

    public static final String ISSUE_CREATE = "Create table if not exists " + TABLE_ISSUE
            + " (" + ISSUE_TITLE + " Varchar, "
            + ISSUE_AUTHOR + " varchar, "
            + ISSUE_EDTION + " varchar, "
            + ISSUE_ISSUEDATE + " varchar, "
            + ISSUE_DUEDATE + " varchar, "
            + ISSUE_RETURNDATE + " varchar)";


    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(NOTI_CREATE);
        db.execSQL(HISTORY_CREATE);
        db.execSQL(ISSUE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
