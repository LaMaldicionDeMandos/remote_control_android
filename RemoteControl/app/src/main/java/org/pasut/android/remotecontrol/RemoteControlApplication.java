package org.pasut.android.remotecontrol;

import android.app.Application;

import roboguice.RoboGuice;

/**
 * Created by marcelo on 01/07/15.
 */
public class RemoteControlApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RoboGuice.setUseAnnotationDatabases(false);
        RoboGuice.getOrCreateBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this), new RemoteControlModule());
    }
}
