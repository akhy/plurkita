package net.akhyar.plurkita.model;

/**
 * @author akhyar
 */
public class Profile {

    int friendsCount;
    int fansCount;
    User userInfo;
    boolean areFriends;
    boolean isFan;
    boolean isFollowing;
    boolean hasReadPermission;
    String privacy;

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public boolean isAreFriends() {
        return areFriends;
    }

    public boolean isFan() {
        return isFan;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public boolean isHasReadPermission() {
        return hasReadPermission;
    }

    public String getPrivacy() {
        return privacy;
    }
}
