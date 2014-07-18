package net.akhyar.plurkita;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import net.akhyar.plurkita.api.AuthApi;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class LoginActivity extends BaseActivity {

    private static final int REQ_VERIFIER = 1001;

    @Inject
    Timber.Tree log;
    @Inject
    AppPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Application.inject(this);
        ButterKnife.inject(this);
    }


    @OnClick(R.id.login)
    public void requestToken() {
        preferences.clearOAuthToken();
        Application.getInstance(AuthApi.class).getRequestToken(new Callback<String>() {
            @Override
            public void success(String tokenParams, Response response) {
                log.d("Success: %s", tokenParams);
                Uri uri = Uri.parse("http://dummy?" + tokenParams);
                preferences.setOAuthToken(
                        uri.getQueryParameter("oauth_token"),
                        uri.getQueryParameter("oauth_token_secret"));
                requestVerifier(tokenParams);
            }

            @Override
            public void failure(RetrofitError error) {
                preferences.clearOAuthToken();
                log.e(error, "Error");
            }
        });
    }

    private void requestVerifier(String queryParams) {
        Intent verifierIntent = AuthorizeActivity.createIntent(this, queryParams);
        startActivityForResult(verifierIntent, REQ_VERIFIER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_VERIFIER) {
            String verifier = data.getStringExtra(AuthorizeActivity.EXTRA_OAUTH_VERIFIER);
            log.d("Verifier found: %s", verifier);
            requestAccessToken(verifier);
        } else {
            preferences.clearOAuthToken();
            log.w("Verifier request failed");
        }
    }

    private void requestAccessToken(String verifier) {
        Application.getInstance(AuthApi.class).getAccessToken(
                verifier,
                new Callback<String>() {
                    @Override
                    public void success(String accessTokenParams, Response response) {
                        log.d("Permanent access token: %s", accessTokenParams);
                        Uri uri = Uri.parse("http://dummy?" + accessTokenParams);
                        preferences.setOAuthToken(
                                uri.getQueryParameter("oauth_token"),
                                uri.getQueryParameter("oauth_token_secret"));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        log.d(retrofitError, "Error getting access token");
                        preferences.clearOAuthToken();
                    }
                }
        );
    }
}
