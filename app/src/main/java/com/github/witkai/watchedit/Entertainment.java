package com.github.witkai.watchedit;

import java.util.Date;

public class Entertainment {

    private Long id;
    private String title;
    private int type;
    private String comment;
    private Date watchedDate;
    private int rating;

    public Entertainment(String title, Date watchedDate) {
        this.title = title;
        this.watchedDate = watchedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getWatchedDate() {
        return watchedDate;
    }

    public void setWatchedDate(Date watchedDate) {
        this.watchedDate = watchedDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
