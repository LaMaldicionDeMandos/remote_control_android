package org.pasut.android.remotecontrol.services.rest;

import com.google.inject.Provider;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by marcelo on 09/07/15.
 */
public class RoboSpiceProvider implements Provider<SpiceManager> {
    @Override
    public SpiceManager get() {
        return new SpiceManager(RestSpiceService.class);
    }
}
