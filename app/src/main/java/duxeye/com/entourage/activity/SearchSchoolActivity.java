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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.SearchSchoolAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.School;

public class SearchSchoolActivity extends AppCompatActivity {
    private static final String TAG = SearchSchoolActivity.class.getSimpleName();
    private SearchBox mSearchBox;
    private Activity mActivity;
    private ArrayList<School> searchedSchoolArrayList;
    private SearchSchoolAdapter mSearchAdapter;
    private ProgressView mProgressBar;
    private boolean isAryanTaskRunning = true;
    private String stateCode;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school);
        mActivity = this;
        init();
        setupToolBar();

        mSearchBox.setLogoText("Entourage");
        mSearchBox.setLogoTextColor(getResources().getColor(R.color.colorPrimary));
//        mSearchBox.setMenuListener(new SearchBox.MenuListener(){
//
//            @Override
//            public void onMenuClick() {
//                //Hamburger has been clicked
//                Toast.makeText(mActivity, "Menu click", Toast.LENGTH_LONG).show();
//            }
//
//        });

        mSearchBox.setSearchListener(new SearchBox.SearchListener() {
            @Override
            public void onSearchOpened() {

            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchClosed() {

            }

            @Override
            public void onSearchTermChanged(String term) {
                if (!term.trim().equalsIgnoreCase("") && term.trim().length() > 0) {
                    if (isAryanTaskRunning) {
                        autocompleteSchool(term.trim());
                    }
                } else {
                    if (mSearchAdapter != null) {
                        searchedSchoolArrayList.clear();
                        mSearchAdapter.notifyDataSetChanged();
                    }


                }
            }

            @Override
            public void onSearch(String result) {

            }

            @Override
            public void onResultClick(SearchResult result) {

            }
        });
    }

    private void init() {
        stateCode = getIntent().getExtras().getString("stateCode");
        mSearchBox = (SearchBox) findViewById(R.id.searchbox);
        mSearchBox.enableVoiceRecognition(this);

        mProgressBar = (ProgressView) findViewById(R.id.pv_progressbar);


    }


    private void setupToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        ((TextView) findViewById(R.id.header_text)).setText("SEARCH SCHOOL");
    }

    private void populateRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.tv_no_address_text)).setVisibility(View.GONE);
        mSearchAdapter = new SearchSchoolAdapter(searchedSchoolArrayList,new MyOnClickListener());
        mRecyclerView.setAdapter(mSearchAdapter);

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

    private void autocompleteSchool(String input) {
        try {
            mProgressBar.start();
            isAryanTaskRunning = false;

            String url = Constant.SEARCH_SCHOOL_URL + input + "&state=" + stateCode;
            Log.e(TAG, "Url: " + url);

            new AQuery(mActivity).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    Log.e(TAG, "Response: " + json);
                    if (json != null) {
                        searchedSchoolArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < json.length(); i++) {
                                String id = json.getJSONObject(i).getString("yearbook_id");
                                String name = json.getJSONObject(i).getString("org_name");
                                searchedSchoolArrayList.add(new School(id, name));

                            }

                            if(searchedSchoolArrayList.size() > 0){
                                Log.e(TAG,"Size: "+searchedSchoolArrayList.size());
                                populateRecyclerView();

                            }else{
                                mSearchAdapter.clearRecyclerData(searchedSchoolArrayList);
                                (findViewById(R.id.tv_no_address_text)).setVisibility(View.VISIBLE);
                            }
                            mProgressBar.stop();
                            isAryanTaskRunning = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            mProgressBar.stop();
                            isAryanTaskRunning = true;
                        }

                    } else {
                        isAryanTaskRunning = true;
                        mProgressBar.stop();

                        try{
                            searchedSchoolArrayList.clear();
                            mSearchAdapter.notifyDataSetChanged();
                        }catch (Exception e) {
                            Log.e(TAG, "No response from server: " + e);

                        }

                        if (Utility.isConnectingToInternet()) {
                            MyDialog.iPhone("No response from server\nPlease try again!", mActivity);

                        } else {
                            Utility.showInternetAlert(mActivity);
                        }
                    }

                    isAryanTaskRunning = true;
                    mProgressBar.stop();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = mRecyclerView.getChildPosition(view);
            String yearBookId = searchedSchoolArrayList.get(itemPosition).getYearBookId();
            String schoolName = searchedSchoolArrayList.get(itemPosition).getOrgName();
            Log.e(TAG,"YearBookId: "+yearBookId+" and Name: "+schoolName);

            Utility.setSharedPreference(mActivity,Constant.YEARBOOKID,yearBookId);
            Utility.setSharedPreference(mActivity,Constant.SCHOOL_NAME,schoolName);

            startActivity(new Intent(mActivity,RegistrationActivity.class));
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            finish();

        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1234 && resultCode == RESULT_OK) {
//            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            mSearchBox.populateEditText(matches.get(0));
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}
