package net.akhyar.plurkita.api;

import net.akhyar.plurkita.model.Timeline;

import retrofit.Callback;
import retrofit.http.GET;
import rx.Observable;

/**
 * @author akhyar
 */
public interface TimelineApi {

    @GET("/getPlurks?limit=10")
    void getPlurks(Callback<Timeline> callback);


    @GET("/getPlurks?limit=10")
    Observable<Timeline> getPlurksObservable();
}
