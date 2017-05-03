package com.github.witkai.watchedit.ui.list;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import com.github.witkai.watchedit.util.CSVFile;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListEntertainmentsActivity
        extends AppCompatActivity
        implements EntertainmentsNavigator {

    private static final String TAG = ListEntertainmentsActivity.class.getSimpleName();
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
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.action_import) {
            importFromCSV();
        } else if (id == R.id.action_delete_all) {
            EntertainmentLocalDatasource.getInstance(this).deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addNewEntertainment() {
        Intent intent = new Intent(
                ListEntertainmentsActivity.this,
                AddEntertainmentActivity.class);
        startActivity(intent);
    }

    private void setupAddFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewEntertainment();
            }
        });
    }

    private void importFromCSV() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.movies);
            CSVFile csvFile = new CSVFile(inputStream);
            List<String[]> moviesList = csvFile.read();
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            EntertainmentDataSource dataSource = EntertainmentLocalDatasource.getInstance(this);
            for (String[] row : moviesList) {
                Date watchedDate = formatter.parse(row[0]);
                String title = row[1];
                Float rating = Float.parseFloat(row[3]);
                Entertainment entertainment = new Entertainment(title);
                entertainment.setType(EntertainmentType.MOVIE);
                entertainment.setWatchedDate(watchedDate);
                entertainment.setRating(rating);
                dataSource.addEntertainment(entertainment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void showAboutDialog() {
        String version;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_about, null);
        TextView versionText = (TextView) view.findViewById(R.id.version);
        versionText.setText("Version: " + version);
        builder.setView(view);
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }

    static class EntertainmentAdapter
            extends RecyclerView.Adapter<EntertainmentAdapter.RowViewHolder> {

        private List<Entertainment> moviesList;
        private Date mLastYear;
        private DateFormat mShortDateFormat;

        EntertainmentAdapter(List<Entertainment> moviesList) {
            this.moviesList = moviesList;
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, -6);
            mLastYear = c.getTime();
            mShortDateFormat = new SimpleDateFormat("MMM dd");
        }

        @Override
        public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);
            return new RowViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RowViewHolder holder, int position) {
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
                    // TODO - use string resource
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

        // TODO - extract into external class
        private String formatDate(Date date) {
            String formattedDate;
            if (date.after(mLastYear)) {
                formattedDate = mShortDateFormat.format(date);
            } else {
                formattedDate = SimpleDateFormat.getDateInstance().format(date);
            }
            return formattedDate;
        }

        class RowViewHolder
                extends RecyclerView.ViewHolder
                implements View.OnClickListener, EntertainmentItemNavigator {

            TextView title, date, type;
            RatingBar rating;

            RowViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                date = (TextView) view.findViewById(R.id.date);
                rating = (RatingBar) view.findViewById(R.id.rating);
                type = (TextView) view.findViewById(R.id.type);
            }

            @Override
            public void onClick(View v) {
                openEntertainmentDetail(getItemId());
            }

            @Override
            public void openEntertainmentDetail(long id) {
                // TODO
            }
        }
    }
}
