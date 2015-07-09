package org.pasut.android.remotecontrol.services.rest;

import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import org.pasut.android.remotecontrol.model.Led;

/**
 * Created by marcelo on 09/07/15.
 */
public class ChangeStateRequest extends AbstractRequest<Boolean> {
    private final Led led;
    private final String state;
    public ChangeStateRequest(final String host, final int port, final Led led, final boolean state) {
        super(host, port, Boolean.class);
        this.led = led;
        this.state = state ? "on" : "off";
    }

    @Override
    public Boolean loadDataFromNetwork() throws Exception {
        HttpContent content = new EmptyContent();
        HttpRequest request = getHttpRequestFactory()
                .buildPutRequest(new GenericUrl(url + "led/" + led.getId() + "/" + state), content);
        HttpResponse response = request.execute();
        return response.getStatusCode() == 200;
    }
}
