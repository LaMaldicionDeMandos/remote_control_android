package org.pasut.android.remotecontrol.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.pasut.android.remotecontrol.R;
import org.pasut.android.remotecontrol.fragments.SettingsFragment;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
    }

}
