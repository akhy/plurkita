package net.akhyar.plurkita.model;

import com.activeandroid.serializer.TypeSerializer;

import org.joda.time.DateTime;

/**
 * For use with ActiveAndroid
 *
 * @author akhyar
 */
public class DateTimeTypeSerializer extends TypeSerializer {

    @Override
    public Class<DateTime> getDeserializedType() {
        return DateTime.class;
    }

    @Override
    public Class<Long> getSerializedType() {
        return Long.class;
    }

    @Override
    public Long serialize(Object data) {
        if (data == null)
            return 0l;

        return ((DateTime) data).getMillis();
    }

    @Override
    public DateTime deserialize(Object data) {
        if (data == null)
            return new DateTime(0l);

        return new DateTime(data);
    }
}
