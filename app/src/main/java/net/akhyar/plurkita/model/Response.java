package net.akhyar.plurkita.model;

import org.joda.time.DateTime;

/**
 * @author akhyar
 */
public class Response {

    long id;
    long plurkId;
    long userId;
    String qualifier;
    String content;
    String contentRaw;
    String lang;
    DateTime posted;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getPlurkId() {
        return plurkId;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getContent() {
        return content;
    }

    public String getContentRaw() {
        return contentRaw;
    }

    public String getLang() {
        return lang;
    }

    public DateTime getPosted() {
        return posted;
    }
}
