package com.github.witkai.watchedit.ui;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.R;
import com.github.witkai.watchedit.data.EntertainmentDataSource;
import com.github.witkai.watchedit.data.local.EntertainmentLocalDatasource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        MainActivity.this,
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
            public TextView title, date;

            MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                date = (TextView) view.findViewById(R.id.date);
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
                    .inflate(R.layout.movie_list_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Entertainment entertainment = moviesList.get(position);
            holder.title.setText(entertainment.getTitle());
            holder.date.setText(formatDate(entertainment.getWatchedDate()));
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
