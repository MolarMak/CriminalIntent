package com.makasart.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * Created by Maxim on 14.09.2016.
 */
public class CriminalFragment extends android.support.v4.app.Fragment {

    Criminal mCriminal;

    private EditText mEditText;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mDeleteCrimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private int mHour, mMinutes;

    public final static String EXTRA_CRIME_ID = "com.makasart.criminalintent.CriminalFragment.EXTRA_CRIME_ID";
    public static final String RETURN_DATE = "RETURN_DATE";
    public static final String RETURN_HOUR = "RETURN_HOUR";
    public static final String RETURN_MINUTES = "RETURN_MINUTES";
    public static final String TAG = "CriminalFragment";

    private static final String DIALOG_TIME = "time";
    private static final int TIME_CONST = 1;
    private static final String DIALOG_DATE = "date";
    private static final int DIALOG_CONST = 0;

    private ImageButton mImageButton;
    private static final int REQUEST_PHOTO = 2;
    public static final String EXTRA_PHOTO_FILENAME = "com.makasart.criminalintent.photo_filename";

    private static final int REQUEST_CONTACT = 3;


    public static boolean isLogged = true;

    private ImageView mImageView;
    private static final String DIALOG_IMAGE = "image";

    private CrimeListFragment.Callbacks mCallbacks;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (CrimeListFragment.Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        UUID crimeID = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCriminal = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    //JSON Save
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }
    //JSON Save

    @Override
    public void onStart() {
        super.onStart();
        ShowPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mImageView);
    }

    public static CriminalFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        CriminalFragment fragment = new CriminalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if(NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        mEditText = (EditText)v.findViewById(R.id.edit_text);
        mEditText.setText(mCriminal.getTitle());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCallbacks.onCrimeUpdater(mCriminal);
                mCriminal.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.date_button);

        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               android.support.v4.app.FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCriminal.getDate());
                dialog.setTargetFragment(CriminalFragment.this, DIALOG_CONST);
                dialog.show(fm, DIALOG_DATE);
                if (isLogged) {
                    Log.d(TAG, "In CF onclick DateButton");
                }
            }
        });

        mTimeButton = (Button)v.findViewById(R.id.time_button);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm2 = getActivity()
                        .getSupportFragmentManager();
                TimePickerFragment timePicker = TimePickerFragment.newTimeInstance(mCriminal.getDate());
                timePicker.setTargetFragment(CriminalFragment.this, TIME_CONST);
                timePicker.show(fm2, DIALOG_TIME);
            }
        });

        mHour = mCriminal.getHour();
        mMinutes = mCriminal.getMinutes();
        if (mHour != -1 && mMinutes != -1) {
            String timeIsNow = Integer.toString(mHour)+":"+Integer.toString(mMinutes);
            mTimeButton.setText(timeIsNow);
        }

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.checkbox_view);
        mSolvedCheckBox.setChecked(mCriminal.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCriminal.setSolved(isChecked);
                mCallbacks.onCrimeUpdater(mCriminal);
            }
        });

        mDeleteCrimeButton = (Button)v.findViewById(R.id.delete_button);
        mDeleteCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeLab.get(getActivity()).deleteCrime(mCriminal);
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
            }
        });
        mImageButton = (ImageButton)v.findViewById(R.id.imageButton);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });
        PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            mImageButton.setEnabled(false);
        }
        mImageView = (ImageView)v.findViewById(R.id.crime_ImageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mCriminal.getPhoto();
                if (p == null)
                    return;

                FragmentManager fm = getActivity().getFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);

            }
        });
        mReportButton = (Button)v.findViewById(R.id.report_button);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = i.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mSuspectButton = (Button)v.findViewById(R.id.suspend_button);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });
        if (mCriminal.getSuspect() != null) {
            mSuspectButton.setText(mCriminal.getSuspect());
        }
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == DIALOG_CONST) {
            Date date = (Date) data.getSerializableExtra(RETURN_DATE);
            mCriminal.setDate(date);
            mCallbacks.onCrimeUpdater(mCriminal);
            updateDate();
        }
        if (requestCode == TIME_CONST) {
            mHour = (Integer) data.getSerializableExtra(RETURN_HOUR);
            mMinutes = (Integer) data.getSerializableExtra(RETURN_MINUTES);
            String timeIsNow = Integer.toString(mHour)+":"+Integer.toString(mMinutes);
            mCriminal.setHour(mHour);
            mCriminal.setMinutes(mMinutes);
            mCallbacks.onCrimeUpdater(mCriminal);
            mTimeButton.setText(timeIsNow);
        }
        if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                if (isLogged) {
                    Log.d(TAG, filename+ " was catching!");
                }
                Photo mPhoto = new Photo(filename);
                mCriminal.setPhoto(mPhoto);
                if (isLogged) {
                    Log.d(TAG, mCriminal.getTitle() + " has a photo " + filename + "!");
                }
                mCallbacks.onCrimeUpdater(mCriminal);
                ShowPhoto();
            }
        }
        if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();

            String []queryField = new String [] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getActivity().getContentResolver().query(contactUri, queryField,
                    null, null, null);
            if (c.getCount() == 0) {
                c.close();
                return;
            }

            c.moveToFirst();
            String suspect = c.getString(0);
            mCriminal.setSuspect(suspect);
            mCallbacks.onCrimeUpdater(mCriminal);
            mSuspectButton.setText(suspect);
            c.close();
        }
    }

    private void ShowPhoto() {
        Photo p = mCriminal.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mImageView.setImageDrawable(b);
    }

    public synchronized void updateDate() {
        String replacement;
        replacement = (String) DateFormat.format("E/d/M/y", mCriminal.getDate());
        mDateButton.setText(replacement);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCriminal.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        }
        if (!mCriminal.isSolved()) {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MM dd";
        String dateString = DateFormat.format(dateFormat, Calendar.getInstance()).toString();
        Log.d("MYSS", dateString);

        String suspect = mCriminal.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report,
                mCriminal.getTitle(), dateString, solvedString, suspect);
    }
}
