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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.EntertainmentManager;
import com.github.witkai.watchedit.R;
import com.github.witkai.watchedit.data.EntertainmentDataSource;
import com.github.witkai.watchedit.data.local.DbHelper;

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
                        AddMovieActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupMoviesList() {
        mMoviesList = (RecyclerView) findViewById(R.id.moviesList);
        mMoviesList.setHasFixedSize(true);
        mMoviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mMoviesList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mMoviesList.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadData() {
        EntertainmentDataSource dataManager = new DbHelper(getApplicationContext());
        EntertainmentManager movieManager = new EntertainmentManager(dataManager);
        List<Entertainment> movies = movieManager.all();
        RecyclerView.Adapter mAdapter = new MoviesAdapter(movies);
        mMoviesList.setAdapter(mAdapter);
    }
}
