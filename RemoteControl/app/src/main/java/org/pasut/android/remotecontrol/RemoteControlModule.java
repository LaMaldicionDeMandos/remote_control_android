package org.pasut.android.remotecontrol;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.octo.android.robospice.SpiceManager;

import org.pasut.android.remotecontrol.services.DefaultPreferencesService;
import org.pasut.android.remotecontrol.services.PreferencesService;
import org.pasut.android.remotecontrol.services.RestService;
import org.pasut.android.remotecontrol.services.rest.RestSpiceService;

import roboguice.inject.SharedPreferencesName;

/**
 * Created by marcelo on 01/07/15.
 */
public class RemoteControlModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(PreferencesService.class).to(DefaultPreferencesService.class);
        binder.bind(SpiceManager.class).toInstance(new SpiceManager(RestSpiceService.class));
        binder.bind(RestService.class);
    }
}
