package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import duxeye.com.ecolaundry.newapp.constant.Constant;
import duxeye.com.ecolaundry.newapp.customView.CircularProgressBar;
import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.utility.MyUrl;
import duxeye.com.ecolaundry.newapp.utility.Utility;

public class OTPActivity extends Activity {
    private static final String TAG = OTPActivity.class.getSimpleName();
    private boolean doubleBackToExitPressedOnce = false;
    private EditText otpEditText;
    private int OTP;
    private Activity mActivity;
    private CircularProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mActivity = this;

        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);

        IntentFilter intentFilter = new IntentFilter("MESSAGE SENT");
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mReceiver, intentFilter);

        try{

            OTP = Integer.parseInt(getIntent().getExtras().getString("otp"));
            Log.e(TAG,"OTP: "+OTP);

        }catch (Exception e){
            e.printStackTrace();
        }
        otpEditText = (EditText) findViewById(R.id.et_otp);

        findViewById(R.id.btn_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP(otpEditText.getText().toString().trim());
            }
        });
    }

    public void verifyOTP(String otp) {
        if(Utility.isConnectingToInternet(mActivity)){
            if (otp.equalsIgnoreCase("") || otp.length() < 4) {
                Utility.showToast(this, "Invalid OTP");
            } else {
                mProgressBar.stop();
                if (Integer.parseInt(otp) == OTP) {
                    String url = MyUrl.VERIFY_OTP+"is_otp_verify=1&mobile="+Utility.getStringSharedPreferences(mActivity,Constant.MOBILE);
                    Log.e(TAG,"Url: "+url);

                    new AQuery(mActivity).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
                            Log.i(TAG, "Response: " + json);
                            if (json != null) {
                                String success = null;
                                try {
                                    success = json.getString("Successfull");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if(success.trim().equalsIgnoreCase("true")){

                                    /**
                                     * Save user detail if Shared Preferences
                                     */
                                    Utility.setStringSharedPreference(mActivity, Constant.USER_NAME,  getIntent().getExtras().getString("name"));
                                    Utility.setStringSharedPreference(mActivity, Constant.PICK_UP_ADD, getIntent().getExtras().getString("address"));
                                    Utility.setStringSharedPreference(mActivity, Constant.MOBILE, getIntent().getExtras().getString("mobile"));

                                    startActivity(new Intent(mActivity, MainActivity.class));
                                    finish();


                                }else{
                                    Utility.iPhone("Invalid OTP \nPlease try again", mActivity);
                                }

                                mProgressBar.stop();

                            } else {
                                mProgressBar.stop();
                                if (Utility.isConnectingToInternet(mActivity)) {
                                    Utility.iPhone("Please try again!", mActivity);

                                } else {
                                    Utility.iPhone("No internet connection", mActivity);
                                }
                            }

                            mProgressBar.stop();
                        }
                    });
                } else {
                    Utility.showToast(this, "Invalid OTP");
                }
            }
        } else{
            Utility.iPhone("No internet connection", mActivity);
        }
    }


    @Override
    public void onBackPressed() {
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
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

    @Override
    protected void onStop() {
        super.onStop();
        try {
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
//            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            otpEditText.setText(Utility.getStringSharedPreferences(mActivity, Constant.MESSAGE));
            verifyOTP(Utility.getStringSharedPreferences(mActivity, Constant.MESSAGE));
            Utility.setStringSharedPreference(mActivity, Constant.MESSAGE, "");

        }
    };
}
