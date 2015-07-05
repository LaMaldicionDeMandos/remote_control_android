package remote_control.android.pasut.org.remotecontrol;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.octo.android.robospice.SpiceManager;

import remote_control.android.pasut.org.remotecontrol.services.DefaultPreferencesService;
import remote_control.android.pasut.org.remotecontrol.services.PreferencesService;
import remote_control.android.pasut.org.remotecontrol.services.RestService;
import remote_control.android.pasut.org.remotecontrol.services.rest.RestSpiceService;

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
