package duxeye.com.ecolaundry.newapp.asyncktask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import duxeye.com.ecolaundry.newapp.activity.OTPActivity;
import duxeye.com.ecolaundry.newapp.utility.MyUrl;

/**
 * Created by Duxeye on 11-11-2015.
 */
public class RegisterWorkerTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = RegisterWorkerTask.class.getSimpleName();
    private Activity mActivity;
    private String name;
    private String address;
    private String mobile;
    private ProgressDialog progress;

    public RegisterWorkerTask(Activity mActivity, String name, String address, String mobile) {
        this.mActivity = mActivity;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(mActivity);
        progress.setMessage("Registering...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

    }

    @Override
    protected String doInBackground(Void... params) {
        String otp = "";
        try {
            String response = login(name, address, mobile);
            Log.e(TAG,"Response: "+response);

            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject != null){
                Log.e(TAG,"URL Response: "+jsonObject.toString());
                otp = jsonObject.getString("OTP").trim();
            }else {
                Log.e(TAG, "URL Response is null");
            }

            //Thread.sleep(3000);

        } catch (Exception e) {
           Log.e(TAG, "Error in doInBackground: " + e);
            e.printStackTrace();
        }
        return otp.trim();
    }

    @Override
    protected void onPostExecute(String response) {
        progress.dismiss();
       Log.e(TAG, response);
        try {
            int otp = Integer.parseInt(response);
            Log.e(TAG,"OTP = "+otp);
            /**
             * Save user detail if Shared Preferences
             */
//            Utility.setStringSharedPreference(mActivity.getApplicationContext(), Constant.USER_NAME,name);
//            Utility.setStringSharedPreference(mActivity.getApplicationContext(), Constant.PICK_UP_ADD,address);
//            Utility.setStringSharedPreference(mActivity.getApplicationContext(), Constant.MOBILE,mobile);

            /**
             * Start activity with OTP
             */
            Intent intent = new Intent(mActivity, OTPActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("address", address);
            intent.putExtra("mobile", mobile);
            intent.putExtra("otp", otp);
            mActivity.startActivity(intent);
            mActivity.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String login(String name, String address, String mobile) {
        String urlResponse = "";
        try {
            String url = MyUrl.REGISTRATION_URL+"?name="+name+"&pickupaddress="+address+"&mobileno="+mobile;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet mHttpGet = new HttpGet(url);
            Log.e(TAG, "LOGIN_URL: " + url);

            try {
                // Add your data
//                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
//                nameValuePairs.add(new BasicNameValuePair("name", name));
//                nameValuePairs.add(new BasicNameValuePair("pickupaddress", address));
//                nameValuePairs.add(new BasicNameValuePair("mobileno", mobile));
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(mHttpGet);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    urlResponse = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        } catch (Exception e) {

        }

        return urlResponse;
    }

}
