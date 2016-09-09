package duxeye.com.entourage.fragment;


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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.CategoryAdapter;
import duxeye.com.entourage.callback.Refresh;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment implements Refresh {
    private static final String TAG = PhotosFragment.class.getSimpleName();
    private View mView;
    private CircularProgressBar mProgressBar;
    private ArrayList<Category> mCategoryArrayList;

    public PhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_photos, container, false);
        ((TextView) mView.findViewById(R.id.photo_header_text)).setText("CATEGORIES");
        mProgressBar = new CircularProgressBar(getActivity());
        mProgressBar.setCancelable(false);
        getCategory();
        return mView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()){
            getCategory();
        }
    }

    private void getCategory(){
        mProgressBar.start();
        mCategoryArrayList = new ArrayList<>();
        String url = Constant.GETCATEGORY+ Utility.getSharedPreferences(getActivity(),Constant.YEARBOOKID)+"&credential_key="+Utility.getSharedPreferences(getActivity(),Constant.CREDENTIALKEY);
//        Log.e(TAG,"Url: "+url);
        new AQuery(getActivity()).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>(){
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
//                Log.e(TAG,"Response: "+json);
                if(json != null){
                    try{
                        for(int i=0; i<json.length(); i++){

                            mCategoryArrayList.add(new Category(
                                    json.getJSONObject(i).getString("id"),
                                    json.getJSONObject(i).getString("count"),
                                    json.getJSONObject(i).getString("icon"),
                                    json.getJSONObject(i).getString("name")));
                        }

                        if(mCategoryArrayList.size() > 0) {
                            populateCategoryRecyclerView();
                        }else{
                            MyDialog.iPhone("Carousel Items not Found\nPlease try again!", getActivity());
                        }

                        mProgressBar.stop();

                    }catch (Exception e){
                        e.printStackTrace();
                        mProgressBar.stop();
                    }

                }else{
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

    private void populateCategoryRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.photo_rv_category);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new CategoryAdapter(mCategoryArrayList, new CategoryAdapter.ItemClickListener() {
            @Override
            public void onCategoryClick(Category mCategory) {
                Utility.setSharedPreference(getActivity(),Constant.CATEGORY_ID,mCategory.getId());
                Utility.setSharedPreference(getActivity(),Constant.PHOTO_COUNT,mCategory.getCount());
                photoGridFragment();

            }
        }));
    }

    private void photoGridFragment(){
        Fragment fragment = new GridPhotoFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void refreshPhotoList() {
        Log.e(TAG,"Refresh Call");
        getCategory();

    }
}
