package com.github.witkai.watchedit.ui.add;

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
import android.widget.RadioButton;
import android.widget.RatingBar;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.EntertainmentType;
import com.github.witkai.watchedit.R;
import com.github.witkai.watchedit.data.EntertainmentDataSource;
import com.github.witkai.watchedit.data.local.EntertainmentLocalDatasource;

import java.util.Calendar;
import java.util.Date;

import static com.github.witkai.watchedit.R.id.calendarView;

public class AddEntertainmentActivity extends AppCompatActivity {

    private EditText mTitle;
    private CalendarView mCalendarView;
    private RadioButton mMovieType;
    private RadioButton mTvShowType;
    private RatingBar mRatingBar;
    private EditText mNotes;
    private long mSelectedDate = Calendar.getInstance().getTimeInMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setupToolbar();
        setupForm();
        setupCalendar();
    }

    private void setupForm() {
        mTitle = (EditText) findViewById(R.id.titleEdit);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mNotes = (EditText) findViewById(R.id.notesText);
        mMovieType = (RadioButton) findViewById(R.id.movieType);
        mTvShowType = (RadioButton) findViewById(R.id.tvShowType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
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
        mCalendarView = (CalendarView) findViewById(calendarView);
        mCalendarView.setMaxDate(Calendar.getInstance().getTimeInMillis());
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                mSelectedDate = calendar.getTimeInMillis();
            }
        });
    }

    private void saveMovie() {
        String title = mTitle.getText().toString();
        if (title.length() > 0) {
            Entertainment entertainment = new Entertainment(title);
            entertainment.setWatchedDate(new Date(mSelectedDate));
            entertainment.setNote(mNotes.getText().toString());
            entertainment.setRating(mRatingBar.getRating());

            if (mMovieType.isChecked()) {
                entertainment.setType(EntertainmentType.MOVIE);
            } else if (mTvShowType.isChecked()) {
                entertainment.setType(EntertainmentType.TV_SHOW);
            }

            EntertainmentDataSource dataSource = EntertainmentLocalDatasource.getInstance(this);
            dataSource.addEntertainment(entertainment);
        }
    }
}
