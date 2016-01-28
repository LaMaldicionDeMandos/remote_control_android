package org.pasut.android.remotecontrol.services.rest;

import android.util.Log;

import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

import org.pasut.android.remotecontrol.model.Led;

import java.util.UUID;

/**
 * Created by marcelo on 09/07/15.
 */
public class ChangeStateRequest extends AbstractRequest<Boolean> {
    private final static String TAG = ChangeStateRequest.class.getSimpleName();
    private final Led led;
    private final String state;
    private final String path;
    public ChangeStateRequest(final String host, final int port, final Led led, final boolean state) {
        super(host, port, Boolean.class);
        this.led = led;
        this.state = state ? "on" : "off";
        this.path = url + "led/" + led.getId() + "/" + this.state + "/";
    }

    @Override
    public Boolean loadDataFromNetwork() throws Exception {
        HttpContent content = new EmptyContent();
        HttpRequest request = getHttpRequestFactory()
                .buildPutRequest(new GenericUrl(path), content);
        try {
            HttpResponse response = request.execute();
            int code = response.getStatusCode();
            Log.d(TAG, "Change state response code: " + code);
            return code == 200;
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 304) {
                Log.d(TAG, "Change state response code: " + e.getStatusCode());
                return false;
            } else {
                throw e;
            }
        }
    }

    @Override
    public String cacheKey() {
        return path + UUID.randomUUID();
    }
}
