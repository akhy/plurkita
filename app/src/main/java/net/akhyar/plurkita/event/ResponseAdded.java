package net.akhyar.plurkita.event;

import net.akhyar.plurkita.model.Response;

/**
 * @author akhyar
 */
public class ResponseAdded {

    private long plurkId;
    private Response response;

    public ResponseAdded(long plurkId, Response response) {
        this.plurkId = plurkId;
        this.response = response;
    }

    public long getPlurkId() {
        return plurkId;
    }

    public Response getResponse() {
        return response;
    }
}
