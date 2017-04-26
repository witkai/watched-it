package com.github.witkai.watchedit.data.local;

import android.provider.BaseColumns;

class PersistenceContract {

    private PersistenceContract() {
    }

    static class EntertainmentTable implements BaseColumns {
        static final String TABLE_NAME = "entertainment";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_TYPE = "type";
        static final String COLUMN_NAME_NOTE = "note";
        static final String COLUMN_NAME_RATING = "rating";
        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_TITLE + " TEXT," +
                        COLUMN_NAME_TYPE + " INT," +
                        COLUMN_NAME_NOTE + " TEXT," +
                        COLUMN_NAME_RATING + " REAL)";
        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class WatchedDateTable implements BaseColumns {
        static final String TABLE_NAME = "watched_date";
        static final String COLUMN_NAME_ENTERTAINMENT_ID = "entertainment_id";
        static final String COLUMN_NAME_DATE = "date";
        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ENTERTAINMENT_ID + " INT," +
                        COLUMN_NAME_DATE + " INT)";
        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
