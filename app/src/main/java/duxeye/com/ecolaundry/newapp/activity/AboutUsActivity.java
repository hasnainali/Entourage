package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.customView.CircularProgressBar;


public class AboutUsActivity extends Activity {
    private WebView mWebView;
    private CircularProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mProgressBar = new CircularProgressBar(this);
        mProgressBar.setCancelable(false);
        mProgressBar.start();

        mWebView = (WebView)findViewById(R.id.wv_about_us);
//        mWebView.loadUrl("file:///android_asset/html/About.htm");
        mWebView.loadUrl("http://ecolaundryindia.com/aboutus.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {

                mProgressBar.stop();
            }
        });

        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
