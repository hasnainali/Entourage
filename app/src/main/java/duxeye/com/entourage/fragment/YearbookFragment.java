package duxeye.com.entourage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.LadderYearBookAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.LadderYearBook;
import duxeye.com.entourage.model.YearBook;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearbookFragment extends Fragment {
    private static final String TAG = YearbookFragment.class.getSimpleName();
    private View mView;
    private CircularProgressBar mProgressBar;
    private ArrayList<LadderYearBook> mArrayList;

    public YearbookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_yearbook, container, false);
        ((TextView) mView.findViewById(R.id.yb_header_text)).setText("YEARBOOK");

        (mView.findViewById(R.id.iv_refresh)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYearBook();
            }
        });

        mProgressBar = new CircularProgressBar(getActivity());
        mProgressBar.setCancelable(false);
        getYearBook();
        return mView;
    }


    private void populatePhotoGrid() {
        RecyclerView photoGridView = (RecyclerView) mView.findViewById(R.id.rv_year_book);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        photoGridView.setLayoutManager(mLayoutManager);
        photoGridView.setItemAnimator(new DefaultItemAnimator());
        photoGridView.setAdapter(new LadderYearBookAdapter(getActivity(), mArrayList, new LadderYearBookAdapter.ItemClickListener() {
            @Override
            public void onPhotoClick(LadderYearBook mLadderYearBook,int position,boolean isLeft,boolean isRight) {
//                Log.e(TAG,"Position: "+position);
                if(isLeft){
                    Utility.setSharedPreference(getActivity(),Constant.CURRENT_PAGE_INDEX,""+((position * 2) + 0)+"");
                }
                if(isRight){
                    Utility.setSharedPreference(getActivity(),Constant.CURRENT_PAGE_INDEX,""+((position * 2) + 1)+"");
                }

                Fragment fragment = new YearBookDetailsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.year_book_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        }));
    }

    private void getYearBook() {
        mProgressBar.start();
        String url = Constant.YEARBOOK_LADDER + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
//        Log.e(TAG, "Url: " + url);
        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
//                Log.e(TAG, "Response : " + json);
                if (json != null) {
                    try {
                        Utility.setSharedPreference(getActivity(),Constant.NUMBER_PAGES_PROD,json.getString("number_pages_prod"));
                        mArrayList = new ArrayList<>();
                        JSONArray mJsonArray = json.getJSONArray("pages");
                        if (mJsonArray.length() > 0) {
                            Log.e(TAG,"json Length: "+mJsonArray.length());
                            for (int i = 0; i < mJsonArray.length(); i++) {
                                mArrayList.add(new LadderYearBook(
                                        mJsonArray.getJSONObject(i).getString("left_page_number"),
                                        mJsonArray.getJSONObject(i).getString("left_page_label"),
                                        mJsonArray.getJSONObject(i).getString("left_page_img"),
                                        mJsonArray.getJSONObject(i).getString("left_page_height"),
                                        mJsonArray.getJSONObject(i).getString("left_page_width"),
                                        mJsonArray.getJSONObject(i).getString("left_wip_page_id"),
                                        mJsonArray.getJSONObject(i).getString("left_page_title"),
                                        mJsonArray.getJSONObject(i).getString("right_page_number"),
                                        mJsonArray.getJSONObject(i).getString("right_page_label"),
                                        mJsonArray.getJSONObject(i).getString("right_page_img"),
                                        mJsonArray.getJSONObject(i).getString("right_page_height"),
                                        mJsonArray.getJSONObject(i).getString("right_page_width"),
                                        mJsonArray.getJSONObject(i).getString("right_wip_page_id"),
                                        mJsonArray.getJSONObject(i).getString("right_page_title")));
                            }
//
                            populatePhotoGrid();
                            mProgressBar.stop();


                        } else {
                            mProgressBar.stop();
                            MyDialog.iPhone("No Yearbook Found!", getActivity());

                        }
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

}
