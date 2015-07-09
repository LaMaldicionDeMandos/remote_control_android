package org.pasut.android.remotecontrol.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import static android.widget.Toast.LENGTH_LONG;
import static org.pasut.android.remotecontrol.R.string.fail_server_dialog_message;
import static org.pasut.android.remotecontrol.R.string.fail_server_dialog_title;
import static org.pasut.android.remotecontrol.R.string.fail_server_message;
import static org.pasut.android.remotecontrol.R.string.no;
import static org.pasut.android.remotecontrol.R.string.yes;

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
            Toast.makeText(SplashActivity.this, fail_server_message, LENGTH_LONG).show();
            new AlertDialog.Builder(this)
                .setTitle(fail_server_dialog_title)
                .setMessage(fail_server_dialog_message)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(SplashActivity.this, SettingsActivity.class), FAILURE_RESPONSE);
                    }
                }).show();
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
