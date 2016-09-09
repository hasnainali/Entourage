package duxeye.com.entourage.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class YearbookRowFragment extends Fragment {
    private static final String TAG = YearbookRowFragment.class.getSimpleName();
    private TextView headingTextView;
    private ImageViewTouch mImageView;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.row_year_book_pager, container, false);
        init();

        setImageAndText(Integer.parseInt(getArguments().getString("position")));

        mImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);

        return mView;
    }

    private void init() {
        headingTextView = (TextView) mView.findViewById(R.id.tv_ybd_name);
        headingTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/CaviarDreams_Bold.ttf"));

        mImageView = (ImageViewTouch) mView.findViewById(R.id.iv_yb_big_image);
    }

    public static YearbookRowFragment newInstance(String position) {
        YearbookRowFragment mYearbookRowFragment = new YearbookRowFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("position", position);
        mYearbookRowFragment.setArguments(mBundle);

        return mYearbookRowFragment;
    }


    public void setImageAndText(int position) {
//        Log.e(TAG, "getImageUrl: " + position);
        String url = Constant.PAGE_INFORMATION + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&page=" + position + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
//        Log.e(TAG, "Url: " + url);
        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
//                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    try {
                        if (json.has("status")) {
                            if (json.getString("status").equalsIgnoreCase("ERROR")) {
                                MyDialog.iPhone(json.getString("message"), getActivity());
                            }
                        } else {
                            headingTextView.setText(json.getString("page_title"));
                            LoadImage.load(getActivity(), json.getString("page_url").replace("[", "%5B").replace("]", "%5D"), mImageView);
                        }
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
