package net.akhyar.plurkita.api.util;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.mime.MimeUtil;

/**
 * @author akhyar
 */
public class OAuthRequestAdapter extends OAuthRequest {

    private Request mRequest;

    public OAuthRequestAdapter(Request request) {
        super(Verb.valueOf(request.getMethod().toUpperCase()), request.getUrl());
        mRequest = request;

        // replicate header
        for (Header header : request.getHeaders())
            addHeader(header.getName(), header.getValue());

        // replicate request body
        if (request.getBody() != null) {
            setCharset(MimeUtil.parseCharset(request.getBody().mimeType()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                request.getBody().writeTo(out);
                addPayload(out.toByteArray());
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

    @Override
    public void addHeader(String key, String value) {
        super.addHeader(key, value);
        ArrayList<Header> headers = new ArrayList<Header>(mRequest.getHeaders());
        headers.add(new Header(key, value));
        mRequest = new Request(mRequest.getMethod(), mRequest.getUrl(), headers, mRequest.getBody());
    }

    public Request getRequest() {
        return mRequest;
    }

}
