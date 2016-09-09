package duxeye.com.entourage.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.rey.material.app.Dialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Action;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.CategoryDropDownAdapter;
import duxeye.com.entourage.adapter.SelectedImageAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.HorizontalProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.Category;
import duxeye.com.entourage.model.UploadFileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = UploadFragment.class.getSimpleName();
    private View mView;
    private Button createCategoryButton, selectCategoryButton, selectImageButton, uploadButton;
    private CircularProgressBar mProgressBar;
    private String selectedOption;
    private ArrayList<Category> mCategoryArrayList;
    private RecyclerView selectedImageRecyclerView;
    private ArrayList<String> imagesPathList = new ArrayList<>();
    private SelectedImageAdapter adapter;
    private String parentUUDID;
    private ImageLoader imageLoader;
//    private HorizontalProgressBar progressBar;

    private android.app.Dialog mDialog;

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_uplaod, container, false);
        Utility.saveImageArrayList(getActivity(), Constant.IMAGE_ARRAY_LIST, new ArrayList<String>());
        init();

//        initImageLoader();

        return mView;
    }

    private void init() {
        ((TextView) mView.findViewById(R.id.upload_header_text)).setText("UPLOAD PHOTO");

        createCategoryButton = (Button) mView.findViewById(R.id.btn_create_category);
        createCategoryButton.setOnClickListener(this);

        selectCategoryButton = (Button) mView.findViewById(R.id.btn_select_category);
        selectCategoryButton.setOnClickListener(this);

        selectImageButton = (Button) mView.findViewById(R.id.btn_select_image);
        selectImageButton.setOnClickListener(this);

        uploadButton = (Button) mView.findViewById(R.id.btn_upload);
        uploadButton.setOnClickListener(this);

        mProgressBar = new CircularProgressBar(getActivity());
        mProgressBar.setCancelable(false);

        selectedImageRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_selected_image);
        selectedImageRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_category:
                if (Utility.getBoolean(getActivity(), Constant.ALLOW_CREATE_CATEGORY)) {
                    final android.app.Dialog mDialog = MyDialog.createCategory("Enter Category Name", getActivity());

                    (mDialog.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            if ((((EditText) mDialog.findViewById(R.id.et_category_name)).getText().toString().trim()).equalsIgnoreCase("")) {
                                MyDialog.iPhone("Category name should not be blank!", getActivity());
                            } else {
                                mProgressBar.start();
                                String url = Constant.CREATE_CATEGORY + ((EditText) mDialog.findViewById(R.id.et_category_name)).getText().toString().trim() + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY) + "&yearbook_id=" + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID);
                                Log.e(TAG, "Url: " + url);

                                new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        Log.e(TAG, "Response: " + json);
                                        if (json != null) {
                                            try {
                                                if (json.getString("status").equalsIgnoreCase("SUCCESS")) {
                                                    MyDialog.iPhone(json.getString("message"), getActivity());
                                                } else {
                                                    MyDialog.iPhone(json.getString("message"), getActivity());
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
                        }
                    });

                    (mDialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });

                } else {
                    MyDialog.iPhone("You are not allowed to create category!", getActivity());
                }
                break;

            case R.id.btn_select_category:
                getCategory();
                break;

            case R.id.btn_select_image:
                if (isStoragePermissionGranted()) {
                    if (selectCategoryButton.getText().toString().equalsIgnoreCase("")) {
                        MyDialog.iPhone("Select category for upload photo!", getActivity());
                    } else {
                        if (Utility.getBoolean(getActivity(), Constant.ALLOW_UPLOAD)) {
                            selectImageFromGalleryFragment();
                        } else {
                            MyDialog.iPhone("You are not allowed to upload photo!", getActivity());
                        }

                    }
                } else {
                    MyDialog.iPhone("Please allow to access storage permission!", getActivity());
                }


                break;

            case R.id.btn_upload:
                if (Utility.isConnectingToInternet()) {
                    if (!selectCategoryButton.getText().toString().equalsIgnoreCase("SELECT CATEGORY")) {
                        if (imagesPathList.size() > 0) {
                            new UploadImageToAmazonServer().execute();
                        } else {
                            MyDialog.iPhone("Select at least one photo for upload!", getActivity());
                        }

                    } else {
                        MyDialog.iPhone("Select category for upload photo!", getActivity());
                    }


                } else {
                    Utility.showInternetAlert(getActivity());
                }

                break;
        }

    }

    private void selectCategorySpinner(String title) {
        final Dialog dd = new Dialog(getActivity());
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.popup_select_category);
        dd.getWindow().setLayout(-1, -2);
        dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ((TextView) dd.findViewById(R.id.tv_title)).setText(title);

        /***
         * get Recycler and populate it
         */
        final RecyclerView mRecyclerView = (RecyclerView) dd.findViewById(R.id.rv_category);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new CategoryDropDownAdapter(getActivity(), -1, mCategoryArrayList, new CategoryDropDownAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category item, int selectedPosition) {
                selectedOption = item.getName();
                Utility.setSharedPreference(getActivity(), Constant.CATEGORY_ID, item.getId());
                Log.e(TAG, "Category ID: " + item.getId());
//                stateCode = item.getId();
            }
        }));

        (dd.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
                selectCategoryButton.setText(selectedOption);

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

    private void getCategory() {
        mProgressBar.start();
        mCategoryArrayList = new ArrayList<>();
        String url = Constant.GETCATEGORY + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
        //Log.e(TAG,"Url: "+url);
        new AQuery(getActivity()).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                //Log.e(TAG,"Response: "+json);
                if (json != null) {
                    try {
                        for (int i = 0; i < json.length(); i++) {

                            mCategoryArrayList.add(new Category(
                                    json.getJSONObject(i).getString("id"),
                                    json.getJSONObject(i).getString("count"),
                                    json.getJSONObject(i).getString("icon"),
                                    json.getJSONObject(i).getString("name")));
                        }

                        if (mCategoryArrayList.size() > 0) {
                            selectCategorySpinner("Select Category");
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
    }

    private void selectImageFromGalleryFragment() {
        Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(i, 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] imagesPath = data.getStringArrayExtra("all_path");
            try {
                imagesPathList.clear();
                Utility.saveImageArrayList(getActivity(), Constant.IMAGE_ARRAY_LIST, new ArrayList<String>());
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < imagesPath.length; i++) {
                Log.e(TAG, "Uri: " + imagesPath[i]);
                imagesPathList.add(imagesPath[i]);
            }

            Utility.saveImageArrayList(getActivity(), Constant.IMAGE_ARRAY_LIST, imagesPathList);
            refreshSelectedImage();
        }
    }

    private void refreshSelectedImage() {
        adapter = new SelectedImageAdapter(getActivity(), imagesPathList, new SelectedImageAdapter.ItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                imagesPathList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        selectedImageRecyclerView.setAdapter(adapter);
    }

    class UploadImageToAmazonServer extends AsyncTask<Void, Integer, Void> {
        private ArrayList<UploadFileData> arrayList;
        private TextView mTextView;
        private Button cancelButton;
        private ProgressBar mUploadProgressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar = new HorizontalProgressBar(getActivity());
//            progressBar.setCancelable(false);
//            progressBar.show();

            mDialog = new android.app.Dialog(getActivity());
            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mDialog.setContentView(R.layout.custom_progress_bar);
            mDialog.getWindow().setLayout(-1, -2);
            mDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog.setCancelable(false);

            mTextView = (TextView) mDialog.findViewById(R.id.timer);
            cancelButton = (Button) mDialog.findViewById(R.id.btn_cancel);
            mUploadProgressBar = (ProgressBar) mDialog.findViewById(R.id.progress_pv_linear_colors);

            int sdk = android.os.Build.VERSION.SDK_INT;
            int color = 0xFFFF0000;
            if (sdk < 21) {
                mUploadProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mUploadProgressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getStatus() == AsyncTask.Status.RUNNING) {
                        cancel(true);
                    }
                }
            });

            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            File mFile = null;
            //http://stackoverflow.com/questions/25590551/how-to-implement-long-running-network-uploads-in-android-not-using-asynctask-and
            try {
                try {
                    Constant.uploadUrl.clear();
                } catch (Exception e) {

                }


                /**
                 *
                 */
                parentUUDID = UUID.randomUUID().toString();
                arrayList = new ArrayList<>();
                AmazonS3Client mAmazonS3Client = new AmazonS3Client(new BasicAWSCredentials("AKIAJ62ZNGGKUCATRYZQ", "oXzlm38axdXOu2vIgVsHRgBNQdv6l+AS9rzkgjiX"));
                String existingBucketName = "entourage-photos";
                mAmazonS3Client.createBucket("entourage-photos");

                for (int i = 0; i < imagesPathList.size(); i++) {
                    String imageUrl = imagesPathList.get(i);

                    String fileUDID = UUID.randomUUID().toString();
                    String fileName = "photos/DELIVERY_2016/YBID_" + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "/" + fileUDID + ".jpg";

//                    mFile = getResizeImage(imageUrl);
                    mFile = new File(imageUrl);
                    PutObjectRequest mPutObjectRequest = new PutObjectRequest(existingBucketName, fileName, mFile);//key is  URL

                    mPutObjectRequest.setCannedAcl(CannedAccessControlList.PublicReadWrite);
                    mAmazonS3Client.putObject(mPutObjectRequest);
                    String _finalUrl = "http://entourage-photos.s3.amazonaws.com/" + fileName;
                    //Log.e(TAG, "URL: " + _finalUrl);

                    arrayList.add(new UploadFileData(fileUDID, fileName, _finalUrl));

                    int currentProgress = (((i + 1) * 100) / imagesPathList.size());
                    Log.e(TAG, "currentProgress: " + currentProgress);
                    publishProgress(currentProgress);

                    /**
                     * delete file
                     */
//                    try{
//                        if(mFile.exists() ){
//                            mFile.delete();
//                        }
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }

                    //Check if user clicked OK in the dialog
                    if (isCancelled()) {
                        //Exit the method if the user dismissed the dialog
                        return null;
                    }

                }


                uploadUrlToEntourageServer(arrayList);

                /**
                 * Json objet send
                 * photo_guid = UDID common
                 * max 3 image at one time
                 * progrss for each image
                 */

            } catch (Exception e) {
                e.printStackTrace();
                mDialog.dismiss();

                try {
                    if (arrayList.size() > 0) {
                        uploadUrlToEntourageServer(arrayList);
                    }
                } catch (Exception ex) {

                }

            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            try {
                if (arrayList.size() > 0) {
                    uploadUrlToEntourageServer(arrayList);
                }
            } catch (Exception e) {

            }

            mDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mUploadProgressBar.setProgress(progress[0]);
            mTextView.setText("" + progress[0] + "%");
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

        }
    }

