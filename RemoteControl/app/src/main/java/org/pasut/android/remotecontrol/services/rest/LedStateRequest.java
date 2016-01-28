package org.pasut.android.remotecontrol.services.rest;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import java.util.UUID;

/**
 * Created by marcelo on 11/07/15.
 */
public class LedStateRequest extends AbstractRequest<Boolean> {
    private final static String TAG = LedStateRequest.class.getSimpleName();
    private final int ledId;
    private final String path;
    public LedStateRequest(final String host, final int port, final int ledId) {
        super(host, port, Boolean.class);
        this.ledId = ledId;
        this.path = url + "status/" + ledId + "/";
    }

    @Override
    public Boolean loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(path));
        HttpResponse response = request.execute();
        String state = response.parseAsString();
        Log.d(TAG, "Led " + ledId + " state: " + state);
        return "on".equals(state);
    }

    @Override
    public String cacheKey() {
        return path + UUID.randomUUID();
    }
}
