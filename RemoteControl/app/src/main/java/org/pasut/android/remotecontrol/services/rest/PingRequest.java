package org.pasut.android.remotecontrol.services.rest;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

/**
 * Created by marcelo on 05/07/15.
 */
public class PingRequest extends AbstractRequest<Void> {
    private final String path;

    public PingRequest(final String host, final int port) {
        super(host, port, Void.class);
        path = url + "ping/";
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(path));
        request.execute();
        return null;
    }

    @Override
    public String cacheKey() {
        return path;
    }
}

