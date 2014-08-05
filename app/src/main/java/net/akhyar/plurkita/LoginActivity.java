package net.akhyar.plurkita;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.dd.processbutton.iml.ActionProcessButton;

import net.akhyar.plurkita.api.AuthApi;
import net.akhyar.plurkita.api.UserApi;
import net.akhyar.plurkita.model.User;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

public class LoginActivity extends BaseActivity {

    private static final int REQ_VERIFIER = 1001;

    @Inject
    Timber.Tree log;
    @Inject
    Session session;
    @InjectView(R.id.login)
    ActionProcessButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Application.inject(this);
        ButterKnife.inject(this);
        login.setMode(ActionProcessButton.Mode.ENDLESS);
        login.setProgress(0);
    }


    @OnClick(R.id.login)
    public void requestToken() {
        session.clearToken();
        login.setEnabled(false);
        login.setProgress(50);
        login.setLoadingText("Requesting Token");
        Application.getInstance(AuthApi.class).getRequestToken(new Callback<String>() {
            @Override
            public void success(String tokenParams, Response response) {
                log.d("Success: %s", tokenParams);
                Uri uri = Uri.parse("http://dummy?" + tokenParams);
                session.setToken(
                        uri.getQueryParameter("oauth_token"),
                        uri.getQueryParameter("oauth_token_secret"));
                requestVerifier(tokenParams);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                log.e(retrofitError, "Error requesting token");
                onError(retrofitError);
            }
        });
    }

    private void requestVerifier(String queryParams) {
        Intent verifierIntent = AuthorizeActivity.createIntent(this, queryParams);
        startActivityForResult(verifierIntent, REQ_VERIFIER);
    }

    private void onError(Throwable throwable) {
        session.clearToken();
        login.setProgress(-1);
        login.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_VERIFIER) {
            if (resultCode == RESULT_OK) {
                String verifier = data.getStringExtra(AuthorizeActivity.EXTRA_OAUTH_VERIFIER);
                log.d("Verifier found: %s", verifier);
                requestAccessToken(verifier);
            } else {
                onError(new Exception("Failed to authorize"));
            }
        }
    }

    private void requestAccessToken(String verifier) {
        login.setLoadingText("Verifying Access");
        Application.getInstance(AuthApi.class).getAccessToken(verifier)
                .flatMap(new Func1<String, Observable<User>>() {
                    @Override
                    public Observable<User> call(String accessTokenParams) {
                        log.d("Permanent access token: %s", accessTokenParams);
                        Uri uri = Uri.parse("http://dummy?" + accessTokenParams);
                        session.setToken(
                                uri.getQueryParameter("oauth_token"),
                                uri.getQueryParameter("oauth_token_secret"));

                        return Application.getInstance(UserApi.class).getMySelf();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                               @Override
                               public void call(User user) {
                                   log.i("Logged in as @%s", user.getNickName());
                                   session.login(user);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   onError(throwable);
                               }
                           }, new Action0() {
                               @Override
                               public void call() {
                                   login.setProgress(100);
                                   login.setEnabled(true);

                                   startActivity(new Intent(LoginActivity.this, TimelineActivity.class));
                               }
                           }
                );
    }
}
