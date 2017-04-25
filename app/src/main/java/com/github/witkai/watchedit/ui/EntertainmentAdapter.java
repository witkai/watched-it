package com.github.witkai.watchedit.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.witkai.watchedit.Entertainment;
import com.github.witkai.watchedit.R;

import java.util.List;

public class EntertainmentAdapter extends RecyclerView.Adapter<EntertainmentAdapter.MyViewHolder> {

    private List<Entertainment> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }


    public EntertainmentAdapter(List<Entertainment> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Entertainment movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}