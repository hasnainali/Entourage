package duxeye.com.ecolaundry.newapp.asyncktask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.utility.MyUrl;
import duxeye.com.ecolaundry.newapp.utility.Utility;

/**
 * Created by User on 11-12-2015.
 */
public class PlaceOrder extends AsyncTask<Void,Void,String> {
    private static final String TAG = PlaceOrder.class.getSimpleName();
    private Activity mActivity;
    private String name;
    private String address;
    private String mobile;
    private String day;
    private String time;
    private ProgressDialog progress;

    public PlaceOrder(Activity mActivity, String name, String address, String mobile, String day, String time) {
        this.mActivity = mActivity;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.day = day;
        this.time = time;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(mActivity);
        progress.setMessage("Placing order...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        String response = "na";
        try {
//            JSONObject jsonObject = new JSONObject(placeOrder(name, address, mobile, day, time));
//            response = (String) jsonObject.get("OTP");

            Log.e(TAG, "PLACE_ORDER_URL: " + MyUrl.PLACE_ORDER_URL);
            Log.e(TAG, "Name: " + name);
            Log.e(TAG, "Address: " + address);
            Log.e(TAG, "Mobile: " + mobile);
            Log.e(TAG, "Day: " + day);
            Log.e(TAG, "Time: " + time);

            response = placeOrder(name, address, mobile, day, time);
            //Log.e(TAG,"Response: "+response);

        } catch (Exception e) {
            Log.e(TAG, "Error in doInBackground: " + e);
            e.printStackTrace();
        }
        return response.trim();
    }

    @Override
    protected void onPostExecute(String response) {
        progress.dismiss();
        Log.e(TAG,"Response: "+response);
        try{
            JSONObject mJsonObject = new JSONObject(response);
            Dialog dd = Utility.iPhone(mJsonObject.getString("RESPONSE"),mActivity);
            dd.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });

            dd.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private String placeOrder(String name, String address, String mobile,String day,String time) {
        String urlResponse = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(MyUrl.PLACE_ORDER_URL);
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<>(5);
//                nameValuePairs.add(new BasicNameValuePair("name", name));
//                nameValuePairs.add(new BasicNameValuePair("mobileno", mobile));
                nameValuePairs.add(new BasicNameValuePair("day", day));
                nameValuePairs.add(new BasicNameValuePair("time", time));
//                nameValuePairs.add(new BasicNameValuePair("pickupaddress", address));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
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
