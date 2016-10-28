package com.makasart.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Maxim on 28.10.2016.
 */

public class Photo {
    public static final String JSON_FILENAME = "filename";
    private String mFileName;

    public Photo(String filename) {
        mFileName = filename;
    }
    public Photo(JSONObject json) throws JSONException {
        mFileName = json.getString(JSON_FILENAME);
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFileName);
        return json;
    }
    public String getFilename() {
        return mFileName;
    }
}
