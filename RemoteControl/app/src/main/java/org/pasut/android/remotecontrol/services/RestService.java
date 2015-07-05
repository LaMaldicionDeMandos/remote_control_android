package org.pasut.android.remotecontrol.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;
import com.octo.android.robospice.SpiceManager;

import roboguice.inject.InjectPreference;

/**
 * Created by marcelo on 05/07/15.
 */
public class RestService {
    private final SpiceManager spice;
    private final String host;
    private final int port;

    @Inject
    public RestService(final SpiceManager spice, final SharedPreferences preferences) {
        this.spice = spice;
        this.host = preferences.getString("settings_ip", "jojo");
        this.port = preferences.getInt("settings_port", 1);
    }

    public void start(final Context context) {
        spice.start(context);
    }

    public void shouldStop() {
        spice.shouldStop();
    }
}
