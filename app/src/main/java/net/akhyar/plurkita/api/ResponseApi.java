package net.akhyar.plurkita.api;

import net.akhyar.plurkita.model.Conversation;
import net.akhyar.plurkita.model.Response;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author akhyar
 */
public interface ResponseApi {

    @GET("/Responses/get")
    Observable<Conversation> getConversation(
            @Query("plurk_id") long plurkId
    );

    @POST("/Responses/responseAdd")
    @FormUrlEncoded
    Observable<Response> postResponse(
            @Field("plurk_id") long plurkId,
            @Field("content") String content,
            @Field("qualifier") String qualifier
    );

    @POST("/Responses/responseDelete")
    @FormUrlEncoded
    Observable<Void> deleteResponse(
            @Field("plurk_id") long plurkId,
            @Field("response_id") long responseId
    );
}
