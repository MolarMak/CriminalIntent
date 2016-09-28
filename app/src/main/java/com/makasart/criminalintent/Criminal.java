package com.makasart.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Maxim on 14.09.2016.
 */
public class Criminal {

    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private int mHour = -1;
    private int mMinutes = -1;

    public Criminal(){
        mID = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public void setMinutes(int minutes) {
        mMinutes = minutes;
    }
}
