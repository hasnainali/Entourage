package duxeye.com.entourage.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.LoadImage;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.PhotoDetails;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Created by Ondoor (Hasnain) on 8/30/2016.
 */
public class PhotoDetailRowFragment extends Fragment {
    private static final String TAG = PhotoDetailRowFragment.class.getSimpleName();
    private ImageView penImageView;
    private ImageViewTouch bigImageView;
    private TextView fromTextView, imageNameTextView, sizeTextView, dateTextView;
    private EditText captionEditText;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.row_photo_details, container, false);
        init();



        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG,"setUserVisibleHint call");
        if (isVisibleToUser && isResumed()){
            //updateUI();
//            if(Utility.getSharedPreferences(getActivity(), Constant.PHOTO_ID).equalsIgnoreCase("")){
////                Toast.makeText(getActivity(), "No more", Toast.LENGTH_SHORT).show();
//            } else{
               // updateUI();
//            }

        }

    }

    private void init() {
        imageNameTextView = (TextView) mView.findViewById(R.id.tv_file_name);
        fromTextView = (TextView) mView.findViewById(R.id.tv_from);
        sizeTextView = (TextView) mView.findViewById(R.id.tv_size);
        dateTextView = (TextView) mView.findViewById(R.id.tv_date);
        penImageView = (ImageView) mView.findViewById(R.id.iv_pen);
        bigImageView = (ImageViewTouch) mView.findViewById(R.id.iv_big_image);
        captionEditText = (EditText) mView.findViewById(R.id.et_caption);
        captionEditText.setTextColor(getActivity().getResources().getColor(R.color.textColor));

    }

    public static PhotoDetailRowFragment newInstance() {
        //Log.e(TAG,"Photo Id: "+photoID);
//        PhotoDetailRowFragment mPhotoDetailRowFragment = new PhotoDetailRowFragment();
//        Bundle mBundle = new Bundle();
//        mBundle.putSerializable("photoID", mArrayList.get(position));
//        mPhotoDetailRowFragment.setArguments(mBundle);

        return  new PhotoDetailRowFragment();
    }


    public void updateUI(PhotoDetails mPhotoDetails) {
//        String url = Constant.PHOTO_DETAILS + photoID + "&yearbook_id=" + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
//        Log.e(TAG, "Url: " + url);
//        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject json, AjaxStatus status) {
//                Log.e(TAG, "Response: " + json);
//                if (json != null) {
                    try {
                        imageNameTextView.setText(mPhotoDetails.getImageName());
                        fromTextView.setText(mPhotoDetails.getUploadedBy());
                        captionEditText.setText(mPhotoDetails.getPhotoCaption());
                        sizeTextView.setText(mPhotoDetails.getSize());
                        dateTextView.setText(mPhotoDetails.getDate());
                        LoadImage.load(getActivity(),mPhotoDetails.getImageUrl(),bigImageView);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                } else {
//                    if (Utility.isConnectingToInternet()) {
//                        MyDialog.iPhone("No response from server\nPlease try again!", getActivity());
//
//                    } else {
//                        Utility.showInternetAlert(getActivity());
//                    }
//                }
//            }
//        });
    }
}
