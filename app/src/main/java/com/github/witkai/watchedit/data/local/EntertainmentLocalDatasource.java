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

import static android.R.attr.rating;
import static com.google.common.base.Preconditions.checkNotNull;

public class EntertainmentLocalDatasource implements EntertainmentDataSource{

    private static final String TAG = EntertainmentLocalDatasource.class.getSimpleName();

    private static EntertainmentLocalDatasource INSTANCE;

    private DbHelper mDbHelper;

    // Prevent direct instantiation.
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
                PersistenceContract.EntertainmentTable.COLUMN_NAME_COMMENT,
                entertainment.getComment());
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
    public void addWatchedDate(long movieId, @NonNull Date date) {

    }

    @Override
    public List<Entertainment> allEntertainments() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "select * from " + PersistenceContract.EntertainmentTable.TABLE_NAME;
        String[] args = {  };
        Cursor cursor = db.rawQuery(query, args);

        List<Entertainment> entertainments = new ArrayList<>();

        while(cursor.moveToNext()) {
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            PersistenceContract.EntertainmentTable.COLUMN_NAME_TITLE));
            int type = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            PersistenceContract.EntertainmentTable.COLUMN_NAME_TYPE));
            String comment = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            PersistenceContract.EntertainmentTable.COLUMN_NAME_COMMENT));
//            long watchedDate = cursor.getLong(
//                    cursor.getColumnIndexOrThrow(PersistenceContract.EntertainmentTable.COLUMN_NAME_WATCHED_DATE));
            int rating = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            PersistenceContract.EntertainmentTable.COLUMN_NAME_RATING));

            Entertainment entertainment = new Entertainment(title);
            entertainment.setType(type);
            entertainment.setRating(rating);
            entertainment.setComment(comment);

            entertainments.add(entertainment);
        }

        cursor.close();

        return entertainments;
    }

    public List<Entertainment> allEntertainmentsOld() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PersistenceContract.EntertainmentTable._ID,
                PersistenceContract.EntertainmentTable.COLUMN_NAME_TITLE,
                PersistenceContract.EntertainmentTable.COLUMN_NAME_RATING
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = PersistenceContract.EntertainmentTable.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {"My Title"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                PersistenceContract.EntertainmentTable.COLUMN_NAME_WATCHED_DATE + " DESC";

        Cursor cursor = db.query(
                PersistenceContract.EntertainmentTable.TABLE_NAME,          // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Entertainment> movies = new ArrayList<>();
        while(cursor.moveToNext()) {
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(PersistenceContract.EntertainmentTable.COLUMN_NAME_TITLE));
            long watchedDate = cursor.getLong(
                    cursor.getColumnIndexOrThrow(PersistenceContract.EntertainmentTable.COLUMN_NAME_WATCHED_DATE));
//            int rating = cursor.getInt(
//                    cursor.getColumnIndexOrThrow(PersistenceContract.Entertainment.COLUMN_NAME_RATING));

            Entertainment movie = new Entertainment(title);
            movie.setRating(rating);

            movies.add(movie);
        }

        cursor.close();

        return movies;
    }

}
