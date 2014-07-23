package net.akhyar.plurkita.model;

import com.activeandroid.serializer.TypeSerializer;
import com.google.gson.Gson;

/**
 * @author akhyar
 */
public class ArrayOfLongTypeSerializer extends TypeSerializer {
    @Override
    public Class<long[]> getDeserializedType() {
        return long[].class;
    }

    @Override
    public Class<String> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data) {
        if (data == null)
            return "[]";

        return new Gson().toJson(data);
    }

    @Override
    public long[] deserialize(Object data) {
        if (data == null)
            return new long[]{};

        return new Gson().fromJson((String) data, long[].class);
    }
}
