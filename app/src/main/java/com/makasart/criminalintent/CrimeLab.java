package com.makasart.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
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

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimes = new ArrayList<Criminal>();
    }

    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Criminal> getCrimes() {
        return mCrimes;
    }

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

    public Criminal getCrime(UUID id) {
        for (Criminal c: mCrimes) {
            if (c.getID().equals(id))
                return c;
        }
        return null;
    }
}
