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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.LoadImage;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.MyDialog;
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
    private String next_photo_id, prior_photo_id, category_photo_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.row_photo_details, container, false);
        init();

        setImageAndText(getArguments().getString("photoID"));

        return mView;
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

    public static PhotoDetailRowFragment newInstance(String photoID) {
        PhotoDetailRowFragment mPhotoDetailRowFragment = new PhotoDetailRowFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("photoID", photoID);
        mPhotoDetailRowFragment.setArguments(mBundle);

        return mPhotoDetailRowFragment;
    }


    public void setImageAndText(String photoID) {
        String url = Constant.PHOTO_DETAILS + photoID + "&yearbook_id=" + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
//        Log.e(TAG, "Url: " + url);
        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
//                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    try {
                        imageNameTextView.setText(json.getString("photo_file"));
                        fromTextView.setText(json.getString("uploaded_by"));
                        captionEditText.setText(json.getString("photo_caption"));
                        sizeTextView.setText(json.getString("exphoto_width") + " * " + json.getString("exphoto_height"));
                        dateTextView.setText(json.getString("create_date"));
                        next_photo_id = json.getString("next_photo_id");
                        prior_photo_id = json.getString("prior_photo_id");
                        category_photo_id = json.getString("category_photo_id");
                        LoadImage.load(getActivity(),json.getString("image").replace("[", "%5B").replace("]", "%5D"),bigImageView);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
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
