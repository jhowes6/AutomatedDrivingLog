package com.bignerdranch.android.automateddrivinglog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.automateddrivinglog.database.SessionDbSchema.SessionTable;

/**
 * Created by howes on 2/13/2018.
 */

public class SessionBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "sessionBase.db";

    public SessionBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + SessionTable.NAME + "(" +
            SessionTable.Cols.UUID + ", " +
            SessionTable.Cols.TITLE + ", " +
            SessionTable.Cols.DATE + ", " +
            SessionTable.Cols.DURATION +
            ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
