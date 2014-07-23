package net.akhyar.plurkita.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author akhyar
 */
@Table(name = Plurk.TABLE_NAME)
public class Plurk extends Model {

    public static final DateTimeFormatter OFFSET_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final String ID = "_id";
    public static final String TABLE_NAME = "Plurks";

    @Column(name = ID, index = true)
    long plurkId;
    @Column
    long replurkerId;
    @Column
    long userId;
    @Column
    long ownerId;
    @Column
    String qualifier;
    @Column
    String qualifierTranslated;
    @Column
    int isUnread;
    @Column
    int plurkType;
    @Column
    DateTime posted;
    @Column
    int noComments;
    @Column
    String content;
    @Column
    String contentRaw;
    @Column
    int responseCount;
    @Column
    int responsesSeen;
    @Column
    long[] limitedTo;
    @Column
    boolean favorite;
    @Column
    int favoriteCount;
    @Column
    long[] favorers;
    @Column
    boolean replurkable;
    @Column
    boolean replurked;
    @Column
    int replurkersCount;
    @Column
    long[] replurkers;

    public static void upsert(Plurk plurk) {
        Plurk existing = find(plurk.getPlurkId());
        if (existing == null) {
            plurk.save();
        } else {
            existing.loadFrom(plurk);
            existing.save();
        }
    }

    public static Plurk find(long plurkId) {
        return new Select()
                .from(Plurk.class)
                .where(ID + " = ?", plurkId)
                .executeSingle();
    }

    public long getPlurkId() {
        return plurkId;
    }

    public void loadFrom(Plurk plurk) {
        this.plurkId = plurk.plurkId;
        this.replurkerId = plurk.replurkerId;
        this.userId = plurk.userId;
        this.ownerId = plurk.ownerId;
        this.qualifier = plurk.qualifier;
        this.qualifierTranslated = plurk.qualifierTranslated;
        this.isUnread = plurk.isUnread;
        this.plurkType = plurk.plurkType;
        this.posted = plurk.posted;
        this.noComments = plurk.noComments;
        this.content = plurk.content;
        this.contentRaw = plurk.contentRaw;
        this.responseCount = plurk.responseCount;
        this.responsesSeen = plurk.responsesSeen;
        this.limitedTo = plurk.limitedTo;
        this.favorite = plurk.favorite;
        this.favoriteCount = plurk.favoriteCount;
        this.favorers = plurk.favorers;
        this.replurkable = plurk.replurkable;
        this.replurked = plurk.replurked;
        this.replurkersCount = plurk.replurkersCount;
        this.replurkers = plurk.replurkers;
    }

    public String getTimestampForOffset() {
        return OFFSET_FORMAT.print(posted);
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

    public long[] getLimitedTo() {
        return limitedTo;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public long[] getFavorers() {
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

    public long[] getReplurkers() {
        return replurkers;
    }
}
