package duxeye.com.entourage.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.quinny898.library.persistentsearch.SearchBox;
import com.rey.material.app.Dialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Spinner;

import org.json.JSONArray;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.SpinnerAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.CustomTextView;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.State;

public class SelectSchoolActivity extends AppCompatActivity {
    private static final String TAG = SelectSchoolActivity.class.getSimpleName();
    private Activity mActivity;
    private CircularProgressBar mProgressBar;
    private TextView selectStatePopup,tvSearchSchool;
    private ArrayList<State> mArrayList;
    private String selectedOption;
    private RelativeLayout searchLayout;
    private String stateCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        mActivity = this;
        setupToolBar();
        init();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        selectStatePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                populateSpinner("Select State");
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stateCode.equalsIgnoreCase("")){
                    MyDialog.iPhone("Select state",mActivity);
                }else{
                    Intent intent = new Intent(mActivity, SearchSchoolActivity.class);
                    intent.putExtra("stateCode",stateCode);
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                }

            }
        });

    }

    private void init() {
        selectStatePopup = (TextView) findViewById(R.id.tv_state);
        tvSearchSchool = (TextView) findViewById(R.id.tv_search_school);
        searchLayout = (RelativeLayout) findViewById(R.id.rl_search);
        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);
        mProgressBar.start();
        String url = Constant.GET_STATE_URL;
        Log.e(TAG, "Url: " + url);

        new AQuery(mActivity).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    mArrayList = new ArrayList<>();
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            String id = json.getJSONObject(i).getString("state_id");
                            String name = json.getJSONObject(i).getString("state_name");

                            if (id.equalsIgnoreCase("")) {
                                selectStatePopup.setText(name);
                            } else {
                                mArrayList.add(new State(id, name));
                            }
                        }
                        mProgressBar.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressBar.stop();
                    }

                } else {

                    mProgressBar.stop();
                    if (Utility.isConnectingToInternet()) {
                        MyDialog.iPhone("No response from server\nPlease try again!", mActivity);

                    } else {
                        Utility.showInternetAlert(mActivity);
                    }
                }

                mProgressBar.stop();
            }
        });

    }

    private void setupToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        ((TextView) findViewById(R.id.header_text)).setText("SELECT SCHOOL");
    }

    private void populateSpinner(String title) {
        final Dialog dd = new Dialog(mActivity);
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.popup_state);
        dd.getWindow().setLayout(-1, -2);
        dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ((TextView) dd.findViewById(R.id.tv_title)).setText(title);

        /***
         * get Recycler and populate it
         */
        final RecyclerView mRecyclerView = (RecyclerView) dd.findViewById(R.id.rv_state);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(new SpinnerAdapter(mActivity, -1, mArrayList, new SpinnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(State item, int selectedPosition) {
                selectedOption = item.getName();
                stateCode = item.getId();
            }
        }));

        (dd.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
                selectStatePopup.setText(selectedOption);

            }
        });

        (dd.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dd.dismiss();

            }
        });

        dd.show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(mActivity, LoginActivity.class));
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        tvSearchSchool.setText(Utility.getSharedPreferences(mActivity,Constant.SCHOOL_NAME));
//        if(Utility.getSharedPreferences(mActivity,Constant.SCHOOL_NAME).equalsIgnoreCase("Search School")){
//
//            findViewById(R.id.searchIcon).setVisibility(View.VISIBLE);
//
//        }else{
//
//            findViewById(R.id.searchIcon).setVisibility(View.GONE);
//        }

    }
}
