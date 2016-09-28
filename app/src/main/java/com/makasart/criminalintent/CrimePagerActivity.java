package com.makasart.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by Maxim on 22.09.2016.
 */
public class CrimePagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<Criminal> mCrimes;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();

        android.support.v4.app.FragmentManager fm5 = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm5) {
            @Override
            public int getCount() {
                return mCrimes.size();
            }

            @Override
            public Fragment getItem(int pos) {
                Criminal Crime = mCrimes.get(pos);
                return CriminalFragment.newInstance(Crime.getID());
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra(CriminalFragment.EXTRA_CRIME_ID);
        for (int i = 0; i < mCrimes.size(); i++) {
            if(mCrimes.get(i).getID().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Criminal crime = mCrimes.get(position);
                if (crime.getTitle() != null) {
                    setTitle(crime.getTitle());
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
