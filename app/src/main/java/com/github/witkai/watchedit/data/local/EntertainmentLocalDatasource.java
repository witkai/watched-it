package com.github.witkai.watchedit.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.data.EntertainmentDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntertainmentLocalDatasource implements EntertainmentDataSource {

    private static final String TAG = EntertainmentLocalDatasource.class.getSimpleName();

    private static EntertainmentLocalDatasource INSTANCE;

    private DbHelper mDbHelper;

    private EntertainmentLocalDatasource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new DbHelper(context);
    }

    public static EntertainmentLocalDatasource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new EntertainmentLocalDatasource(context);
        }
        return INSTANCE;
    }

    @Override
    public void addEntertainment(@NonNull Entertainment entertainment) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(
                PersistenceContract.EntertainmentTable.COLUMN_NAME_TITLE,
                entertainment.getTitle());
        values.put(
                PersistenceContract.EntertainmentTable.COLUMN_NAME_TYPE,
                entertainment.getType());
        values.put(
                PersistenceContract.EntertainmentTable.COLUMN_NAME_NOTE,
                entertainment.getNotes());
        values.put(
                PersistenceContract.EntertainmentTable.COLUMN_NAME_RATING,
                entertainment.getRating());

        long entertainmentId = db.insert(PersistenceContract.EntertainmentTable.TABLE_NAME, null, values);

        Log.d(TAG, "Entertainment inserted " + entertainmentId);

        if (entertainment.getWatchedDate() != null) {
            values = new ContentValues();
            values.put(
                    PersistenceContract.WatchedDateTable.COLUMN_NAME_ENTERTAINMENT_ID,
                    entertainmentId);
            values.put(
                    PersistenceContract.WatchedDateTable.COLUMN_NAME_DATE,
                    entertainment.getWatchedDate().getTime());
            db.insert(PersistenceContract.WatchedDateTable.TABLE_NAME, null, values);

            Log.d(TAG, "Watched date inserted");
        }
    }

    @Override
    public void addWatchedDate(@NonNull Long movieId, @NonNull Date date) {

    }

    @Override
    public List<Entertainment> allEntertainments() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String entertainmentTableName = PersistenceContract.EntertainmentTable.TABLE_NAME;
        String watchedDateTableName = PersistenceContract.WatchedDateTable.TABLE_NAME;
        String query = "SELECT e._ID,e.title,e.type,e.rating,d.date"
                + " FROM " + entertainmentTableName + " e"
                + " INNER JOIN " + watchedDateTableName + " d"
                + " ON e._ID = d." + PersistenceContract.WatchedDateTable.COLUMN_NAME_ENTERTAINMENT_ID
                + " ORDER BY d.date DESC";
        String[] args = {};
        Cursor cursor = db.rawQuery(query, args);

        List<Entertainment> entertainments = new ArrayList<>();

        while (cursor.moveToNext()) {
            Entertainment entertainment = getEntertainment(cursor);
            entertainments.add(entertainment);
        }

        cursor.close();

        return entertainments;
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(PersistenceContract.WatchedDateTable.TABLE_NAME, null, null);
        db.delete(PersistenceContract.EntertainmentTable.TABLE_NAME, null, null);
    }

    @Override
    public Entertainment getEntertainment(@NonNull Long id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "SELECT e._ID,e.title,e.type,e.rating,e.note,d.date"
                + " FROM " + PersistenceContract.EntertainmentTable.TABLE_NAME + " e"
                + " INNER JOIN " + PersistenceContract.WatchedDateTable.TABLE_NAME + " d"
                + " ON e._ID = d." + PersistenceContract.WatchedDateTable.COLUMN_NAME_ENTERTAINMENT_ID
                + " WHERE e._ID = " + id;
        String[] args = {};
        Cursor cursor = db.rawQuery(query, args);
        cursor.moveToFirst();

        Entertainment entertainment = getEntertainment(cursor);
        // add note
        String note = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        PersistenceContract.EntertainmentTable.COLUMN_NAME_NOTE));
        entertainment.setNote(note);

        return entertainment;
    }

    private Entertainment getEntertainment(Cursor cursor) {
        long id = cursor.getLong(0);
        String title = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        PersistenceContract.EntertainmentTable.COLUMN_NAME_TITLE));
        int type = cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        PersistenceContract.EntertainmentTable.COLUMN_NAME_TYPE));
        long watchedDate = cursor.getLong(
                cursor.getColumnIndexOrThrow(
                        PersistenceContract.WatchedDateTable.COLUMN_NAME_DATE));
        float rating = cursor.getFloat(
                cursor.getColumnIndexOrThrow(
                        PersistenceContract.EntertainmentTable.COLUMN_NAME_RATING));

        Entertainment entertainment = new Entertainment(title);
        entertainment.setId(id);
        entertainment.setType(type);
        entertainment.setRating(rating);
        entertainment.setWatchedDate(new Date(watchedDate));

        return entertainment;
    }
}
