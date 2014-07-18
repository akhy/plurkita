package net.akhyar.plurkita.model;

import org.joda.time.DateTime;

/**
 * @author akhyar
 */
public class Plurk {

    long plurkId;
    long replurkerId;
    long userId;
    long ownerId;
    String qualifier;
    String qualifierTranslated;
    int isUnread;
    int plurkType;
    DateTime posted;
    int noComments;
    String content;
    String contentRaw;
    int responseCount;
    int responsesSeen;
    int[] limitedTo;
    boolean favorite;
    int favoriteCount;
    int[] favorers;
    boolean replurkable;
    boolean replurked;
    int replurkersCount;
    int[] replurkers;

    private User _user;

    public User get_user() {
        return _user;
    }

    public void set_user(User _user) {
        this._user = _user;
    }

    public long getPlurkId() {
        return plurkId;
    }

    public long getReplurkerId() {
        return replurkerId;
    }

    public long getUserId() {
        return userId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getQualifierTranslated() {
        return qualifierTranslated;
    }

    public int isUnread() {
        return isUnread;
    }

    public int getPlurkType() {
        return plurkType;
    }

    public DateTime getPosted() {
        return posted;
    }

    public int getNoComments() {
        return noComments;
    }

    public String getContent() {
        return content;
    }

    public String getContentRaw() {
        return contentRaw;
    }

    public int getResponseCount() {
        return responseCount;
    }

    public int getResponsesSeen() {
        return responsesSeen;
    }

    public int[] getLimitedTo() {
        return limitedTo;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int[] getFavorers() {
        return favorers;
    }

    public boolean isReplurkable() {
        return replurkable;
    }

    public boolean isReplurked() {
        return replurked;
    }

    public int getReplurkersCount() {
        return replurkersCount;
    }

    public int[] getReplurkers() {
        return replurkers;
    }
}
