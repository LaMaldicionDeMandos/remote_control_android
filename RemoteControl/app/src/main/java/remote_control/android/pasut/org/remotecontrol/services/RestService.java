package remote_control.android.pasut.org.remotecontrol.services;

import android.content.Context;

import com.google.inject.Inject;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by marcelo on 05/07/15.
 */
public class RestService {
    private final SpiceManager spice;

    @Inject
    public RestService(final SpiceManager spice) {
        this.spice = spice;
    }

    public void start(final Context context) {
        spice.start(context);
    }

    public void shouldStop() {
        spice.shouldStop();
    }
}
