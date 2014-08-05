package net.akhyar.plurkita.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author akhyar
 */
public interface AuthApi {

    @GET("/request_token")
    void getRequestToken(
            Callback<String> callback
    );

    @GET("/access_token")
    Observable<String> getAccessToken(
            @Query("oauth_verifier") String oAuthVerifier
    );
}
