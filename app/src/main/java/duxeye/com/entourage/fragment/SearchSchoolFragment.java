package duxeye.com.entourage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.rey.material.app.Dialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.MySQLiteHelper;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.activity.HomeActivity;
import duxeye.com.entourage.adapter.SearchSchoolYearBookAdapter;
import duxeye.com.entourage.adapter.SpinnerAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.SearchSchool;
import duxeye.com.entourage.model.State;

/**
 * Created by Ondoor (Hasnain) on 8/2/2016.
 */
public class SearchSchoolFragment extends Fragment {
    private static final String TAG = SearchSchoolFragment.class.getSimpleName();
    private View mView;
    private Button selectStateButton, searchButton;
    private EditText schoolNameEditText;
    private RecyclerView searchResultRecyclerView;
    private ArrayList<SearchSchool> mArrayList;
    private ArrayList<State> mStateArrayList;
    private CircularProgressBar mProgressBar;
    private String stateCode;
    private String selectedOption;
    private ImageView backButton;
    private String emptyCheck;

    public SearchSchoolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_school, container, false);
        init();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = schoolNameEditText.getText().toString().trim();
                if (searchString.equalsIgnoreCase("")) {
                    MyDialog.iPhone("School name should not be empty!", getActivity());
                } else {
                    if (selectStateButton.getText().toString().trim().equalsIgnoreCase(emptyCheck)) {
                        MyDialog.iPhone("Select state!", getActivity());
                    } else {
                        getSchool(searchString);

                    }
                }

            }
        });

        selectStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSpinner("Select State");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AccountInfoFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.account_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.trans_right_in, R.anim.trans_right_out);
                fragmentTransaction.commit();
            }
        });

        return mView;
    }

    private void init() {
        selectStateButton = (Button) mView.findViewById(R.id.btn_select_state);
        searchButton = (Button) mView.findViewById(R.id.btn_search);
        schoolNameEditText = (EditText) mView.findViewById(R.id.et_school_name);
        searchResultRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_searched_school);
        backButton = (ImageView) mView.findViewById(R.id.iv_search_school_back);
        mProgressBar = new CircularProgressBar(getActivity());
        mProgressBar.setCancelable(false);

        getState();
    }

    private void populateSearResult() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        searchResultRecyclerView.setLayoutManager(mLayoutManager);
        searchResultRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchResultRecyclerView.setAdapter(new SearchSchoolYearBookAdapter(getActivity(), mArrayList, new SearchSchoolYearBookAdapter.ItemClickListener() {
            @Override
            public void inItemClick(final SearchSchool mSearchSchool) {
                /**
                 * get Link Account Information
                 */
                mProgressBar.start();
                String url = Constant.GETLINKACCOUNTINFORMATION + mSearchSchool.getYearBookId() + "&yearbook_id=" + mSearchSchool.getYearBookId();
//                Log.e(TAG, "Url: " + url);

                new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
//                        Log.e(TAG, "Response : " + json);
                        if (json != null) {
                            try {
                                if (json.getString("link_status").equalsIgnoreCase("ACTIVE")) {
                                    /**
                                     * Add new Yearbook
                                     */
                                    String addUrl = Constant.ACCESS_YEARBOOK + json.getString("active_yearbook_id") + "&link_password=" + json.getString("link_account_password") + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
//                                    Log.e(TAG, "Url: " + addUrl);
                                    new AQuery(getActivity()).ajax(addUrl, JSONObject.class, new AjaxCallback<JSONObject>() {

                                        @Override
                                        public void callback(String url, JSONObject json, AjaxStatus status) {
//                                            Log.e(TAG, "Response : " + json);
                                            if (json != null) {
                                                try {
                                                    if (json.getString("status").equalsIgnoreCase("SUCCESS")) {

                                                        /**
                                                         * go back to account info page
                                                         *
                                                         */

                                                        Fragment fragment = new AccountInfoFragment();
                                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.replace(R.id.account_container, fragment);
                                                        fragmentTransaction.addToBackStack(null);
                                                        fragmentTransaction.setCustomAnimations(R.anim.trans_right_in, R.anim.trans_right_out);
                                                        fragmentTransaction.commit();

                                                    } else {
                                                        MyDialog.iPhone(json.getString("message"), getActivity());
                                                    }

                                                    mProgressBar.stop();

                                                } catch (Exception e) {
                                                    mProgressBar.stop();
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                mProgressBar.stop();
                                                if (Utility.isConnectingToInternet()) {
                                                    MyDialog.iPhone("No response from server\nPlease try again!", getActivity());

                                                } else {
                                                    Utility.showInternetAlert(getActivity());
                                                }
                                            }
                                        }
                                    });

                                } else {
                                    MyDialog.iPhone("You are not allowed for this Yearbook!", getActivity());
                                }


                                mProgressBar.stop();

                            } catch (Exception e) {
                                mProgressBar.stop();
                                e.printStackTrace();
                            }
                        } else {
                            mProgressBar.stop();
                            if (Utility.isConnectingToInternet()) {
                                MyDialog.iPhone("No response from server\nPlease try again!", getActivity());

                            } else {
                                Utility.showInternetAlert(getActivity());
                            }
                        }
                    }
                });

            }
        }));


    }

    private void getSchool(String searchString) {
        mProgressBar.start();

        String url = Constant.SEARCH_SCHOOL_URL + searchString + "&state=" + stateCode;
//        Log.e(TAG, "Url: " + url);

        new AQuery(getActivity()).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
//                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    mArrayList = new ArrayList<>();
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            String id = json.getJSONObject(i).getString("yearbook_id");
                            String name = json.getJSONObject(i).getString("org_name");
                            mArrayList.add(new SearchSchool(id, name));

                        }

                        populateSearResult();

                        mProgressBar.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressBar.stop();
                    }

                } else {
                    mProgressBar.stop();
                    if (Utility.isConnectingToInternet()) {
                        MyDialog.iPhone("No response from server\nPlease try again!", getActivity());

                    } else {
                        Utility.showInternetAlert(getActivity());
                    }
                }

                mProgressBar.stop();
            }
        });
    }

    private void getState() {
        String url = Constant.GET_STATE_URL;
        Log.e(TAG, "Url: " + url);

        new AQuery(getActivity()).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    mStateArrayList = new ArrayList<>();
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            String id = json.getJSONObject(i).getString("state_id");
                            String name = json.getJSONObject(i).getString("state_name");

                            if (id.equalsIgnoreCase("")) {
                                selectStateButton.setText(name);
                                emptyCheck = name;
                            } else {
                                mStateArrayList.add(new State(id, name));
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
                        MyDialog.iPhone("No response from server\nPlease try again!", getActivity());

                    } else {
                        Utility.showInternetAlert(getActivity());
                    }
                }

                mProgressBar.stop();
            }
        });
    }

    private void populateSpinner(String title) {
        final Dialog dd = new Dialog(getActivity());
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.popup_state);
        dd.getWindow().setLayout(-1, -2);
        dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ((TextView) dd.findViewById(R.id.tv_title)).setText(title);

        /***
         * get Recycler and populate it
         */
        final RecyclerView mRecyclerView = (RecyclerView) dd.findViewById(R.id.rv_state);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new SpinnerAdapter(getActivity(), -1, mStateArrayList, new SpinnerAdapter.OnItemClickListener() {
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
                selectStateButton.setText(selectedOption);

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

}
