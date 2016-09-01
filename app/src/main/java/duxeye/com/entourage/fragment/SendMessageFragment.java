package duxeye.com.entourage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.rey.material.app.Dialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.CategoryDropDownAdapter;
import duxeye.com.entourage.adapter.UserTypeDropDownAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CircularProgressBar;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.model.Carousel;
import duxeye.com.entourage.model.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessageFragment extends Fragment {
    private static final String TAG = SendMessageFragment.class.getSimpleName();
    private View mView;
    private TextView cancelTextView,postTextView,sendMessageToTextView;
    private String selectedOption,userMessage;
    private ImageView spinnerButton;
    private ArrayList<String> mUserTypeArrayList = new ArrayList<>();
    private CircularProgressBar mProgressBar;
    private EditText userMessageEditText;

    public SendMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_send_message, container, false);
       init();
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(Utility.isConnectingToInternet()){
                  goBack();
              }else{
                   Utility.showInternetAlert(getActivity());
                }
            }
        });

        postTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMessage = userMessageEditText.getText().toString().trim();
                if(userMessage.equalsIgnoreCase("")){
                    MyDialog.iPhone("User message should not be blank!",getActivity());
                } else{

                    if(Utility.isConnectingToInternet()){

                        if (Utility.getBoolean(getActivity(), Constant.ALLOW_POST_MESSAGE)) {

                            postMessage();

                        } else {
                            MyDialog.iPhone("You are not allowed to message!", getActivity());
                        }

                    } else{
                        Utility.showInternetAlert(getActivity());
                    }

                }

            }
        });


        spinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserTypeSpinner("Select user type!");
            }
        });

        return mView;
    }

    private void init(){
        cancelTextView = (TextView) mView.findViewById(R.id.textView_cancel);
        postTextView = (TextView) mView.findViewById(R.id.textView_post);
        userMessageEditText = (EditText) mView.findViewById(R.id.et_user_message);
        sendMessageToTextView = (TextView) mView.findViewById(R.id.tv_message_to);
        spinnerButton = (ImageView) mView.findViewById(R.id.iv_spinner);
        mProgressBar = new CircularProgressBar(getActivity());
        mProgressBar.setCancelable(false);
        mUserTypeArrayList.add("EVERYONE");
        mUserTypeArrayList.add("STAFF");
    }

    private void selectUserTypeSpinner(String title) {
        final Dialog dd = new Dialog(getActivity());
        dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dd.setContentView(R.layout.popup_select_user_type);
        dd.getWindow().setLayout(-1, -2);
        dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ((TextView) dd.findViewById(R.id.tv_title)).setText(title);

        /***
         * get Recycler and populate it
         */
        final RecyclerView mRecyclerView = (RecyclerView) dd.findViewById(R.id.rv_category);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new UserTypeDropDownAdapter(getActivity(), -1, mUserTypeArrayList, new UserTypeDropDownAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int selectedPosition) {
                selectedOption = mUserTypeArrayList.get(selectedPosition);
            }
        }));

        (dd.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.dismiss();
                sendMessageToTextView.setText(selectedOption);

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

    private void goBack(){
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void postMessage(){
        String url = Constant.POST_MESSAGE+ Utility.getSharedPreferences(getActivity(),Constant.YEARBOOKID)+"&message_type=MESSAGE&message="+userMessage+"&credential_key="+Utility.getSharedPreferences(getActivity(),Constant.CREDENTIALKEY)+"&audience="+selectedOption;
        //Log.e(TAG,"Url: "+url);

        new AQuery(getActivity()).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
               // Log.e(TAG,"Response: "+json);
                if(json != null){
                    try{
                        if(json.getString("status").equalsIgnoreCase("SUCCESS")){

                            Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();

                            goBack();

                        } else{
                            MyDialog.iPhone(json.getString("message"), getActivity());
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
}
