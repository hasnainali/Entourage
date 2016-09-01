package duxeye.com.entourage.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.MySQLiteHelper;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.YearBookAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.YearBook;

public class YearBooksActivity extends AppCompatActivity {
    private static final String TAG = YearBooksActivity.class.getSimpleName();
    private Activity mActivity;
    private ArrayList<YearBook> mArrayList;
    private CircularProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_books);
        mActivity = this;
        init();

    }

    private void init() {
        ((TextView) findViewById(R.id.header_text)).setText("SELECT YEARBOOK");
        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);
        if (Utility.isConnectingToInternet()) {
            getAllYearBooks();
        } else {
            Utility.showInternetAlert(mActivity);
        }


    }

    private void populateRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new YearBookAdapter(mArrayList, new YearBookAdapter.ItemClickListener() {
            @Override
            public void onYearBookClick(YearBook yearBook) {
                Utility.setSharedPreference(mActivity, Constant.YEARBOOKID, yearBook.getYearBookId());
                Utility.setSharedPreference(mActivity, Constant.YEARBOOK_NAME, yearBook.getYearBookName());
                //new MySQLiteHelper(mActivity).saveYearBook(yearBook.getYearBookId(),yearBook.getYearBookName());
                getMemberPermission();
            }
        }));
    }

    private void getAllYearBooks() {
        try {
            mProgressBar.start();
            mArrayList = new ArrayList<>();
            String url = Constant.GET_YEAR_BOOK + Utility.getSharedPreferences(mActivity, Constant.CREDENTIALKEY);
            //Log.e(TAG, "Url: " + url);

            new AQuery(mActivity).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    //Log.e(TAG, "Response : " + json);
                    if (json != null) {
                        try {
                            if (json.length() > 0) {
                                for (int i = 0; i < json.length(); i++) {
                                    mArrayList.add(new YearBook(
                                            json.getJSONObject(i).getString("yearbook_id")
                                            , json.getJSONObject(i).getString("yearbook_title")));
                                }


                                mProgressBar.stop();
                                populateRecyclerView();

                            } else {
                                mProgressBar.stop();
                                MyDialog.iPhone("No Yearbook Found", mActivity);

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
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void getMemberPermission() {
        try {
            mProgressBar.start();
            String url = Constant.MEMBER_PERMISSION + Utility.getSharedPreferences(mActivity, Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(mActivity, Constant.CREDENTIALKEY);
            //Log.e(TAG, "Url: " + url);

            new AQuery(mActivity).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
//                    Log.e(TAG, "Response : " + json);
                    if (json != null) {
                        try {

                            Utility.setSharedPreference(mActivity, Constant.MEMBER_TYPE, json.getString("member_type"));
                            Utility.setSharedPreference(mActivity, Constant.STATUS, json.getString("status"));
                            Utility.setBoolean(mActivity, Constant.ALLOW_PHOTOS, json.getBoolean("allow_photos"));
                            Utility.setBoolean(mActivity, Constant.ALLOW_YEARBOOK, json.getBoolean("allow_yearbook"));
                            Utility.setBoolean(mActivity, Constant.ALLOW_UPLOAD, json.getBoolean("allow_upload"));
                            Utility.setBoolean(mActivity, Constant.ALLOW_CREATE_CATEGORY, json.getBoolean("allow_create_category"));
                            Utility.setBoolean(mActivity, Constant.ALLOW_CAPTION, json.getBoolean("allow_caption"));
                            Utility.setBoolean(mActivity, Constant.ALLOW_POST_MESSAGE, json.getBoolean("allow_post_message"));

//                            Log.e(TAG, Utility.getSharedPreferences(mActivity, Constant.MEMBER_TYPE));
//                            Log.e(TAG, Utility.getSharedPreferences(mActivity, Constant.STATUS));
//                            Log.e(TAG, "" + Utility.getBoolean(mActivity, Constant.ALLOW_PHOTOS));
//                            Log.e(TAG, "" + Utility.getBoolean(mActivity, Constant.ALLOW_YEARBOOK));
//                            Log.e(TAG, "" + Utility.getBoolean(mActivity, Constant.ALLOW_UPLOAD));
//                            Log.e(TAG, "" + Utility.getBoolean(mActivity, Constant.ALLOW_CREATE_CATEGORY));
//                            Log.e(TAG, "" + Utility.getBoolean(mActivity, Constant.ALLOW_CAPTION));
//                            Log.e(TAG, "" + Utility.getBoolean(mActivity, Constant.ALLOW_POST_MESSAGE));


                            Intent intent = new Intent(mActivity, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                            finish();


                            mProgressBar.stop();
                            populateRecyclerView();


                        } catch (Exception e) {
                            mProgressBar.stop();
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
            });
        } catch (Exception e) {
            e.printStackTrace();
            mProgressBar.stop();

        }
    }
}
