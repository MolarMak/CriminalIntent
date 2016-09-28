package com.makasart.criminalintent;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.DatePicker;

        import java.util.Calendar;
        import java.util.Date;
        import java.util.GregorianCalendar;

/**
 * Created by Maxim on 22.09.2016.
 */
public class DatePickerFragment extends android.support.v4.app.DialogFragment {
    public static final String EXTRA_DATE = "com.makasart.criminalintent.EXTRA_DATE";

    private Date mDate;

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        if (CriminalFragment.isLogged) {
            Log.d(CriminalFragment.TAG, "In new Instance DatePickerFragment");
        }
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (CriminalFragment.isLogged) {
            Log.d(CriminalFragment.TAG, "In onCreateDialog DatePickerFragment");
        }
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(mDate);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.date_picker, null);

        DatePicker mDatePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setView(v)
                .create();
    }

    private void sendResult(int recCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent mIntent = new Intent();
        mIntent.putExtra(CriminalFragment.RETURN_DATE, mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), recCode, mIntent);
    }

}
