package com.makasart.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maxim on 19.09.2016.
 */
public class CrimeListFragment extends ListFragment {
    private ArrayList<Criminal> mCrimes;
    private boolean isLogged = true;
    private final static String TAG = "Criminal";
    private boolean mIsShowTitle;
    private boolean mFeature = false;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeSelected(Criminal crime);
        void onCrimeUpdater(Criminal crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void updateUI() {
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crime_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();
        setRetainInstance(true);
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
        mIsShowTitle = false;
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
     //   Intent mIntent = new Intent(getActivity(), CrimePagerActivity.class);
     //   mIntent.putExtra(CriminalFragment.EXTRA_CRIME_ID, c.getID());
     //   startActivity(mIntent);
        mCallbacks.onCrimeSelected(c);
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
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (showSubtitle != null && mIsShowTitle) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Criminal crime = new Criminal();
                CrimeLab.get(getActivity()).addCrime(crime);
             //   Intent i = new Intent(getActivity(), CrimePagerActivity.class);
             //   i.putExtra(CriminalFragment.EXTRA_CRIME_ID, crime.getID());
             //   startActivityForResult(i, 0);
                ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();;
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle() == null) {
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    mIsShowTitle = true;
                } else {
                    getActivity().getActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    mIsShowTitle = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
        Criminal crime = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (mIsShowTitle) {
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB && mFeature) {
            registerForContextMenu(listView);
        } else if (mFeature){
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
                            CrimeLab crimeLab = CrimeLab.get(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                crimeLab.deleteCrime(adapter.getItem(i));
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        else {
            registerForContextMenu(listView);
        }
        return v;
    }
}
