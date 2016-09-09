package duxeye.com.entourage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.LoadImage;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.PhotoDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoDetailsFragment extends Fragment {
    private static final String TAG = PhotoDetailsFragment.class.getSimpleName();
    private View mView;
    private ViewPager mViewPager;
    private PhotoDetailPagerAdapter mPagerAdapter;
    private int selectedPosition,lastPosition = 0;
//    private boolean isFirstTime = true;
//    private ImageView penImageView;
//    private ImageViewTouch bigImageView;
//    private TextView fromTextView, imageNameTextView, sizeTextView, dateTextView;
//    private EditText captionEditText;
//    private String next_photo_id, prior_photo_id, category_photo_id;
//    private CircularProgressBar mProgressBar;
//    private RelativeLayout mPhotoLayout;
//    private ScaleGestureDetector mGestureDetector;
//    private Matrix matrix = new Matrix();
//    private float scale = 1f;

    private ArrayList<PhotoDetails> mArrayList = new ArrayList<>();

    public PhotoDetailsFragment() {
        /**
         *   Required empty public constructor
         */

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        init();

        mView.findViewById(R.id.iv_pd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoGridFragment();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (lastPosition > position) {
                    /**
                     * Left
                     */
                    Log.e(TAG, "onPageSelected Left call");
//                    if (Utility.getSharedPreferences(getActivity(), Constant.PRIOR_PHOTO_ID).equalsIgnoreCase("")) {
//                        Toast.makeText(getActivity(), "You are at 1st photo", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Utility.setSharedPreference(getActivity(), Constant.PHOTO_ID, Utility.getSharedPreferences(getActivity(), Constant.PRIOR_PHOTO_ID));

//                    }

                } else if (lastPosition < position) {
                    /**
                     * Right
                     */
                    Log.e(TAG, "onPageSelected Right call");
                    lastPosition = position;
//                    if (Utility.getSharedPreferences(getActivity(), Constant.NEXT_PHOTO_ID).equalsIgnoreCase("")) {
//                        Toast.makeText(getActivity(), "No more next", Toast.LENGTH_SHORT).show();
//                    } else {

                        Utility.setSharedPreference(getActivity(), Constant.PHOTO_ID, Utility.getSharedPreferences(getActivity(), Constant.NEXT_PHOTO_ID));
//                    }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        captionEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                captionEditText.setCursorVisible(true);
//            }
//        });
//        penImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Utility.getBoolean(getActivity(), Constant.ALLOW_CAPTION)) {
//                    if (!captionEditText.getText().toString().trim().equalsIgnoreCase("")) {
//                        captionEditText.setCursorVisible(false);
//                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                        captionEditText.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
//                        updateCaption(captionEditText.getText().toString());
//                    } else {
//                        MyDialog.iPhone("Photo caption should not be empty!", getActivity());
//                    }
////                    Log.e(TAG,"Button click");
//
//                } else {
//                    MyDialog.iPhone("You are not allowed to update photo caption!", getActivity());
//                }
//
//
//            }
//        });

//        mPhotoLayout.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
//            public void onSwipeRight() {
//                captionEditText.setCursorVisible(false);
////                Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
//                if (prior_photo_id.equalsIgnoreCase("")) {
//                    Toast.makeText(getActivity(), "You are at 1st photo", Toast.LENGTH_SHORT).show();
//                } else {
//                    String url = Constant.PHOTO_DETAILS + prior_photo_id + "&yearbook_id=" + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
////                    Log.e(TAG, "Url: " + url);
//                    new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject json, AjaxStatus status) {
////                            Log.e(TAG, "Response: " + json);
//                            if (json != null) {
//                                try {
//                                    imageNameTextView.setText(json.getString("photo_file"));
//                                    fromTextView.setText(json.getString("uploaded_by"));
//                                    captionEditText.setText(json.getString("photo_caption").trim());
//                                    sizeTextView.setText(json.getString("exphoto_width") + " * " + json.getString("exphoto_height"));
//                                    dateTextView.setText(json.getString("create_date"));
//                                    next_photo_id = json.getString("next_photo_id");
//                                    prior_photo_id = json.getString("prior_photo_id");
//                                    category_photo_id = json.getString("category_photo_id");
//                                    Picasso.with(getActivity()).load(json.getString("image").replace("[", "%5B").replace("]", "%5D")).into(bigImageView);
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            } else {
//                                if (Utility.isConnectingToInternet()) {
//                                    MyDialog.iPhone("No response from server\nPlease try again!", getActivity());
//
//                                } else {
//                                    Utility.showInternetAlert(getActivity());
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//
//            public void onSwipeLeft() {
//                captionEditText.setCursorVisible(false);
////                Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
//                if (next_photo_id.equalsIgnoreCase("")) {
//                    Toast.makeText(getActivity(), "No more next", Toast.LENGTH_SHORT).show();
//                } else {
//                    String url = Constant.PHOTO_DETAILS + next_photo_id + "&yearbook_id=" + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
////                    Log.e(TAG, "Url: " + url);
//                    new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject json, AjaxStatus status) {
////                            Log.e(TAG, "Response: " + json);
//                            if (json != null) {
//                                try {
//                                    imageNameTextView.setText(json.getString("photo_file"));
//                                    fromTextView.setText(json.getString("uploaded_by"));
//                                    captionEditText.setText(json.getString("photo_caption"));
//                                    sizeTextView.setText(json.getString("exphoto_width") + " * " + json.getString("exphoto_height"));
//                                    dateTextView.setText(json.getString("create_date"));
//                                    next_photo_id = json.getString("next_photo_id");
//                                    prior_photo_id = json.getString("prior_photo_id");
//                                    category_photo_id = json.getString("category_photo_id");
//                                    Picasso.with(getActivity()).load(json.getString("image").replace("[", "%5B").replace("]", "%5D")).into(bigImageView);
//
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            } else {
//                                if (Utility.isConnectingToInternet()) {
//                                    MyDialog.iPhone("No response from server\nPlease try again!", getActivity());
//
//                                } else {
//                                    Utility.showInternetAlert(getActivity());
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//        });

        return mView;
    }

    private void init() {
        ((TextView) mView.findViewById(R.id.pd_header_text)).setText(getActivity().getResources().getString(R.string.photo_details));
//        backButton = (ImageView) mView.findViewById(R.id.iv_pd_back);
//        bigImageView = (ImageViewTouch) mView.findViewById(R.id.iv_big_image);
//        penImageView = (ImageView) mView.findViewById(R.id.iv_pen);

//        imageNameTextView = (TextView) mView.findViewById(R.id.tv_file_name);
//        fromTextView = (TextView) mView.findViewById(R.id.tv_from);
//        sizeTextView = (TextView) mView.findViewById(R.id.tv_size);
//        dateTextView = (TextView) mView.findViewById(R.id.tv_date);

//        captionEditText = (EditText) mView.findViewById(R.id.et_caption);
//        captionEditText.setTextColor(getActivity().getResources().getColor(R.color.textColor));
//        mPhotoLayout = (RelativeLayout) mView.findViewById(R.id.relativelayout);

//        mProgressBar = new CircularProgressBar(getActivity());
//        mProgressBar.setCancelable(false);

        mViewPager = (ViewPager) mView.findViewById(R.id.vp_pd_pager);
        mPagerAdapter = new PhotoDetailPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        /**
         * Set default pager position
         */
        selectedPosition = Integer.parseInt(Utility.getSharedPreferences(getActivity(), Constant.CURRENT_PAGE_INDEX));
        mViewPager.setCurrentItem(selectedPosition);


        getUIData(Utility.getSharedPreferences(getActivity(),Constant.PHOTO_ID), selectedPosition);
//        getImageDetails();
    }

    private void photoGridFragment() {
        Fragment fragment = new GridPhotoFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    private void getImageDetails(final boolean isRight) {
//        String url = Constant.PHOTO_DETAILS + Utility.getSharedPreferences(getActivity(), Constant.PHOTO_ID) + "&yearbook_id=" + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
////        Log.e(TAG, "Url: " + url);
//        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject json, AjaxStatus status) {
////                Log.e(TAG, "Response: " + json);
//                if (json != null) {
//                    try {
////                        imageNameTextView.setText(json.getString("photo_file"));
////                        fromTextView.setText(json.getString("uploaded_by"));
////                        captionEditText.setText(json.getString("photo_caption"));
////                        sizeTextView.setText(json.getString("exphoto_width") + " * " + json.getString("exphoto_height"));
////                        dateTextView.setText(json.getString("create_date"));
////                        next_photo_id = json.getString("next_photo_id");
////                        prior_photo_id = json.getString("prior_photo_id");
////                        category_photo_id = json.getString("category_photo_id");
//
//                        if(isRight){
//                            Utility.setSharedPreference(getActivity(),Constant.PHOTO_ID, json.getString("next_photo_id"));
//                        }else{
//                            Utility.setSharedPreference(getActivity(),Constant.PHOTO_ID, json.getString("next_photo_id"));
//                        }
//
////                        Picasso.with(getActivity()).load(json.getString("image").replace("[", "%5B").replace("]", "%5D")).into(bigImageView);
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
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
//    }

//    private void updateCaption(String caption) {
//        mProgressBar.start();
//        String url = Constant.PHOTO_CAPTION_UPDATE + category_photo_id + "&caption=" + caption.replace(" ", "%20") + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
////        Log.e(TAG,"url: "+url);
//        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject json, AjaxStatus status) {
////                Log.e(TAG,"Response: "+json);
//                if (json != null) {
//                    try {
//                        if (json.getString("status").equalsIgnoreCase("SUCCESS")) {
//                            Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
//
//                        mProgressBar.stop();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        mProgressBar.stop();
//                    }
//
//                } else {
//                    mProgressBar.stop();
//                    if (Utility.isConnectingToInternet()) {
//                        MyDialog.iPhone("No response from server\nPlease try again!", getActivity());
//
//                    } else {
//                        Utility.showInternetAlert(getActivity());
//                    }
//                }
//            }
//        });
//
//    }

    public class PhotoDetailPagerAdapter extends FragmentPagerAdapter {

        public PhotoDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "Position : " + position);
//
//            if (position != selectedPosition) {
//
//            }
            /**
             * Return only blank fragment
             */
            return PhotoDetailRowFragment.newInstance();

//            } else if (currentPositio > position) {
//                currentPositio = position;
//                return PhotoDetailRowFragment.newInstance(Utility.getSharedPreferences(getActivity(), Constant.NEXT_PHOTO_ID));
//            } else if(currentPositio < position){
//                currentPositio = position;
//                return PhotoDetailRowFragment.newInstance(Utility.getSharedPreferences(getActivity(), Constant.PRIOR_PHOTO_ID));
//            }
//
//            return getUIData(Utility.getSharedPreferences(getActivity(), Constant.PRIOR_PHOTO_ID), position);
        }

        @Override
        public int getCount() {
            return Integer.parseInt(Utility.getSharedPreferences(getActivity(), Constant.PHOTO_COUNT));
        }

    }


    public void getUIData(String photoID, final int position) {
        String url = Constant.PHOTO_DETAILS + photoID + "&yearbook_id=" + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
        Log.e(TAG, "Url: " + url);
        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    try {

                        try {
                            mArrayList.get(position);
                        } catch (IndexOutOfBoundsException e) {
                            mArrayList.add(position, new PhotoDetails(
                                    json.getString("photo_file"),
                                    json.getString("uploaded_by"),
                                    json.getString("photo_caption"),
                                    json.getString("exphoto_width") + " * " + json.getString("exphoto_height"),
                                    json.getString("create_date"),
                                    json.getString("next_photo_id"),
                                    json.getString("prior_photo_id"),
                                    json.getString("category_photo_id"),
                                    json.getString("image").replace("[", "%5B").replace("]", "%5D")));
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
