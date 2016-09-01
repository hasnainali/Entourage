package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import duxeye.com.ecolaundry.newapp.constant.Constant;
import duxeye.com.ecolaundry.newapp.customView.CircularProgressBar;
import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.utility.MyUrl;
import duxeye.com.ecolaundry.newapp.utility.Utility;

public class RegistrationActivity extends Activity {
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private EditText nameEditText, addressEditText, mobileEditText;
    private String name, address, mobile;
    private Button getStartedButton;
    private boolean doubleBackToExitPressedOnce = false;
    private Activity mActivity;
    //private CheckBox rememberMeCheckBox;
    private CircularProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity = this;
        init();

//        nameEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nameEditText.setCursorVisible(true);
//            }
//        });
//
//        addressEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addressEditText.setCursorVisible(true);
//            }
//        });
//
//        mobileEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mobileEditText.setCursorVisible(true);
//            }
//        });

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEditText.getText().toString().trim();
                address = addressEditText.getText().toString().trim();
                mobile = mobileEditText.getText().toString().trim();

                if (name.equalsIgnoreCase("")) {
                    Utility.showToast(mActivity, "Name should not be blank");

                } else if (address.equalsIgnoreCase("")) {
                    Utility.showToast(mActivity, "Address should not be blank");

                } else if (mobile.equalsIgnoreCase("")) {
                    Utility.showToast(mActivity, "Mobile should not be blank");

                } else if (mobile.length() < 10) {
                    Utility.showToast(mActivity, "Incorrect mobile number");

                } else {
                    if (Utility.isConnectingToInternet(mActivity)) {
                        mProgressBar.start();

                        String url = MyUrl.REGISTRATION_URL + "?name=" + name.replace(" ","%20") + "&pickupaddress=" + address.replace(" ","%20") + "&mobileno=" + mobile;
                        Log.e(TAG, "LOGIN_URL: " + url);

                        Log.e(TAG, "Name: " + name);
                        Utility.setStringSharedPreference(mActivity, Constant.NAME,name);
                        Utility.setStringSharedPreference(mActivity, Constant.MOBILE,mobile);

                        new AQuery(mActivity.getApplicationContext()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, JSONObject json, AjaxStatus status) {
                                Log.e(TAG, "Response: " + json);
                                if (json != null) {
                                    String otp = null;
                                    try {
                                        otp = json.getString("otp");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Intent intent = new Intent(mActivity, OTPActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("address", address);
                                    intent.putExtra("mobile", mobile);
                                    intent.putExtra("otp", otp);
                                    mActivity.startActivity(intent);
                                    mActivity.finish();

                                    mProgressBar.stop();

                                } else {
                                    mProgressBar.stop();
                                    if (Utility.isConnectingToInternet(mActivity)) {
                                        Utility.iPhone("Please try again!", mActivity);

                                    } else {
                                        Utility.iPhone("No internet connection", mActivity);
                                    }
                                }

                                //mProgressBar.stop();
                            }
                        });

                        //new RegisterWorkerTask(RegistrationActivity.this,name,address,mobile).execute();
                    } else {
                        Utility.iPhone("No internet connection", mActivity);
                    }
                }
            }
        });
    }

    private void init() {
        nameEditText = (EditText) findViewById(R.id.et_name);
        addressEditText = (EditText) findViewById(R.id.et_address);
        mobileEditText = (EditText) findViewById(R.id.et_mobile);
        getStartedButton = (Button) findViewById(R.id.btn_getStart);
        // rememberMeCheckBox = (CheckBox) findViewById(R.id.chk_remember);

        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Utility.showToast(this, "Double press on back button to exit");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
