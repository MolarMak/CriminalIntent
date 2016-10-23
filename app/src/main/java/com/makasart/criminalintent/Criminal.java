package com.makasart.criminalintent;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Maxim on 14.09.2016.
 */
public class Criminal {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";

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

    //JSON package and unpackage
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mID.toString());
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_TITLE, mTitle);
        Log.i("BLUA", json.toString());
        return json;
    }

    public Criminal(JSONObject json) throws JSONException {
        mID = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mDate = new Date(json.getLong(JSON_DATE));
        mSolved = json.getBoolean(JSON_SOLVED);
    }
    //JSON package and unpackage

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
