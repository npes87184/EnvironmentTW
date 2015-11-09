package com.npes87184.enviromenttw;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by npes87184 on 2015/11/9.
 */
public class SettingPreference extends PreferenceFragment {

    private static SettingPreference fragment;

    public static SettingPreference newInstance() {
        if(fragment==null) {
            fragment = new SettingPreference();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
