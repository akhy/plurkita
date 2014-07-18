package net.akhyar.plurkita.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author akhyar
 */
public interface AuthApi {

    @GET("/request_token")
    void getRequestToken(
            Callback<String> callback
    );

    @GET("/access_token")
    void getAccessToken(
            @Query("oauth_verifier") String oAuthVerifier,
            Callback<String> callback
    );
}
