package org.pasut.android.remotecontrol.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.pasut.android.remotecontrol.R;


public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }
}
