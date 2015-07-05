package remote_control.android.pasut.org.remotecontrol.services.rest;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

/**
 * Created by marcelo on 05/07/15.
 */
public class PingRequest extends GoogleHttpClientSpiceRequest<Void> {

    public PingRequest() {
        super(Void.class);
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl("http://localhost:8080/ping"));
        request.execute();
        return null;
    }
}

