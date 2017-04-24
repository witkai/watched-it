package com.github.witkai.watchedit;

import com.github.witkai.watchedit.data.EntertainmentDataSource;

import java.util.List;

public class EntertainmentManager {

    EntertainmentDataSource dataManager;

    public EntertainmentManager(EntertainmentDataSource dataManager) {
        this.dataManager = dataManager;
    }

    public void add(Entertainment movie) {
        dataManager.addEntertainment(movie);
    }

    public List<Entertainment> all() {
        return dataManager.allEntertainments();
    }
}
