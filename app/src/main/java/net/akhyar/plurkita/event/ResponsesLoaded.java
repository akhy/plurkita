package net.akhyar.plurkita.event;

import net.akhyar.plurkita.model.Response;

import java.util.List;

/**
 * @author akhyar
 */
public class ResponsesLoaded {

    private long plurkId;
    private List<Response> responses;

    public ResponsesLoaded(long plurkId, List<Response> responses) {
        this.plurkId = plurkId;
        this.responses = responses;
    }

    public long getPlurkId() {
        return plurkId;
    }

    public List<Response> getResponses() {
        return responses;
    }
}
