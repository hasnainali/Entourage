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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.rey.material.app.Dialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import duxeye.com.entourage.activity.WebActivity;
import duxeye.com.entourage.callback.CarouselCallback;
import duxeye.com.entourage.callback.ChangeTab;
import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.HomeAdapter;
import duxeye.com.entourage.adapter.PollDropDownAdapter;
import duxeye.com.entourage.callback.NewsFeedCallback;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.Answer;
import duxeye.com.entourage.model.Carousel;
import duxeye.com.entourage.model.NewsFeed;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements CarouselCallback, NewsFeedCallback {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private View mView;
    private ArrayList<Carousel> carouselsArrayList;
    private ArrayList<NewsFeed> newsFeedArrayList;
    private CircularProgressBar mProgressBar;
    private TextView sendMessageButton;
    private ChangeTab mChangeTab;
    private ArrayList<Answer> answerArrayList;
    private String selectedOption;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_fragmnet, container, false);
        mChangeTab = (ChangeTab) getActivity();
        init();
        if (Utility.isConnectingToInternet()) {
            getCarousel();
        } else {
            Utility.showInternetAlert(getActivity());
        }

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Fragment fragment = new SendMessageFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.home_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
            }
        });
        return mView;
    }

    private void init() {
        mProgressBar = new CircularProgressBar(getActivity());
        mProgressBar.setCancelable(false);
        sendMessageButton = (TextView) mView.findViewById(R.id.tv_send_message);

    }

    private void populateNewsFeedRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_feed);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        Log.e(TAG, "newsFeedArrayList: " + newsFeedArrayList.size());
