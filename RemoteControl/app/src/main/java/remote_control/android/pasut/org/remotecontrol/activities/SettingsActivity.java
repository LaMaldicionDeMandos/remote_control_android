package remote_control.android.pasut.org.remotecontrol.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import remote_control.android.pasut.org.remotecontrol.R;
import remote_control.android.pasut.org.remotecontrol.fragments.SettingsFragment;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
    }

}
