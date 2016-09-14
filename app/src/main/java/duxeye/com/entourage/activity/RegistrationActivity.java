package duxeye.com.entourage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
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
import duxeye.com.entourage.customViews.CustomEditText;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.constant.Constant;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private int schoolUpdate = 1;
    private int entourageUpdate = 1;
    private String uname, upass, uemail, uconpass;
    private Activity mActivity;
    private CircularProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mActivity = this;
        mProgressBar = new CircularProgressBar(this);
        mProgressBar.setCancelable(false);

        setUpToolbar();

        //get value for school update
        ((SwitchCompat) findViewById(R.id.sw_schoolupdate)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //do stuff when Switch is ON
                    schoolUpdate = 1;
                } else {
                    //do stuff when Switch if OFF
                    schoolUpdate = 0;
                }

            }
        });

        //get value for entourage update
        ((SwitchCompat) findViewById(R.id.sw_entourageupdate)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    //do stuff when Switch is ON
                    entourageUpdate = 1;
                } else {
                    //do stuff when Switch if OFF
                    entourageUpdate = 0;
                }
            }
        });

        findViewById(R.id.btn_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserValue();
                if (uname.equalsIgnoreCase("")) {
                    Toast.makeText(mActivity, "User Id should not be blank!", Toast.LENGTH_SHORT).show();

                } else if (uemail.equalsIgnoreCase("")) {
                    Toast.makeText(mActivity, "Email should not be blank!", Toast.LENGTH_SHORT).show();

                } else if (!Utility.emailValidator(uemail)) {
                    Toast.makeText(mActivity, "Invalid email address!", Toast.LENGTH_SHORT).show();

                } else if (upass.equalsIgnoreCase("")) {
                    Toast.makeText(mActivity, "Password should not be blank!", Toast.LENGTH_SHORT).show();

                } else if (uconpass.equalsIgnoreCase("")) {
                    Toast.makeText(mActivity, "Confirm Password  should not be blank!", Toast.LENGTH_SHORT).show();

                } else if (!upass.equals(uconpass)) {
                    Toast.makeText(mActivity, "Passwords  not matched!", Toast.LENGTH_SHORT).show();

                } else {

                    mProgressBar.start();
                    String url = Constant.REGISTRATION_URL + uemail + "&password=" + upass + "&school_password=sssshh&school_updates=" + schoolUpdate + "&entourage_updates=" + entourageUpdate + "&yearbook_id="+Utility.getSharedPreferences(mActivity,Constant.YEARBOOKID)+"&name=" + uname.replace(" ","%20");
                    Log.e(TAG, "Url: " + url);

                    AjaxCallback<JSONObject> mAjaxCallback = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
                            Log.e(TAG, "Response: " + json);

                            if (json != null) {
                                try {
                                    if (json.getString("status").equalsIgnoreCase("SUCCESS")) {

                                        Utility.setSharedPreference(mActivity, Constant.MEMBERID, json.getString("member_id"));
                                        Utility.setSharedPreference(mActivity, Constant.CREDENTIALKEY, json.getString("credential_key"));


                                        mProgressBar.stop();

                                        /***
                                         * Clear shared prefrences here
                                         */
                                        try{
                                            Utility.clearSharedPreference(mActivity);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(mActivity, YearBooksActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

                                    } else {
                                        mProgressBar.stop();
                                        MyDialog.iPhone(json.getString("message").toString(), mActivity);
                                    }

                                } catch (Exception e) {
                                    Log.e(TAG, ":err" + e.getMessage());

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

    public void getUserValue() {
        uname = ((EditText) findViewById(R.id.eT_uname)).getText().toString().trim();
        upass = ((EditText) findViewById(R.id.eT_upass)).getText().toString().trim();
        uemail = ((EditText) findViewById(R.id.eT_uemail)).getText().toString().trim();
        uconpass = ((EditText) findViewById(R.id.eT_ucon_pass)).getText().toString().trim();
    }

    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        ((TextView) findViewById(R.id.header_text)).setText("REGISTER USER");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
