package com.bignerdranch.android.automateddrivinglog.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.automateddrivinglog.Session;
import com.bignerdranch.android.automateddrivinglog.database.SessionDbSchema.SessionTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by howes on 2/13/2018.
 */

public class SessionCursorWrapper extends CursorWrapper {
    public SessionCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Session getSession() {
        String uuidString = getString(getColumnIndex(SessionTable.Cols.UUID));
        String title = getString(getColumnIndex(SessionTable.Cols.TITLE));
        long date = getLong(getColumnIndex(SessionTable.Cols.DATE));
        String duration = getString(getColumnIndex(SessionTable.Cols.DURATION));

        Session session = new Session(UUID.fromString(uuidString));
        session.setTitle(title);
        session.setDate(new Date(date));
        session.setDuration(duration);

        return session;

    }
}
