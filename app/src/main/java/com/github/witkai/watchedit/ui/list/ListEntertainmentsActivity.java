package com.github.witkai.watchedit.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListEntertainmentsActivity extends AppCompatActivity {

    private RecyclerView mMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setupAddFab();
        setupMoviesList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupAddFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        ListEntertainmentsActivity.this,
                        AddEntertainmentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupMoviesList() {
        mMoviesList = (RecyclerView) findViewById(R.id.moviesList);
        mMoviesList.setHasFixedSize(true);
        mMoviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mMoviesList.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mMoviesList.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadData() {
        EntertainmentDataSource datasource =
                EntertainmentLocalDatasource.getInstance(getApplicationContext());
        List<Entertainment> movies = datasource.allEntertainments();
        RecyclerView.Adapter mAdapter = new EntertainmentAdapter(movies);
        mMoviesList.setAdapter(mAdapter);
    }

    static class EntertainmentAdapter extends RecyclerView.Adapter<EntertainmentAdapter.MyViewHolder> {

        private List<Entertainment> moviesList;
        private Date mLastYear;
        private DateFormat mShortDateFormat;

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, date, type;
            RatingBar rating;

            MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                date = (TextView) view.findViewById(R.id.date);
                rating = (RatingBar) view.findViewById(R.id.rating);
                type = (TextView) view.findViewById(R.id.type);
            }
        }

        EntertainmentAdapter(List<Entertainment> moviesList) {
            this.moviesList = moviesList;
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, -6);
            mLastYear = c.getTime();
            mShortDateFormat = new SimpleDateFormat("MMM dd");
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Entertainment entertainment = moviesList.get(position);
            holder.title.setText(entertainment.getTitle());
            holder.date.setText(formatDate(entertainment.getWatchedDate()));
            holder.rating.setRating(entertainment.getRating());
            holder.type.setText(getType(entertainment));
        }

        @Nullable
        private String getType(Entertainment entertainment) {
            String type;
            switch (entertainment.getType()) {
                case EntertainmentType.MOVIE:
                    type = "Movie";
                    break;
                case EntertainmentType.TV_SHOW:
                    type = "TV Show";
                    break;
                default:
                    type = null;
            }
            return type;
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        private String formatDate(Date date) {
            String formattedDate;
            if (date.after(mLastYear)) {
                formattedDate = mShortDateFormat.format(date);
            } else {
                formattedDate = SimpleDateFormat.getDateInstance().format(date);
            }
            return formattedDate;
        }
    }
}
