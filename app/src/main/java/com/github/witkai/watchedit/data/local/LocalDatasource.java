package com.github.witkai.watchedit.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.data.EntertainmentDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.rating;
import static com.google.common.base.Preconditions.checkNotNull;

public class LocalDatasource implements EntertainmentDataSource{

    private static LocalDatasource INSTANCE;

    private DbHelper mDbHelper;

    // Prevent direct instantiation.
    private LocalDatasource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new DbHelper(context);
    }

    public static LocalDatasource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDatasource(context);
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
                entertainment.getType());
        values.put(
                PersistenceContract.EntertainmentTable.COLUMN_NAME_RATING,
                entertainment.getRating());

        long entertainmentId = db.insert(PersistenceContract.EntertainmentTable.TABLE_NAME, null, values);

        if (entertainment.getWatchedDate() != null) {
            values = new ContentValues();
            values.put(
                    PersistenceContract.WatchedDateTable.COLUMN_NAME_ENTERTAINMENT_ID,
                    entertainmentId);
            values.put(
                    PersistenceContract.WatchedDateTable.COLUMN_NAME_DATE,
                    entertainment.getWatchedDate().getTime());
            db.insert(PersistenceContract.WatchedDateTable.TABLE_NAME, null, values);
        }
    }

    @Override
    public void addWatchedDate(long movieId, @NonNull Date date) {

    }

    @Override
    public List<Entertainment> allEntertainments() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PersistenceContract.EntertainmentTable._ID,
                PersistenceContract.EntertainmentTable.COLUMN_NAME_TITLE,
                PersistenceContract.EntertainmentTable.COLUMN_NAME_WATCHED_DATE
//                PersistenceContract.Entertainment.COLUMN_NAME_RATING
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

            Entertainment movie = new Entertainment(title, new Date(watchedDate));
            movie.setRating(rating);

            movies.add(movie);
        }

        cursor.close();

        return movies;
    }

}
