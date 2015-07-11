package org.pasut.android.remotecontrol.services.rest;

import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

/**
 * Created by marcelo on 06/07/15.
 */
public abstract class AbstractRequest<T> extends GoogleHttpClientSpiceRequest<T>{
    protected final String url;

    public AbstractRequest(final String host, final int port, Class<T> clazz) {
        super(clazz);
        this.url = "http://" + host + ":" + port + "/";
    }

    public abstract String cacheKey();
}
