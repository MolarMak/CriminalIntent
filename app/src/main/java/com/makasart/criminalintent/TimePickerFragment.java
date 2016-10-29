package com.makasart.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Maxim on 24.09.2016.
 */
public class TimePickerFragment extends android.support.v4.app.DialogFragment {

    private Date mDate;
    public static String EXTRA_TIMER = "timer";

    public static int mHour;
    public static int mMinutes;

    public static TimePickerFragment newTimeInstance (Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIMER, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {

        mDate = (Date)getArguments().getSerializable(EXTRA_TIMER);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(mDate);
        final int hour = Calendar.HOUR;
        final int minutes = Calendar.MINUTE;

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.time_picker, null);

        mHour = hour;
        mMinutes = minutes;

        TimePicker mTimePicker = (TimePicker)v.findViewById(R.id.time_picker_fragment);
        mTimePicker.setIs24HourView(true);

        //to support old versions
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minutes);
        } else {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minutes);
        }
        //

        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinutes = minute;
            }
        });
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.set_time)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(Activity.RESULT_OK);
                    }
                }).setView(v)
                .create();
    }

    private void setResult (int recCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent mIntent = new Intent();
        mIntent.putExtra(CriminalFragment.RETURN_HOUR, mHour);
        mIntent.putExtra(CriminalFragment.RETURN_MINUTES, mMinutes);
        getTargetFragment().onActivityResult(getTargetRequestCode(), recCode, mIntent);
    }
}
