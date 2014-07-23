package net.akhyar.plurkita.api.util;

import android.net.Uri;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit.client.Header;
import retrofit.client.Request;

/**
 * @author akhyar
 */
public class OAuthRequestAdapter extends OAuthRequest {

    private Request request;

    public OAuthRequestAdapter(Request request) {
        super(Verb.valueOf(request.getMethod()), request.getUrl());
        this.request = request;

        // replicate request body
        if (request.getBody() != null &&
                request.getBody().mimeType().startsWith("application/x-www-form-urlencoded")) {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                request.getBody().writeTo(out);
                String params = new String(out.toByteArray());

                // TODO find a better way to convert query params to map
                Uri dummy = Uri.parse("http://dummy?" + params);
                for (String key : dummy.getQueryParameterNames())
                    addBodyParameter(key, dummy.getQueryParameter(key));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Request getRequest() {
        return request;
    }

    @Override
    public void addHeader(String key, String value) {
        super.addHeader(key, value);
        ArrayList<Header> headers = new ArrayList<Header>(request.getHeaders());
        headers.add(new Header(key, value));
        request = new Request(request.getMethod(), request.getUrl(), headers, request.getBody());
    }

}
