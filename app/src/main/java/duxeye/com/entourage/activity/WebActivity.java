package duxeye.com.entourage.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import duxeye.com.entourage.R;
import duxeye.com.entourage.customViews.CircularProgressBar;

public class WebActivity extends AppCompatActivity {
    private static final String TAG = WebActivity.class.getSimpleName();
    private Activity mActivity;
    private ImageView backButton;
    private WebView mWebView;
    private CircularProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mActivity = this;
        init();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG,"Link Value: "+url);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                mProgressBar.stop();

            }
        });
    }

    private void init() {
        backButton = (ImageView) findViewById(R.id.iv_web_view_back);
        mWebView = (WebView) findViewById(R.id.wb_load);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.loadUrl( getIntent().getExtras().getString("link"));
        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);
        mProgressBar.start();

    }
}
