package duxeye.com.entourage.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import duxeye.com.entourage.activity.HomeActivity;
import duxeye.com.entourage.activity.LoginActivity;
import duxeye.com.entourage.adapter.SchoolYearBookAdapter;
import duxeye.com.entourage.adapter.SearchSchoolYearBookAdapter;
import duxeye.com.entourage.callback.ChangeTab;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.CustomTextView;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.Carousel;
import duxeye.com.entourage.model.ResultFromDBSearchSchool;
import duxeye.com.entourage.model.SearchSchool;
import duxeye.com.entourage.model.YearBook;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountInfoFragment extends Fragment {
    private static final String TAG = AccountInfoFragment.class.getSimpleName();
    private View mView;
    private Button logoutButton, updateButton, privacyPolicyButton, supportButton, searchSchoolButton;
    private ChangeTab mChangeTab;
    private EditText nameEditText, emailEditText, phoneEditText;
    private CircularProgressBar mProgressBar;
    //    private MySQLiteHelper mySQLiteHelper;
    private ArrayList<ResultFromDBSearchSchool> mYearBookArrayList;

    public AccountInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account_info_fragmnet, container, false);
        mChangeTab = (ChangeTab) getActivity();
        init();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final android.app.Dialog mDialog = MyDialog.logout("Are you sure you want to Logout?", getActivity());

                (mDialog.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Utility.clearSharedPreference(getActivity());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                        getActivity().finish();


                    }
                });

                (mDialog.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });




            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                if (name.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Name should not be blank!", Toast.LENGTH_SHORT).show();

                } else if (email.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Email should not be blank!", Toast.LENGTH_SHORT).show();

                } else if (!Utility.emailValidator(email)) {
                    Toast.makeText(getActivity(), "Invalid email!", Toast.LENGTH_SHORT).show();

                } else if (phone.equalsIgnoreCase("") || phone.length() < 10) {
                    Toast.makeText(getActivity(), "Invalid mobile number!", Toast.LENGTH_SHORT).show();

                } else {
                    if (Utility.isConnectingToInternet()) {
                        mProgressBar.start();
                        String url = Constant.UPDATE_MEMBER_INFO + name.replace(" ", "%20") + "&member_email=" + email + "&member_phone=" + phone + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
                        Log.e(TAG, "URL: " + url);

                        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, JSONObject json, AjaxStatus status) {
                                Log.e(TAG, "Response: " + json);
                                if (json != null) {
                                    try {

                                        Toast.makeText(getActivity(), json.getString("status"), Toast.LENGTH_SHORT).show();

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
                            }
                        });
                    } else {
                        Utility.showInternetAlert(getActivity());
                    }

                }


            }
        });

        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new T_N_C_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.account_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out);
                fragmentTransaction.commit();


            }
        });

        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("mailto:help@entourageyearbooks.com");
                Intent mIntent = new Intent(Intent.ACTION_SENDTO, uri);
                mIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");
                startActivity(mIntent);
            }
        });

        searchSchoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SearchSchoolFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.account_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out);
                fragmentTransaction.commit();
            }
        });
        return mView;
    }

    private void init() {
//        mySQLiteHelper = new MySQLiteHelper(getActivity());
        ((TextView) mView.findViewById(R.id.ac_header_text)).setText("ACCOUNT INFO");

//        yearBoonTextView = (CustomTextView) mView.findViewById(R.id.tv_year_book);
//        yearBoonTextView.setText(Utility.getSharedPreferences(getActivity(), Constant.YEARBOOK_NAME));

        nameEditText = (EditText) mView.findViewById(R.id.ac_et_name);
        emailEditText = (EditText) mView.findViewById(R.id.ac_et_email);
        phoneEditText = (EditText) mView.findViewById(R.id.ac_et_phone);

        logoutButton = (Button) mView.findViewById(R.id.ac_btn_logout);
        updateButton = (Button) mView.findViewById(R.id.ac_btn_update);
        privacyPolicyButton = (Button) mView.findViewById(R.id.ac_btn_policy);
        supportButton = (Button) mView.findViewById(R.id.ac_btn_support);
        searchSchoolButton = (Button) mView.findViewById(R.id.ac_btn_search_school);

        mProgressBar = new CircularProgressBar(getActivity());
        mProgressBar.setCancelable(false);

        getAccountInfo();


    }

    private void getAccountInfo() {
        mProgressBar.start();
        String url = Constant.MEMBER_INFO + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
//        Log.e(TAG, "URL: " + url);

        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
//                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    try {
                        setDetails(json.getString("member_name").trim(), json.getString("member_email").trim(), json.getString("member_phone").trim());
                        Utility.setSharedPreference(getActivity(), Constant.MEMBERID, json.getString("member_id").trim());

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
            }
        });

    }

    private void setDetails(String member_name, String member_email, String member_phone) {
        nameEditText.setText(member_name);
        emailEditText.setText(member_email);
        phoneEditText.setText(member_phone);

        getAllYearBooks();
    }

    private void populateRecycler() {
        RecyclerView searchResultRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_yearbooks);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        searchResultRecyclerView.setLayoutManager(mLayoutManager);
        searchResultRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchResultRecyclerView.setAdapter(new SchoolYearBookAdapter(getActivity(), mYearBookArrayList, new SchoolYearBookAdapter.ItemClickListener() {
            @Override
            public void inItemClick(ResultFromDBSearchSchool mSearchSchool) {
                getMemberPermission(mSearchSchool);

            }
        }));
    }

    private void getMemberPermission(final ResultFromDBSearchSchool mSearchSchool) {
        try {
//            Log.e(TAG, "Id: " + mSearchSchool.getYearBookId());
//            Log.e(TAG, "name: " + mSearchSchool.getOrgName());

            mProgressBar.start();
            String url = Constant.MEMBER_PERMISSION + mSearchSchool.getYearBookId() + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
//            Log.e(TAG, "Url: " + url);

            new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
//                    Log.e(TAG, "Response : " + json);
                    if (json != null) {
                        try {

                            Utility.setSharedPreference(getActivity(), Constant.MEMBER_TYPE, json.getString("member_type"));
                            Utility.setSharedPreference(getActivity(), Constant.STATUS, json.getString("status"));
                            Utility.setBoolean(getActivity(), Constant.ALLOW_PHOTOS, json.getBoolean("allow_photos"));
                            Utility.setBoolean(getActivity(), Constant.ALLOW_YEARBOOK, json.getBoolean("allow_yearbook"));
                            Utility.setBoolean(getActivity(), Constant.ALLOW_UPLOAD, json.getBoolean("allow_upload"));
                            Utility.setBoolean(getActivity(), Constant.ALLOW_CREATE_CATEGORY, json.getBoolean("allow_create_category"));
                            Utility.setBoolean(getActivity(), Constant.ALLOW_CAPTION, json.getBoolean("allow_caption"));
                            Utility.setBoolean(getActivity(), Constant.ALLOW_POST_MESSAGE, json.getBoolean("allow_post_message"));

                            Utility.setSharedPreference(getActivity(), Constant.YEARBOOKID, mSearchSchool.getYearBookId());
                            Utility.setSharedPreference(getActivity(), Constant.YEARBOOK_NAME, mSearchSchool.getOrgName());

                            mChangeTab.changeTabPosition(0);


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
        } catch (Exception e) {
            e.printStackTrace();
            mProgressBar.stop();

        }
    }

    private void getAllYearBooks() {
        try {
            mProgressBar.start();
            mYearBookArrayList = new ArrayList<>();
            String url = Constant.GET_YEAR_BOOK + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
            //Log.e(TAG, "Url: " + url);

            new AQuery(getActivity()).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    //Log.e(TAG, "Response : " + json);
                    if (json != null) {
                        try {
                            if (json.length() > 0) {
                                for (int i = 0; i < json.length(); i++) {
                                    mYearBookArrayList.add(new ResultFromDBSearchSchool(
                                            json.getJSONObject(i).getString("yearbook_id")
                                            , json.getJSONObject(i).getString("yearbook_title")));
                                }


                                mProgressBar.stop();
                                populateRecycler();

                            } else {
                                mProgressBar.stop();
                                MyDialog.iPhone("No Yearbook Found", getActivity());

                            }
                        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
