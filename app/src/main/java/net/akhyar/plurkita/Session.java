package net.akhyar.plurkita;

import android.content.Context;
import android.content.SharedPreferences;

import net.akhyar.plurkita.model.User;

import javax.inject.Singleton;

/**
 * @author akhyar
 */
@Singleton
public class Session {

    private final static String PREFERENCE_SUFFIX = "_session";
    private final static String KEY_TOKEN = "oauth_token";
    private final static String KEY_TOKEN_SECRET = "oauth_token_secret";
    private final static String KEY_USER_ID = "user_id";
    private final SharedPreferences preferences;

    public Session(Context context) {
        preferences = context.getSharedPreferences(
                context.getPackageName() + PREFERENCE_SUFFIX,
                Context.MODE_PRIVATE);
    }

    public User getUser() {
        return isLoggedIn() ? User.find(getUserId()) : null;
    }

    public boolean isLoggedIn() {
        return getUserId() >= 0;
    }

    public long getUserId() {
        return preferences.getLong(KEY_USER_ID, -1);
    }

    public void login(User user) {
        login(user.getUserId());
    }

    public void login(long userId) {
        preferences.edit()
                .putLong(KEY_USER_ID, userId)
                .commit();
    }

    public void logout() {
        preferences.edit().clear().commit();
    }

    public void setToken(String token, String tokenSecret) {
        preferences.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_TOKEN_SECRET, tokenSecret)
                .commit();
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public String getTokenSecret() {
        return preferences.getString(KEY_TOKEN_SECRET, null);
    }

    public void clearToken() {
        preferences.edit()
                .remove(KEY_TOKEN)
                .remove(KEY_TOKEN_SECRET)
                .commit();
    }
}
