package com.github.witkai.watchedit;

import org.junit.Test;

import java.util.Date;

public class MovieTest {

    @Test
    public void createNew() {
        String title = "Test";
        Date watchedDate = new Date();
        new Entertainment(title, watchedDate);
    }
}
