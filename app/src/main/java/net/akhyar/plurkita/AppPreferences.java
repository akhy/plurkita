package net.akhyar.plurkita;

import android.content.SharedPreferences;

/**
 * @author akhyar
 */
public class AppPreferences {

    private static final String KEY_TOKEN = "oauth_token";
    private static final String KEY_TOKEN_SECRET = "oauth_token_secret";
    private SharedPreferences preferences;

    public AppPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setOAuthToken(String oAuthToken, String oAuthTokenSecret) {
        preferences.edit()
                .putString(KEY_TOKEN, oAuthToken)
                .putString(KEY_TOKEN_SECRET, oAuthTokenSecret)
                .commit();
    }

    public String getOAuthToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public void clearOAuthToken() {
        preferences.edit()
                .remove(KEY_TOKEN)
                .remove(KEY_TOKEN_SECRET)
                .commit();
    }

    public String getOAuthTokenSecret() {
        return preferences.getString(KEY_TOKEN_SECRET, null);
    }
}
