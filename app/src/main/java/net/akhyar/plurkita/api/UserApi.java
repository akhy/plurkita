package net.akhyar.plurkita.api;

import net.akhyar.plurkita.model.User;

import retrofit.http.GET;
import rx.Observable;

/**
 * @author akhyar
 */
public interface UserApi {

    @GET("/Users/me")
    Observable<User> getMySelf();
}
