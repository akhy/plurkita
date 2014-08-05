package net.akhyar.plurkita.api;

import net.akhyar.plurkita.model.Conversation;
import net.akhyar.plurkita.model.Response;

import retrofit.http.GET;
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

    // TODO change to a proper POST request
    @GET("/Responses/responseAdd")
    Observable<Response> postResponse(
            @Query("plurk_id") long plurkId,
            @Query("content") String content,
            @Query("qualifier") String qualifier
    );

    // TODO change to a proper POST request
    @GET("/Responses/responseDelete")
    Observable<Void> deleteResponse(
            @Query("plurk_id") long plurkId,
            @Query("response_id") long responseId
    );
}
