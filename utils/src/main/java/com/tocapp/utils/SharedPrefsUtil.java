package com.tocapp.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefsUtil extends Activity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;

    public SharedPrefsUtil(Application application) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(application);
        sharedPrefEditor = sharedPref.edit();
    }
    public boolean getSharedPref(String mapid) {
        return sharedPref.getBoolean(mapid, false);
    }
    public void setSharedPref(String mapid) {
        sharedPrefEditor.putBoolean(mapid, true);
        sharedPrefEditor.commit();
    }
}
