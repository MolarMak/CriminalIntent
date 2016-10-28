package com.makasart.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Maxim on 26.10.2016.
 */

public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
