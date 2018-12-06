package com.bignerdranch.android.automateddrivinglog;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

/**
 * Created by howes on 2/10/2018.
 */

public class Session {

    private UUID mId;

    private String mTitle;
    private Date mDate;
    private String duration; //in seconds

    public Session(){
        this(UUID.randomUUID());
    }
    public Session(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
}
