package com.bignerdranch.android.automateddrivinglog.database;

import android.provider.BaseColumns;

/**
 * Created by howes on 2/13/2018.
 */

public class SessionDbSchema {
    public static final class SessionTable {
        public static final String NAME = "sessions";

        public static  class Cols   {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String DURATION = "duration";
        }
    }
}
