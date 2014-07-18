package net.akhyar.plurkita.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author akhyar
 */
public class DateTimeTypeConverter
        implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    @Override
    public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {

        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("E, dd MMM yyyy HH:mm:ss ZZZZ");
            return fmt.parseDateTime(json.getAsString());
        } catch (IllegalArgumentException e) {
            // May be it came in formatted as a java.util.Date, so try that
            Date date = context.deserialize(json, Date.class);
            return new DateTime(date);
        }
    }
}