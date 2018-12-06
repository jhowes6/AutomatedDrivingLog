package com.bignerdranch.android.automateddrivinglog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.automateddrivinglog.database.SessionBaseHelper;
import com.bignerdranch.android.automateddrivinglog.database.SessionCursorWrapper;
import com.bignerdranch.android.automateddrivinglog.database.SessionDbSchema.SessionTable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//************************************************************************************************
//SessionLog
//      *class which stores an array of Sessions.
//
//************************************************************************************************
public class SessionLog {
    private static SessionLog sSessionLog;
    private List<Session> mSessions;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    //*******************************************************************************************
    //get(Context context)
    //      *returns a SessionLog if one does not already exist
    //
    //*******************************************************************************************
    public static SessionLog get(Context context) {
        if (sSessionLog == null)
            sSessionLog = new SessionLog(context);
        return sSessionLog;
    }
    //*******************************************************************************************
    //SessionLog(Context context)
    //      *constructor for SessionLog
    //      *instantiates mDatabase
    //
    //*******************************************************************************************
    private SessionLog(Context context) {
        String d = "10";

        mContext = context.getApplicationContext();
        mDatabase = new SessionBaseHelper(context)
                .getWritableDatabase();
    }
    //*******************************************************************************************
    //addSession(Session s)
    //      *extracts content values from s and inserts them into mDatabase
    //
    //*******************************************************************************************
    public void addSession(Session s) {

        int x = getSessions().size();
        String tit = "Session " + (x+1);
        s.setTitle(tit);

        ContentValues values = getContentValues(s);

        mDatabase.insert(SessionTable.NAME, null, values);
    }
    //*******************************************************************************************
    //getSessions()
    //      *returns the list of Sessions as an ArrayList
    //
    //*******************************************************************************************
    public List<Session> getSessions() {


        List<Session> sessions = new ArrayList<>();

        SessionCursorWrapper cursor = querySessions(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                sessions.add(cursor.getSession());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return sessions;
    }
    //*******************************************************************************************
    //getSession(UUID id)
    //      *returns the Session with the corresponding UUID
    //
    //*******************************************************************************************
    public Session getSession(UUID id) {


        SessionCursorWrapper cursor = querySessions(
                SessionTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {

            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getSession();
        } finally {
            cursor.close();
        }
    }
    //*******************************************************************************************
    //updateSession(Session session)
    //      *updates mDatabase with session
    //
    //*******************************************************************************************
    public void updateSession(Session session) {
        String uuidString = session.getId().toString();
        ContentValues values = getContentValues(session);

        mDatabase.update(SessionTable.NAME, values,
                SessionTable.Cols.UUID + " = ?",
                new String[] { uuidString});
    }
    //*******************************************************************************************
    //getContentValues(Session session)
    //      *extracts content values from SessionTable and returns them
    //
    //*******************************************************************************************
    private static ContentValues getContentValues(Session session) {
        ContentValues values = new ContentValues();
        values.put(SessionTable.Cols.UUID, session.getId().toString());
        values.put(SessionTable.Cols.TITLE, session.getTitle());
        values.put(SessionTable.Cols.DATE, session.getDate().getTime());
        values.put(SessionTable.Cols.DURATION, session.getDuration());
        return values;
    }
    //*******************************************************************************************
    //querySessions(String whereClause, String[] whereArgs)
    //      *returns a new SessionCursorWrapper
    //
    //*******************************************************************************************
    private SessionCursorWrapper querySessions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SessionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new SessionCursorWrapper(cursor);
    }
}
