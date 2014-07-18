package net.akhyar.plurkita;

import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = (WebView) findViewById(R.id.webview);
        setUpWebView(webView);
        webView.loadUrl(getDefaultUrl());
    }

    protected String getDefaultUrl() {
        return getIntent().getStringExtra("url");
    }

    protected void setUpWebView(WebView webView) {
    }
}
