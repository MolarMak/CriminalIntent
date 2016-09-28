package com.makasart.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Maxim on 16.09.2016.
 */
public class CrimeLab {
    private ArrayList<Criminal> mCrimes;
    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimes = new ArrayList<Criminal>();
        for (int i = 0; i < 100; i++) {
            Criminal cr = new Criminal();
            cr.setTitle("Crime #"+i);
            cr.setSolved(i % 2 == 0);
            mCrimes.add(cr);
        }
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

    public Criminal getCrime(UUID id) {
        for (Criminal c: mCrimes) {
            if (c.getID().equals(id))
                return c;
        }
        return null;
    }
}
