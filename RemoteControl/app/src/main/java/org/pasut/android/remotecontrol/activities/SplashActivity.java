package org.pasut.android.remotecontrol.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.pasut.android.remotecontrol.R;
import org.pasut.android.remotecontrol.services.PreferencesService;
import org.pasut.android.remotecontrol.services.RestService;
import org.pasut.android.remotecontrol.utils.ActivityUtils;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import static android.widget.Toast.LENGTH_LONG;
import static org.pasut.android.remotecontrol.R.string.fail_server_message;
import static org.pasut.android.remotecontrol.utils.ActivityUtils.configurarionErrorMessage;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends RoboActivity {
    private final static String FRE = "FIRST_RUNNING_EXPERIENCE";
    private final static int RESPONSE = 1;
    private final static int FAILURE_RESPONSE = 2;

    @Inject
    private RestService restService;

    @Inject
    private PreferencesService preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView loadingImage = (ImageView) findViewById(R.id.loading);

        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImage.getBackground();
        loadingAnimation.start();

        if (!preferences.contain(FRE)) {
            startActivityForResult(new Intent(this, SettingsActivity.class), RESPONSE);
            preferences.put(FRE, true);
        } else {
            restService.ping(pingListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESPONSE) {
            restService.ping(pingListener);
        } else {
            configurarionErrorMessage(this, FAILURE_RESPONSE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        restService.start(this);
    }

    @Override
    protected void onStop() {
        restService.shouldStop();
        super.onStop();
    }

    private final RequestListener<Void> pingListener = new RequestListener<Void>() {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SplashActivity.this, fail_server_message, LENGTH_LONG).show();
            startActivityForResult(new Intent(SplashActivity.this, SettingsActivity.class), FAILURE_RESPONSE);
        }

        @Override
        public void onRequestSuccess(Void aVoid) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };
}
