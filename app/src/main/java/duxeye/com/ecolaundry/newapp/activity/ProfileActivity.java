package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import duxeye.com.ecolaundry.newapp.constant.Constant;
import duxeye.com.ecolaundry.newapp.customView.CircularProgressBar;
import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.utility.MyUrl;
import duxeye.com.ecolaundry.newapp.utility.Utility;

public class ProfileActivity extends Activity {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private EditText nameEditText, addressEditText, mobileEditText;
    private ImageButton editButton;
    private Activity mActivity;
    private CircularProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();


        /**
         * Click on edit button
         */
        editButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = event.getActionMasked();
                switch (id) {
                    case MotionEvent.ACTION_DOWN:
                        editButton.setImageResource(R.mipmap.profile_edit);
                        break;

                    case MotionEvent.ACTION_UP:
                        editButton.setImageResource(R.mipmap.profile_edit_selected);

                        nameEditText.setFocusableInTouchMode(true);
                        nameEditText.setTextColor(getResources().getColor(R.color.colorApp));

                        addressEditText.setFocusableInTouchMode(true);
                        addressEditText.setTextColor(getResources().getColor(R.color.colorApp));

                        mobileEditText.setFocusableInTouchMode(true);
                        mobileEditText.setTextColor(getResources().getColor(R.color.colorApp));

                        break;

                    case MotionEvent.ACTION_CANCEL:
                        editButton.setImageResource(R.mipmap.profile_edit_selected);
                        break;
                }
                return true;

            }
        });

        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });
    }

    private void init() {
        mActivity = this;
        editButton = (ImageButton) findViewById(R.id.ib_edit);
        nameEditText = (EditText) findViewById(R.id.et_name);
        nameEditText.setText(Utility.getStringSharedPreferences(ProfileActivity.this, Constant.USER_NAME));
        nameEditText.setFocusable(false);
        nameEditText.setTextColor(getResources().getColor(R.color.colorTextOff));

        addressEditText = (EditText) findViewById(R.id.et_address);
        addressEditText.setText(Utility.getStringSharedPreferences(ProfileActivity.this, Constant.PICK_UP_ADD));
        addressEditText.setFocusable(false);
        addressEditText.setTextColor(getResources().getColor(R.color.colorTextOff));

        mobileEditText = (EditText) findViewById(R.id.et_mobile);
        mobileEditText.setText(Utility.getStringSharedPreferences(ProfileActivity.this, Constant.MOBILE));
        mobileEditText.setFocusable(false);
        mobileEditText.setTextColor(getResources().getColor(R.color.colorTextOff));


        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);

    }

    public void updateProfileButton(View view) {

        if (Utility.isConnectingToInternet(mActivity)) {
            final String name = nameEditText.getText().toString().trim();
            final String address = addressEditText.getText().toString().trim();
            final String mobile = mobileEditText.getText().toString().trim();

            if (name.equalsIgnoreCase("") && address.equalsIgnoreCase("") && mobile.equalsIgnoreCase("")) {
                Utility.iPhone("All fields are mandatory\nShould not be blank", mActivity);
            } else {
                //http://www.ecolaundryindia.com/updateprofile.php?name=mayur&pickupaddress=ujjain&mobile=9753950040
                if (Utility.isConnectingToInternet(mActivity)) {
                    mProgressBar.start();
                    String url = MyUrl.UPDATE_PROFILE_URL + name + "&pickupaddress=" + address + "&mobile=" + mobile;

                    Log.e(TAG, "Url: " + url);


                    new AQuery(mActivity).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
                            Log.e(TAG, "Response: " + json);
                            if (json != null) {
                                try {
                                    if (json.getString("Successfull").equalsIgnoreCase("true")) {
                                        Utility.setStringSharedPreference(mActivity, Constant.USER_NAME, name);
                                        Utility.setStringSharedPreference(mActivity, Constant.PICK_UP_ADD, address);
                                        Utility.setStringSharedPreference(mActivity, Constant.MOBILE, mobile);
                                        Utility.showToast(mActivity, json.getString("message"));

                                        mActivity.finish();

                                    } else {
                                        Utility.iPhone(json.getString("message") + "\nPlease try again!", mActivity);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                mProgressBar.stop();
                                //startSlider();

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
                    Utility.iPhone("No internet connection", mActivity);
                }

            }

        } else {
            Utility.iPhone("No internet connection", mActivity);
        }


    }

    @Override
    public void onBackPressed() {

    }

//    class UpdateProfile extends AsyncTask<Void, Void, String> {
//        private String name;
//        private String address;
//        private String mobile;
//        private ProgressDialog progress;
//
//        public UpdateProfile(String name, String address, String mobile) {
//            this.name = name;
//            this.address = address;
//            this.mobile = mobile;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progress = new ProgressDialog(mActivity);
//            progress.setMessage("Updating profile...");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String urlResponse = "na";
//            try {
//                Log.e(TAG, "UPDATE_PROFILE_URL: " + MyUrl.UPDATE_PROFILE_URL);
//                Log.e(TAG, "Name: " + name);
//                Log.e(TAG, "Address: " + address);
//                Log.e(TAG, "Mobile: " + mobile);
//
//                try {
//                    HttpClient httpclient = new DefaultHttpClient();
//                    HttpPost httppost = new HttpPost(MyUrl.UPDATE_PROFILE_URL);
//
//                    try {
//                        // Add your data
//                        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
//                        nameValuePairs.add(new BasicNameValuePair("name", name));
//                        nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
//                        nameValuePairs.add(new BasicNameValuePair("pickupaddress", address));
//                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                        // Execute HTTP Post Request
//                        HttpResponse response = httpclient.execute(httppost);
//                        StatusLine statusLine = response.getStatusLine();
//                        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
//                            ByteArrayOutputStream out = new ByteArrayOutputStream();
//                            response.getEntity().writeTo(out);
//                            urlResponse = out.toString();
//                            out.close();
//                        } else {
//                            //Closes the connection.
//                            response.getEntity().getContent().close();
//                            throw new IOException(statusLine.getReasonPhrase());
//                        }
//
//                    } catch (ClientProtocolException e) {
//                        // TODO Auto-generated catch block
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                    }
//                } catch (Exception e) {
//
//                }
//
//            } catch (Exception e) {
//                Log.e(TAG, "Error in doInBackground: " + e);
//                e.printStackTrace();
//            }
//            return urlResponse.trim();
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//            progress.dismiss();
//            Log.e(TAG, "Response: " + response);
//            try {
//                mActivity.finish();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
