package net.akhyar.plurkita.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import net.akhyar.plurkita.PlurkUtils;

import org.joda.time.DateTime;

/**
 * @author akhyar
 */
@Table(name = "Users")
public class User extends Model {

    @Column(index = true)
    @SerializedName("id")
    long userId;
    @Column
    String nickName; // akhy
    @Column
    String displayName; // chickenger
    @Column
    String fullName; // Khayrullah A. Rama
    @Column
    int hasProfileImage;
    @Column
    int avatar = -1;
    @Column
    String location;
    DateTime dateOfBirth;
    @Column
    int bdayPrivacy;
    @Column
    int gender;
    @Column
    float karma;

    public static User find(User user) {
        return find(user.getUserId());
    }

    public static User find(long userId) {
        return new Select().from(User.class).where("userId = ?", userId).executeSingle();
    }

    public long getUserId() {
        return userId;
    }

    public static void upsert(User user) {
        User existing = find(user.getUserId());
        if (existing == null) {
            user.save();
        } else {
            existing.loadFrom(user);
            existing.save();
        }
    }

    public void loadFrom(User user) {
        this.userId = user.userId;
        this.nickName = user.nickName;
        this.displayName = user.displayName;
        this.fullName = user.fullName;
        this.hasProfileImage = user.hasProfileImage;
        this.avatar = user.avatar;
        this.dateOfBirth = user.dateOfBirth;
        this.bdayPrivacy = user.bdayPrivacy;
        this.gender = user.gender;
        this.karma = user.karma;
    }

    public String getAvatarUrl() {
        return getAvatarUrl("big");
    }

    public String getAvatarUrl(String size) {
        return PlurkUtils.getAvatarUrl(userId, hasProfileImage, avatar, size);
    }

    public String getNickName() {
        return nickName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFullName() {
        return fullName;
    }

    public int getHasProfileImage() {
        return hasProfileImage;
    }

    public int getAvatar() {
        return avatar;
    }

    public String getLocation() {
        return location;
    }

    public DateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public int getBdayPrivacy() {
        return bdayPrivacy;
    }

    public int getGender() {
        return gender;
    }

    public float getKarma() {
        return karma;
    }
}
