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

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.UploadCompletedGridAdapter;
import duxeye.com.entourage.constant.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadedCompletedFragment extends Fragment {
    private static final String TAG = UploadedCompletedFragment.class.getSimpleName();
    private View mView;
    private ArrayList<String> urlArrayList;
    private ImageView backButton;

    public UploadedCompletedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_upload_completed, container, false);
        Utility.setBoolean(getActivity(), Constant.isBackFromImageUploadDetails,true);
        backButton = (ImageView) mView.findViewById(R.id.iv_upc_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UploadFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.upload_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        mView.findViewById(R.id.iv_upc_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UploadFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.upload_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Utility.saveImageArrayList(getActivity(),Constant.IMAGE_ARRAY_LIST,new ArrayList<String>());
        populatePhotoGrid();
        return mView;
    }

    private void populatePhotoGrid(){
        RecyclerView photoGridView = (RecyclerView) mView.findViewById(R.id.rv_upload_competeted);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),3);
        photoGridView.setLayoutManager(mLayoutManager);
        photoGridView.setItemAnimator(new DefaultItemAnimator());
        photoGridView.setAdapter(new UploadCompletedGridAdapter(getActivity(),Constant.uploadUrl, new UploadCompletedGridAdapter.ItemClickListener() {
            @Override
            public void onPhotoClick(String url) {
                Log.e(TAG,"Url: "+url);

            }
        }));
    }
}
