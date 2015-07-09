package org.pasut.android.remotecontrol.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.api.client.http.HttpRequest;
import com.google.inject.Inject;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.pasut.android.remotecontrol.model.Led;
import org.pasut.android.remotecontrol.services.rest.ChangeStateRequest;
import org.pasut.android.remotecontrol.services.rest.LedsRequest;
import org.pasut.android.remotecontrol.services.rest.PingRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectPreference;

import static com.octo.android.robospice.persistence.DurationInMillis.ONE_MINUTE;

/**
 * Created by marcelo on 05/07/15.
 */
@ContextSingleton
public class RestService {
    private final SpiceManager spice;
    private final SharedPreferences preferences;

    @Inject
    public RestService(final SpiceManager spice, final SharedPreferences preferences) {
        this.spice = spice;
        this.preferences = preferences;
    }

    public void ping(RequestListener<Void> listener) {
        executeRequest(new PingRequest(getHost(), getPort()), listener);
    }

    public void leds(RequestListener<List<BigDecimal>> listener) {
        executeRequest(new LedsRequest(getHost(), getPort()), listener);
    }

    public void changeStatus(final Led led, final boolean state, RequestListener<Boolean> listener) {
        SpiceRequest<Boolean> request = new ChangeStateRequest(getHost(), getPort(), led, state);
        executeRequest(request, listener);
    }

    private <T> void executeRequest(SpiceRequest<T> request, RequestListener<T> listener) {
        spice.execute(request, "json", ONE_MINUTE, listener);
    }

    private String getHost() {
        return preferences.getString("settings_ip", "localhost");
    }

    private int getPort() {
        return Integer.valueOf(preferences.getString("settings_port", "80"));
    }

    public void start(final Context context) {
        spice.start(context);
    }

    public void shouldStop() {
        spice.shouldStop();
    }
}
