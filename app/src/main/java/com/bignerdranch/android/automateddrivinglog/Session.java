package com.bignerdranch.android.automateddrivinglog;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

//************************************************************************************************
//Session
//      *Class which represents an instance of a driving session
//
//************************************************************************************************
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

    public String getDateString() {
        String myDate = mDate.toString();
        String[] tokens = myDate.split(":");
        myDate = tokens[0];
        String year = tokens[2];
        year = year.substring(6);
        myDate = myDate.substring(0, myDate.length() - 2);
        myDate = myDate + ", " + year;
        return myDate;
    }
    public Date getDate(){
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
