package com.github.witkai.watchedit.data;

import android.support.annotation.NonNull;

import com.github.witkai.watchedit.Entertainment;

import java.util.Date;
import java.util.List;

public interface EntertainmentDataSource {

    void addEntertainment(@NonNull Entertainment entertainment);

    void addWatchedDate(@NonNull Long movieId, @NonNull Date date);

    List<Entertainment> allEntertainments();

    void deleteAll();

    Entertainment getEntertainment(@NonNull Long id);
}
