package com.github.witkai.watchedit;

import org.junit.Test;

import java.util.Date;

public class EntertainmentTest {

    @Test
    public void createNew() {
        String title = "Test";
        Date watchedDate = new Date();
        Entertainment entertainment = new Entertainment(title);
        entertainment.setWatchedDate(watchedDate);
    }
}
