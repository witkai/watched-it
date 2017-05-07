package com.github.witkai.watchedit.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.EntertainmentType;
import com.github.witkai.watchedit.R;
import com.github.witkai.watchedit.data.EntertainmentDataSource;
import com.github.witkai.watchedit.data.local.EntertainmentLocalDatasource;
import com.github.witkai.watchedit.ui.add.AddEntertainmentActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ViewEntertainmentActivity extends AppCompatActivity {

    public static final String ENTERTAINMENT_ID_EXTRA = "entertainment_id";

    private Entertainment mEntertainment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setupToolbar();
        loadEntertainment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadEntertainment() {
        long entertainmentId = getIntent().getLongExtra(ENTERTAINMENT_ID_EXTRA, -1);
        EntertainmentDataSource dataSource = EntertainmentLocalDatasource.getInstance(this);
        mEntertainment = dataSource.getEntertainment(entertainmentId);
        if (mEntertainment != null) {
            TextView titleText = (TextView) findViewById(R.id.titleText);
            TextView typeText = (TextView) findViewById(R.id.typeText);
            TextView watchedDateText = (TextView) findViewById(R.id.watchedDateText);
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            TextView notesText = (TextView) findViewById(R.id.notesText);

            titleText.setText(mEntertainment.getTitle());
            String type;
            switch (mEntertainment.getType()) {
                case EntertainmentType.MOVIE:
                    type = getResources().getString(R.string.type_movie);
                    break;
                case EntertainmentType.TV_SHOW:
                    type = getResources().getString(R.string.type_tv_show);
                    break;
                default:
                    type = "";
            }
            typeText.setText(type);
            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            String formatedDate = dateFormat.format(mEntertainment.getWatchedDate());
            watchedDateText.setText(formatedDate);
            ratingBar.setRating(mEntertainment.getRating());
            notesText.setText(mEntertainment.getNotes());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                // TODO
                Intent intent = new Intent(this, AddEntertainmentActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void watchTrailerOnYoutube() {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        intent.putExtra("query", mEntertainment.getTitle() + " trailer");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
