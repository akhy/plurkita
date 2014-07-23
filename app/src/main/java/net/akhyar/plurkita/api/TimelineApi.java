package net.akhyar.plurkita.api;

import net.akhyar.plurkita.model.Timeline;

import retrofit.http.GET;
import rx.Observable;

/**
 * @author akhyar
 */
public interface TimelineApi {

    @GET("/Timeline/getPlurks?limit=10")
    Observable<Timeline> getPlurks();

}
