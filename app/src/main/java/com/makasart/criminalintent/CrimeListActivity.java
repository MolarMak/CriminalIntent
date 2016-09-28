package com.makasart.criminalintent;

import android.app.Fragment;

/**
 * Created by Maxim on 19.09.2016.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
