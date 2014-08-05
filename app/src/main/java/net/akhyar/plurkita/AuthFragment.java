package net.akhyar.plurkita;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

public class AuthFragment extends Fragment {

    @Inject
    Timber.Tree log;
    @Inject
    Session session;
    @InjectView(R.id.button)
    ActionProcessButton button;
    @InjectView(R.id.notice)
    TextView notice;

    private Listener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener)
            listener = (Listener) activity;
        else
            throw new ClassCastException(activity + " must implements " + Listener.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_auth, null);
        Application.inject(this);
        ButterKnife.inject(this, view);
        button.setMode(ActionProcessButton.Mode.ENDLESS);
        button.setProgress(0);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateSessionState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    void invalidateSessionState() {
        button.setProgress(0);
        button.setEnabled(true);

        if (session.isLoggedIn()) {
            button.setText("Log me out!"); // TODO move to xml resource
            notice.setVisibility(View.INVISIBLE);
        } else {
            button.setText("Log me in!"); // TODO move to xml resource
            notice.setVisibility(View.VISIBLE);
        }

        listener.onSessionInvalidated();
    }

    @OnClick(R.id.button)
    public void buttonClick() {
        if (session.isLoggedIn()) {
            session.logout();
            invalidateSessionState();
        } else {
            session.clearToken();
            button.setEnabled(false);
            button.setProgress(50);
            button.setLoadingText("Requesting Token");
            Application.getInstance(AuthApi.class).getRequestToken(new Callback<String>() {
                @Override
                public void success(String tokenParams, Response response) {
                    log.d("Success: %s", tokenParams);
                    Uri uri = Uri.parse("http://dummy?" + tokenParams);
                    session.setToken(
                            uri.getQueryParameter("oauth_token"),
                            uri.getQueryParameter("oauth_token_secret"));
                    listener.requestVerifier(tokenParams);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    log.e(retrofitError, "Error requesting token");
                    onError(retrofitError);
                }
            });
        }
    }

    void onError(Throwable throwable) {
        session.logout();
        button.setProgress(-1);
        button.setEnabled(true);
    }

    public void requestAccessToken(String verifier) {
        log.d("Verifying access, %s", verifier);
        button.setLoadingText("Verifying access"); // TODO move to xml resource
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
                                   invalidateSessionState();
                                   listener.onAuthSuccess();
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   onError(throwable);
                               }
                           }
                );
    }

    public static interface Listener {
        public void requestVerifier(String queryParams);

        public void onSessionInvalidated();

        public void onAuthSuccess();
    }
}
