package duxeye.com.entourage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.LadderYearBookAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.LadderYearBook;

/**
 * A simple {@link Fragment} subclass.
 */
public class T_N_C_Fragment extends Fragment {
    private static final String TAG = T_N_C_Fragment.class.getSimpleName();
    private View mView;
    private CircularProgressBar mProgressBar;
    private ImageView backButton;
    private WebView mWebView;

    public T_N_C_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_t_n_c_web, container, false);
        init();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    Fragment fragment = new AccountInfoFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.account_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.trans_right_in, R.anim.trans_right_out);
                    fragmentTransaction.commit();
                }

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

        return mView;
    }

    private void init() {
        backButton = (ImageView) mView.findViewById(R.id.iv_t_n_c_back);
        mWebView = (WebView) mView.findViewById(R.id.wb_t_n_c_load);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //mWebView.loadUrl("http://www.entourageyearbooks.com/PrivacyPolicy.asp");
        mWebView.loadUrl("file:///android_asset/html/PrivacPolicy.html");
        mProgressBar = new CircularProgressBar(getActivity());
        mProgressBar.setCancelable(false);
        mProgressBar.start();

        load_T_N_C();

    }


    private void load_T_N_C() {

    }

}
