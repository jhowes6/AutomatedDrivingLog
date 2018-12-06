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

/**
 * Created by howes on 2/10/2018.
 */

public class SessionLog {
    private static SessionLog sSessionLog;
    private List<Session> mSessions;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static SessionLog get(Context context) {
        if (sSessionLog == null)
            sSessionLog = new SessionLog(context);
        return sSessionLog;
    }
    private SessionLog(Context context) {
        String d = "10";

        mContext = context.getApplicationContext();
        mDatabase = new SessionBaseHelper(context)
                .getWritableDatabase();
        /*

        mSessions = new ArrayList<>();
        Session session1 = new Session();
        session1.setTitle("Session 1");
        session1.setDuration(d);
        mSessions.add(session1);
        Session session2 = new Session();
        session2.setTitle("Session 2");
        session2.setDuration("12");
        mSessions.add(session2);
        Session session3 = new Session();
        session3.setTitle("Session 3");
        session3.setDuration("25");
        mSessions.add(session3);
        */

    }
    public void addSession(Session s) {
        //mSessions.add(s);
        int x = getSessions().size();
        String tit = "Session " + (x+1);
        s.setTitle(tit);

        ContentValues values = getContentValues(s);

        mDatabase.insert(SessionTable.NAME, null, values);
    }
    public List<Session> getSessions() {
        //return mSessions;

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
    public Session getSession(UUID id) {
        /*

        for(Session session : mSessions) {
            if(session.getId().equals(id))
                return session;
        }
        return null; */

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
    public void updateSession(Session session) {
        String uuidString = session.getId().toString();
        ContentValues values = getContentValues(session);

        mDatabase.update(SessionTable.NAME, values,
                SessionTable.Cols.UUID + " = ?",
                new String[] { uuidString});
    }
    private static ContentValues getContentValues(Session session) {
        ContentValues values = new ContentValues();
        values.put(SessionTable.Cols.UUID, session.getId().toString());
        values.put(SessionTable.Cols.TITLE, session.getTitle());
        values.put(SessionTable.Cols.DATE, session.getDate().getTime());
        values.put(SessionTable.Cols.DURATION, session.getDuration());
        return values;
    }
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
