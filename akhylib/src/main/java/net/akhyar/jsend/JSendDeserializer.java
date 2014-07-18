package net.akhyar.jsend;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author akhyar
 */
public class JSendDeserializer<T> implements JsonDeserializer<T> {

    private Gson dataGson;

    public JSendDeserializer() {
        dataGson = new Gson();
    }

    public JSendDeserializer(Gson dataGson) {
        this.dataGson = dataGson;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject object = jsonElement.getAsJsonObject();

        if (!object.has("status"))
            throw new JSend.JSendParseException("Malformed JSend data: \"status\" field not found");

        String message = object.has("message") ? object.get("message").getAsString() : "";
        String status = object.get("status").getAsString();
        if (JSend.STATUS_ERROR.equalsIgnoreCase(status)) {
            throw new JSend.JSendErrorException(message);
        } else if (JSend.STATUS_FAIL.equalsIgnoreCase(status)) {
            throw new JSend.JSendFailException(message);
        }

        return dataGson.fromJson(object.get("data"), type);
    }
}