//        Log.e(TAG, "carouselsArrayList: " + carouselsArrayList.size());

        mRecyclerView.setAdapter(new HomeAdapter(getActivity(), newsFeedArrayList, this, this, carouselsArrayList));

    }

    private void getCarousel() {
        try {
            carouselsArrayList = new ArrayList<>();
            mProgressBar.start();
            String url = Constant.GET_CAROUSEL + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
            //Log.e(TAG,"Url: "+url);

            new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //Log.e(TAG,"Response: "+json);
                    if (json != null) {
                        try {
                            JSONArray mJsonArray = json.getJSONArray("carousel_items");
                            for (int i = 0; i < mJsonArray.length(); i++) {

                                carouselsArrayList.add(new Carousel(
                                        mJsonArray.getJSONObject(i).getString("id"),
                                        mJsonArray.getJSONObject(i).getString("image_url"),
                                        mJsonArray.getJSONObject(i).getString("link_value"),
                                        mJsonArray.getJSONObject(i).getString("link_type")));
                                //Log.e(TAG,mJsonArray.getJSONObject(i).getString("link_type"));
                            }

                            if (carouselsArrayList.size() > 0) {
                                getNewsFeed();


                            } else {
                                MyDialog.iPhone("Carousel Items not Found\nPlease try again!", getActivity());
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
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getNewsFeed() {
        try {
            newsFeedArrayList = new ArrayList<>();
            mProgressBar.start();
            String url = Constant.NEWSFEED + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
//             Log.e(TAG,"Url: "+url);

            new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
//                    Log.e(TAG,"Response: "+json);
                    if (json != null) {
                        try {
                            JSONArray mJsonArray = json.getJSONArray("news_feed_items");
                            for (int i = 0; i < mJsonArray.length(); i++) {

                                newsFeedArrayList.add(new NewsFeed(
                                        mJsonArray.getJSONObject(i).getString("id"),
                                        mJsonArray.getJSONObject(i).getString("icon"),
                                        mJsonArray.getJSONObject(i).getString("title"),
                                        mJsonArray.getJSONObject(i).getString("author_member_id"),
                                        mJsonArray.getJSONObject(i).getString("created_string"),
                                        mJsonArray.getJSONObject(i).getString("author_name"),
                                        mJsonArray.getJSONObject(i).getString("link_value"),
                                        mJsonArray.getJSONObject(i).getString("type"),
                                        mJsonArray.getJSONObject(i).getString("notes")));
                            }

                            if (newsFeedArrayList.size() > 0) {
                                populateNewsFeedRecyclerView();
                            } else {
                                MyDialog.iPhone("No news found\nPlease try again!", getActivity());
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
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getSingleChoicePopup(String pollId) {
        final String[] title = new String[1];
        final String[] questionType = new String[1];
        final String[] questionId = new String[1];
        mProgressBar.start();
        String url = Constant.GET_OPTION_POLL_URL + pollId + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
        Log.e(TAG, "Poll Url: " + url);
        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.e(TAG, "Poll Response: " + json);
                if (json != null) {
                    try {
                        title[0] = json.getString("poll_title");
                        JSONArray questionJsonArray = json.getJSONArray("questions");
                        for (int i = 0; i < questionJsonArray.length(); i++) {
                            JSONObject mJsonObject = questionJsonArray.getJSONObject(i);
                            questionType[0] = mJsonObject.getString("type");
                            questionId[0] = mJsonObject.getString("id");

                            try {
                                answerArrayList = new ArrayList<>();
                                JSONArray ansJsonArray = questionJsonArray.getJSONObject(i).getJSONArray("answers");
                                for (int j = 0; j < ansJsonArray.length(); j++) {

                                    answerArrayList.add(new Answer(
                                            ansJsonArray.getJSONObject(j).getString("answer"),
                                            ansJsonArray.getJSONObject(j).getString("id")));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (questionType[0].equalsIgnoreCase("MULTIPLE CHOICE")) {
                            selectQuestionSpinner(title[0], questionId[0]);

                        } else if (questionType[0].equalsIgnoreCase("ENTER TEXT")) {

                            enterTextSpinner(title[0], questionId[0]);
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
            }
        });
    }

    private void selectQuestionSpinner(String title, final String questionId) {
        final Dialog dd = new Dialog(getActivity());
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.popup_select_poll);
        dd.getWindow().setLayout(-1, -2);
        dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ((TextView) dd.findViewById(R.id.tv_title)).setText(title);

        /***
         * get Recycler and populate it
         */
        final RecyclerView mRecyclerView = (RecyclerView) dd.findViewById(R.id.rv_category);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new PollDropDownAdapter(getActivity(), -1, answerArrayList, new PollDropDownAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Answer item, int selectedPosition) {
                selectedOption = item.getAnswer();
            }
        }));

        (dd.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();

                if (Utility.isConnectingToInternet()) {
                    submitAnswer(questionId, selectedOption);
                } else {
                    Utility.showInternetAlert(getActivity());
                }

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

    private void enterTextSpinner(String title, final String questionId) {
        final Dialog dd = new Dialog(getActivity());
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.popup_select_poll_edit);
        dd.getWindow().setLayout(-1, -2);
        dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ((TextView) dd.findViewById(R.id.tv_title)).setText(title);

        /***
         * get Recycler and populate it
         */
        final android.widget.EditText ansEditText = (android.widget.EditText) dd.findViewById(R.id.tv_other);

        (dd.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();

                if (Utility.isConnectingToInternet()) {
                    submitAnswer(questionId, ansEditText.getText().toString().trim());
                } else {
                    Utility.showInternetAlert(getActivity());
                }


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

    private void submitAnswer(String questionId, String answer) {
        Log.e(TAG, "questionId: " + questionId);
        Log.e(TAG, "answer: " + answer);
        mProgressBar.start();
        String url = Constant.SUBMIT_ANSWER + questionId + "&answer=" + answer + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
        Log.e(TAG, "Url: " + url);

        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.e(TAG, "Poll Response: " + json);
                if (json != null) {
                    try {

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

    @Override
    public void onNewsFeedClick(NewsFeed mNewsFeed) {
        if (mNewsFeed.getType().equalsIgnoreCase("LINK")) {
            String url = mNewsFeed.getLinkValue();

            /**
             * Open this link default webview
             */

            if (mNewsFeed.getLinkValue().equalsIgnoreCase("")) {
                MyDialog.iPhone("Web link not found for this page", getActivity());
            } else {
                Intent mIntent = new Intent(getActivity(), WebActivity.class);
                mIntent.putExtra("link", url);
                startActivity(mIntent);
            }
        }

        if (mNewsFeed.getType().equalsIgnoreCase("UPLOAD")) {
            /**
             * Open upload fragment
             */
            mChangeTab.changeTabPosition(2);

        } else if (mNewsFeed.getType().equalsIgnoreCase("POLL")) {
            getSingleChoicePopup(mNewsFeed.getId());
        }


    }

    @Override
    public void onLinkClick(String linkValue) {
        Intent mIntent = new Intent(getActivity(), WebActivity.class);
        mIntent.putExtra("link", linkValue);
        startActivity(mIntent);
    }

    @Override
    public void onUploadClick() {
        mChangeTab.changeTabPosition(2);
    }
}
