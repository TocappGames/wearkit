package com.tocapp.utils;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefsUtil extends Activity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;
    private String MAP_NUM_ = "MAP_NUM_";

    public SharedPrefsUtil(Application application) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences( application );
    }

    public void setViewedMap(String mapId) {
        sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putBoolean( MAP_NUM_ + mapId, true );
        sharedPrefEditor.commit();
    }

    public boolean getViewedMap(String mapId) {
        return sharedPref.getBoolean( MAP_NUM_ + mapId, false );
    }


}
