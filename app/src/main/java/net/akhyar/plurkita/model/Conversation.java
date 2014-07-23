package net.akhyar.plurkita.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author akhyar
 */
public class Conversation {

    @SerializedName("friends")
    private Map<String, User> users;

    @SerializedName("responses")
    private List<Response> responses;

    @SerializedName("responses_seen")
    private int seenResponsesCount;

    @SerializedName("response_count")
    private int allResponsesCount;

    public int getAllResponsesCount() {
        return allResponsesCount;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public int getSeenResponsesCount() {
        return seenResponsesCount;
    }
}
