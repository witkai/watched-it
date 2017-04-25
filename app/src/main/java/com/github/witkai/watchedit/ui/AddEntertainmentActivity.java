package com.github.witkai.watchedit.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.EntertainmentManager;
import com.github.witkai.watchedit.EntertainmentType;
import com.github.witkai.watchedit.R;
import com.github.witkai.watchedit.data.EntertainmentDataSource;
import com.github.witkai.watchedit.data.local.EntertainmentLocalDatasource;

import java.util.Calendar;
import java.util.Date;

public class AddEntertainmentActivity extends AppCompatActivity {

    private EditText mTitle;
    private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        mTitle = (EditText) findViewById(R.id.titleEdit);
        setupToolbar();
        setupCalendar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveMovie();
                Snackbar.make(mTitle, "Entertainment saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                onSupportNavigateUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    private void setupCalendar() {
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setMaxDate(Calendar.getInstance().getTimeInMillis());
    }

    private void saveMovie() {
        String title = mTitle.getText().toString();
        Date watchedDate = new Date(mCalendarView.getDate());

        Entertainment entertainment = new Entertainment(title);
        entertainment.setType(EntertainmentType.MOVIE);
        entertainment.setWatchedDate(watchedDate);

        EntertainmentDataSource dataManager = EntertainmentLocalDatasource.getInstance(this);
        EntertainmentManager movieManager = new EntertainmentManager(dataManager);
        movieManager.add(entertainment);
    }
}
