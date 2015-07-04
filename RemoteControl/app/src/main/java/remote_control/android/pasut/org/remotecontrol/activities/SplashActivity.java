package remote_control.android.pasut.org.remotecontrol.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.inject.Inject;

import remote_control.android.pasut.org.remotecontrol.R;
import remote_control.android.pasut.org.remotecontrol.services.PreferencesService;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends RoboActivity {
    private final static String FRE = "FIRST_RUNNING_EXPERIENCE";
    private final static int RESPONSE = 1;

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
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
