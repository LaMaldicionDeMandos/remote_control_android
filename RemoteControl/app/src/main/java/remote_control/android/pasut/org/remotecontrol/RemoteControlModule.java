package remote_control.android.pasut.org.remotecontrol;

import com.google.inject.Binder;
import com.google.inject.Module;

import remote_control.android.pasut.org.remotecontrol.services.DefaultPreferencesService;
import remote_control.android.pasut.org.remotecontrol.services.PreferencesService;

/**
 * Created by marcelo on 01/07/15.
 */
public class RemoteControlModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(PreferencesService.class).to(DefaultPreferencesService.class);
    }
}
