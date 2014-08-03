package net.akhyar.plurkita.api;

import net.akhyar.plurkita.model.Plurk;
import net.akhyar.plurkita.model.Timeline;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author akhyar
 */
public interface TimelineApi {

    @GET("/Timeline/getPlurks?limit=10")
    Observable<Timeline> getPlurks();

    @GET("/Timeline/getPlurks?limit=20")
    Observable<Timeline> getPlurks(
            @Query("offset") String before
    );

    @POST("/Timeline/plurkAdd")
    @FormUrlEncoded
    Observable<Plurk> addPlurk(
            @Field("qualifier") String qualifier,
            @Field("content") String content
    );
}
