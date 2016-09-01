package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import duxeye.com.ecolaundry.newapp.customView.CircularProgressBar;
import duxeye.com.ecolaundry.newapp.R;

public class PricesActivity extends Activity {
    private WebView mWebView;
    private CircularProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hard_code_price_list);

        mProgressBar = new CircularProgressBar(this);
        mProgressBar.setCancelable(false);
        mProgressBar.start();

        mWebView = (WebView)findViewById(R.id.wv_price);
        mWebView.loadUrl("http://ecolaundryindia.com/mprice.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {

                mProgressBar.stop();
            }
        });

//        arrayList = Utility.getServicesContent();
//        init();

        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PricesActivity.this.finish();
            }
        });

//        adapter = new PriceListAdapter(PricesActivity.this,arrayList);
//        mListView.setAdapter(adapter);

    }

//    private void init() {
//        mListView = (ListView) findViewById(R.id.lv_prices);
//    }

    @Override
    public void onBackPressed() {

    }
}
