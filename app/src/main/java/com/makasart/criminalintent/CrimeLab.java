package com.makasart.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Maxim on 16.09.2016.
 */
public class CrimeLab {
    private ArrayList<Criminal> mCrimes;
    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private CriminalIntentJSONSerializer mSerializer;

    public void addCrime(Criminal c) {
        mCrimes.add(c);
    }

    //JSON load
    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(appContext, FILENAME);
        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<Criminal>();
            Log.e(TAG, "Error loading page ", e);
        }
    }
    //JSON load

    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Criminal> getCrimes() {
        return mCrimes;
    }

    //JSON save
    public boolean saveCrimes() {
        try {
            mAppContext.getApplicationContext();
            mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "JSON saved to file!");
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error saved JSON ", e);
            return false;
        }
    }
    //JSON save

    public Criminal getCrime(UUID id) {
        for (Criminal c: mCrimes) {
            if (c.getID().equals(id))
                return c;
        }
        return null;
    }
}
