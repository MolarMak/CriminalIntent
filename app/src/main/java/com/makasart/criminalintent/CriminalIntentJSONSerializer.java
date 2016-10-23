package com.makasart.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Maxim on 23.10.2016.
 */

public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFileName;

    public CriminalIntentJSONSerializer (Context c, String f) {
        mContext = c;
        mFileName = f;
    }

    public void saveCrimes(ArrayList<Criminal> crimes) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Criminal c : crimes) {
            array.put(c.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                    writer.close();
            }
        }
    }

    public ArrayList<Criminal> loadCrimes() throws IOException, JSONException {
        ArrayList<Criminal> crimes = new ArrayList<Criminal>();
        BufferedReader reader = null;
        try {
            InputStream input = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Criminal(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return crimes;
    }
}