//    private File getResizeImage(String photoPath) {
//        File mFile = null;
//        try {
//            /**
//             * Convert image path to Bitmap
//             */
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//            /**
//             * Re-size bitmap
//             */
//            Bitmap resized = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(photoPath, options), (int) (BitmapFactory.decodeFile(photoPath, options).getWidth() * 0.8), (int) (BitmapFactory.decodeFile(photoPath, options).getHeight() * 0.8), true);
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            resized.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//
//            //Create a new file name "test.jpg"
//            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+"Entourage");
//            if (!mediaStorageDir.exists()) {
//                if (!mediaStorageDir.mkdirs()) {
//                    Log.e(TAG, "failed to create directory");
//                } else {
//                    mediaStorageDir.mkdir();
//                    Log.e(TAG, mediaStorageDir + " Directory created...");
//                }
//            }
//
//
//           mFile = new File(mediaStorageDir+File.separator+"test.jpg");
//            mFile.createNewFile();
//            //write the bytes in file
//            FileOutputStream fo = new FileOutputStream(mFile);
//            fo.write(bytes.toByteArray());
//
//            // Close FileOutput
//            fo.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return mFile;
//    }

    private void uploadUrlToEntourageServer(ArrayList<UploadFileData> arrayList) {
        try {

            JSONObject finalJson = new JSONObject();
            JSONArray mJsonArray = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject successJsonObject = new JSONObject();
                JSONObject urlJsonObject = new JSONObject();
                urlJsonObject.put("filename", arrayList.get(i).getFileName());
                urlJsonObject.put("fileurl", arrayList.get(i).getImageUrl());
                successJsonObject.put("success", urlJsonObject);
                mJsonArray.put(successJsonObject);
                finalJson.put("array", mJsonArray);

               /* if(((i+1)%3) == 0){
                    Log.e(TAG, i + " : " + mJsonArray);
                    mJsonArray = new JSONArray();
                    temp =arrayList.size()-3;
                    for(int k=0;k<mJsonArray.length();k++)
                    {
                        mJsonArray.remove(k);
                    }
                }else
                {
                    if(temp<3)
                    {
                        Log.e(TAG, i + " : " + mJsonArray);
                        mJsonArray = new JSONArray();
                    }
                }*/
            }

            uploadItToEntourage(URLEncoder.encode(finalJson.toString(), "UTF-8"));
            String result = java.net.URLDecoder.decode(URLEncoder.encode(finalJson.toString(), "UTF-8"), "UTF-8");
            Log.e(TAG, "Decode: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadItToEntourage(String imageUrl) {
        String url = Constant.UPLOAD_IMAGE_URL + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&category_id=" + Utility.getSharedPreferences(getActivity(), Constant.CATEGORY_ID) + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY) + "&photo_guid=" + parentUUDID + "&js=" + imageUrl;
//        Log.e(TAG, "Url: " + url);
        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
//                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    try {
                        if (json.getString("status").equalsIgnoreCase("SUCCESS")) {
                            imagesPathList.clear();
                            adapter.notifyDataSetChanged();
                            selectCategoryButton.setText("");

                            /**
                             * Get uploaded photo URL
                             */
                            getPhotoList(json.getString("category_id"), json.getString("photo_guid"));

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

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }

    }

    private void getPhotoList(String category_id, String photo_guid) {
        mProgressBar.start();
        String url = Constant.LIST_CATEGORY_PHOTO + Utility.getSharedPreferences(getActivity(), Constant.YEARBOOKID) + "&category_id=" + category_id + "&photo_guid=" + photo_guid + "&credential_key=" + Utility.getSharedPreferences(getActivity(), Constant.CREDENTIALKEY);
        Log.e(TAG, "Url: " + url);
        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.e(TAG, "Response: " + json);
                if (json != null) {
                    try {
                        JSONArray mJsonArray = json.getJSONArray("photo_list");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            Constant.uploadUrl.add(mJsonArray.getJSONObject(i).getString("image"));
                        }

                        /**
                         * Update photo list as well
                         */
                        mDialog.dismiss();

                        Fragment fragment = new UploadedCompletedFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.upload_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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

//    private void initImageLoader() {
//        try {
//            String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.temp_tmp";
//            new File(CACHE_DIR).mkdirs();
//
//            File cacheDir = StorageUtils.getOwnCacheDirectory(getActivity().getBaseContext(),CACHE_DIR);
//
//            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                    .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
//                    .bitmapConfig(Bitmap.Config.RGB_565).build();
//
//            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getActivity().getBaseContext())
//                    .defaultDisplayImageOptions(defaultOptions)
//                    .discCache(new UnlimitedDiscCache(cacheDir))
//                    .memoryCache(new WeakMemoryCache());
//
//            ImageLoaderConfiguration config = builder.build();
//            imageLoader = ImageLoader.getInstance();
//            imageLoader.init(config);
//
//        } catch (Exception e) {
//
//        }
//    }
}
