package duxeye.com.entourage.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import duxeye.com.entourage.R;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.CustomTextView;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.constant.Constant;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Activity mActivity;
    private CircularProgressBar mProgressBar;
    private CustomTextView registerTextView;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;
        init();
//        setUpToolbar();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SelectSchoolActivity.class));
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mobiletest1
                //test1
                String userName = ((EditText) findViewById(R.id.eT_userid)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.eT_password)).getText().toString().trim();

                if (userName.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "User name should not be blank!", Toast.LENGTH_SHORT).show();

                } else if (password.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Password should not be blank!", Toast.LENGTH_SHORT).show();
                } else {

                    mProgressBar.start();
                    String url = Constant.LOGIN_URL + userName + "&password=" + password + "&device_platform=APPLE%2BIOS&device_type=foo&device_token=foo";
//                    Log.e(TAG, "Url: " + url);

                    AjaxCallback<JSONObject> mAjaxCallback = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
//                            Log.e(TAG, "Response: " + json);

                            if (json != null) {
                                try {
                                    if (json.getString("status").equalsIgnoreCase("SUCCESS")) {

                                        try{
                                            Utility.clearSharedPreference(mActivity);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        Utility.setSharedPreference(mActivity, Constant.MEMBERID, json.getString("member_id"));
                                        Utility.setSharedPreference(mActivity, Constant.CREDENTIALKEY, json.getString("credential_key"));

                                        Intent intent = new Intent(mActivity, YearBooksActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                        mProgressBar.stop();

                                    } else {
                                        if (json.has("message")) {
                                            mProgressBar.stop();
                                            MyDialog.iPhone(json.getString("message").toString(), mActivity);
                                        } else {
                                            mProgressBar.stop();
                                            MyDialog.iPhone("Invalid credential", mActivity);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                mProgressBar.stop();
                                if (Utility.isConnectingToInternet()) {
                                    MyDialog.iPhone("No response from server\nPlease try again!", mActivity);

                                } else {
                                    Utility.showInternetAlert(mActivity);
                                }
                            }
                        }
                    };


                    Map<String, String> header = new HashMap<>();
                    header.put("accept", "application/json");
                    header.put("apikey", Constant.API_KEY);
                    mAjaxCallback.params(header);

                    new AQuery(mActivity).ajax(url, JSONObject.class, mAjaxCallback);
                }


            }
        });


    }

    private void init() {
        loginButton = (Button) findViewById(R.id.btn_login);
        registerTextView = (CustomTextView) findViewById(R.id.tv_regis);
        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);
    }

//    private void setUpToolbar() {
//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(mToolbar);
//        ((TextView) findViewById(R.id.header_text)).setText("LOGIN");
//
//
//    }

}
