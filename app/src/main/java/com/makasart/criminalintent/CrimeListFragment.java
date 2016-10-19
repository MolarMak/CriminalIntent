package com.makasart.criminalintent;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Maxim on 19.09.2016.
 */
public class CrimeListFragment extends ListFragment {
    private ArrayList<Criminal> mCrimes;
    private boolean isLogged = true;
    private final static String TAG = "Criminal";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crime_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Criminal c = ((CrimeAdapter)getListAdapter()).getItem(position);
        if (isLogged) {
            Log.d("LOG", c.getTitle()+" was clicked");
        }
        Intent mIntent = new Intent(getActivity(), CrimePagerActivity.class);
        mIntent.putExtra(CriminalFragment.EXTRA_CRIME_ID, c.getID());
        startActivity(mIntent);
    }

    private class CrimeAdapter extends ArrayAdapter<Criminal> {
        public CrimeAdapter (ArrayList<Criminal> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }
            Criminal c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_text_view1);
            titleTextView.setText(c.getTitle());

            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_text_view2);
            String replacement;
            replacement = (String)DateFormat.format("E/d/M/y", c.getDate());
            dateTextView.setText(replacement);

            CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.crime_list_check_box);
            checkBox.setChecked(c.isSolved());

            return convertView;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Criminal crime = new Criminal();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CriminalFragment.EXTRA_CRIME_ID, crime.getID());
                startActivityForResult(i, 0);
                return true;
            case R.id.menu_item_show_subtitle:
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
