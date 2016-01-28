package org.pasut.android.remotecontrol.services.rest;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Lists;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.api.client.util.Lists.newArrayList;

/**
 * Created by marcelo on 09/07/15.
 */
public class LedsRequest extends AbstractRequest<List<BigDecimal>> {
    private final static Class<List<BigDecimal>> getClazz() {
        List<Integer> list = newArrayList();
        return (Class<List<BigDecimal>>)list.getClass();
    }

    private final String path;

    public LedsRequest(String host, int port) {
        super(host, port, getClazz());
        path = url + "leds/";
    }

    @Override
    public List<BigDecimal> loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(path));
        request.setParser(new GsonFactory().createJsonObjectParser());
        HttpResponse response = request.execute();
        List<BigDecimal> result = response.parseAs(getClazz());
        return result;
    }

    @Override
    public String cacheKey() {
        return path;
    }
}
