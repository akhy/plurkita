package net.akhyar.plurkita;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.inject.Inject;

import timber.log.Timber;

public class AuthorizeActivity extends WebActivity {

    public static final String EXTRA_OAUTH_VERIFIER = "oauth_verifier";
    public static final String EXTRA_QUERY_PARAMS = "query_params";

    @Inject
    Timber.Tree log;

    /**
     * @param context     Context to start the activity
     * @param queryParams Query parameters string (e.g. oauth_token_secret=abcdef&oauth_token=12345
     * @return Intent to start {@link AuthorizeActivity}
     */
    public static Intent createIntent(Context context, String queryParams) {
        Intent intent = new Intent(context, AuthorizeActivity.class);
        intent.putExtra(EXTRA_QUERY_PARAMS, queryParams);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.inject(this);
    }

    @Override
    protected void setUpWebView(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                log.d(url);
                if (url.startsWith(AppConfig.OAUTH_CALLBACK_URL)) {
                    log.d("Callback URL detected, extracting OAuth verifier");
                    view.stopLoading();
                    String verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
                    onOAuthVerifierReceived(verifier);
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected String getDefaultUrl() {
        return AppConfig.OAUTH_AUTHORIZE_URL + "?" + getIntent().getStringExtra(EXTRA_QUERY_PARAMS);
    }

    protected void onOAuthVerifierReceived(String verifier) {
        Intent result = new Intent();
        result.putExtra(EXTRA_OAUTH_VERIFIER, verifier);
        setResult(RESULT_OK, result);
        finish();
    }
}
