package com.github.witkai.watchedit.ui.list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.EntertainmentType;
import com.github.witkai.watchedit.R;
import com.github.witkai.watchedit.ui.view.ViewEntertainmentActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class EntertainmentAdapter
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
        holder.id = entertainment.getId();
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

        long id;
        TextView title, date, type;
        RatingBar rating;
        private Context context;

        RowViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            rating = (RatingBar) view.findViewById(R.id.rating);
            type = (TextView) view.findViewById(R.id.type);
            context = view.getContext();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openEntertainmentDetail(id);
                }
            });
        }

        @Override
        public void onClick(View v) {
            openEntertainmentDetail(getItemId());
        }

        @Override
        public void openEntertainmentDetail(long id) {
            Intent intent = new Intent(context, ViewEntertainmentActivity.class);
            intent.putExtra(ViewEntertainmentActivity.ENTERTAINMENT_ID_EXTRA, id);
            context.startActivity(intent);
        }
    }
}
