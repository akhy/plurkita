/*
 * Copyright (C) 2013 Patrik �kerfeldt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.akhyar.plurkita.api.util;

import android.net.Uri;
import android.text.TextUtils;

import com.squareup.okhttp.OkHttpClient;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import java.io.IOException;

import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

/**
 * This is a helper class, a {@code retrofit.client.UrlConnectionClient} to use
 * when building your {@code retrofit.RestAdapter}.
 */
public class SignedHeaderClient extends OkClient {

    private final OAuthService service;
    private final Token accessToken;


    public SignedHeaderClient(OAuthService service, Token accessToken) {
        super(new OkHttpClient());
        this.service = service;
        this.accessToken = accessToken;
    }

    public SignedHeaderClient(OkHttpClient client, OAuthService service, Token accessToken) {
        super(client);
        this.service = service;
        this.accessToken = accessToken;
    }

    @Override
    public Response execute(Request request) throws IOException {
        String verifier = getVerifierFromUrl(request.getUrl());
        if (!TextUtils.isEmpty(verifier)) {
            // TODO find better method to remove certain query parameters
            if (!request.getMethod().equalsIgnoreCase("GET"))
                request = new Request(
                        request.getMethod(),
                        removeParam(request.getUrl(), "oauth_verifier"),
                        request.getHeaders(),
                        request.getBody());
        }

        OAuthRequestAdapter tmp = new OAuthRequestAdapter(request);
        if (!TextUtils.isEmpty(verifier)) {
            tmp.addOAuthParameter("oauth_verifier", verifier);
        }

        service.signRequest(accessToken, tmp);
        return super.execute(tmp.getRequest());
    }

    private String getVerifierFromUrl(String url) {
        return Uri.parse(url).getQueryParameter("oauth_verifier");
    }

    private String removeParam(String url, String param) {
        Uri old = Uri.parse(url);
        Uri.Builder b = new Uri.Builder();
        b.scheme(old.getScheme());
        b.authority(old.getAuthority());
        b.path(old.getPath());
        for (String key : old.getQueryParameterNames()) {
            if (!key.equalsIgnoreCase(param))
                b.appendQueryParameter(key, old.getQueryParameter(key));
        }
        return b.build().toString();
    }

}
