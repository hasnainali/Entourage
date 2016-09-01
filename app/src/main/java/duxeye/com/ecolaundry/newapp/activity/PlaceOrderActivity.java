package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;

import duxeye.com.ecolaundry.newapp.constant.Constant;
import duxeye.com.ecolaundry.newapp.customView.CircularProgressBar;
import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.utility.MyUrl;
import duxeye.com.ecolaundry.newapp.utility.Utility;

public class PlaceOrderActivity extends Activity {
    private static final String TAG = PlaceOrderActivity.class.getSimpleName();
    private HashMap<Integer, Integer> hashMap;
    private LinearLayout nowLinearLayout, laterLinearLayout;
    private TextView nowTextView, nowSubTextView, laterTextView, laterSubTextView;
    private RelativeLayout placeButton;
    private RadioGroup radioGroup;
    private int hour;
    private String selectedDay, selectedTime;
    private boolean isChangeClick;
    private CircularProgressBar mProgressBar;
    private  Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        mActivity = this;
        init();

        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceOrderActivity.this.finish();
            }
        });

        nowLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorApp));
                nowTextView.setTextColor(Color.WHITE);
                nowSubTextView.setTextColor(Color.WHITE);

                laterLinearLayout.setBackgroundColor(Color.WHITE);

                laterTextView.setTextColor(Color.BLACK);
                laterTextView.setText("Later");

                laterSubTextView.setTextColor(Color.parseColor("#3C4049"));
                laterSubTextView.setText("Choose a time slot");

                setDefaultTimeSlot();

            }
        });

        laterLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laterLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorApp));
                laterTextView.setTextColor(Color.WHITE);
                laterSubTextView.setTextColor(Color.WHITE);

                nowLinearLayout.setBackgroundColor(Color.WHITE);
                nowTextView.setTextColor(Color.BLACK);

                nowSubTextView.setTextColor(Color.parseColor("#3C4049"));

                selectedDay = "na";
                selectedTime = "na";

                openPopUp();
            }
        });

        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                String time = today.format("%k:%M:%S");
                int hh = Integer.parseInt(time.split(":")[0].toString().trim());

//                MyHandler.showError(TAG, "Selected day: " + selectedDay);
//                MyHandler.showError(TAG, "Selected time: " + selectedTime);

                if (selectedDay.equalsIgnoreCase("Now")) {
                    if (hh > 21 || hh < 9) {
                        AlertDialog alertDialog = new AlertDialog.Builder(PlaceOrderActivity.this).create();
                        alertDialog.setTitle("Opps!");
                        alertDialog.setMessage("Choose other time slot for pickup\nThis facility is available in working hour\n09:00:00 AM to 09:00:00 PM");
                        alertDialog.setIcon((false) ? R.drawable.success : R.drawable.fail);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //PlaceOrderActivity.this.finish();
                            }
                        });
                        alertDialog.show();

                    } else {
                        try {
                            final Dialog dd = new Dialog(PlaceOrderActivity.this);
                            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dd.setContentView(R.layout.edit_address_dialog);
                            dd.setCancelable(false);
                            dd.getWindow().setLayout(-1, -2);
                            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            final EditText tempAddressEditText = (EditText) dd.findViewById(R.id.edt_temp_address);
                            tempAddressEditText.setVisibility(View.GONE);
                            ((TextView)dd.findViewById(R.id.tv_stored_address)).setText(Utility.getStringSharedPreferences(getApplicationContext(), Constant.PICK_UP_ADD));
                            isChangeClick = false;

                            (dd.findViewById(R.id.tv_ok_button)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        dd.dismiss();
                                        if (isChangeClick) {
                                            placeOrder(tempAddressEditText.getText().toString().trim());
                                        } else {
                                            placeOrder("");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            (dd.findViewById(R.id.tv_change_button)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   //dd.dismiss();
                                    tempAddressEditText.setVisibility(View.VISIBLE);
                                    isChangeClick = true;
                                }
                            });

                            dd.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    if (selectedDay.equalsIgnoreCase("Tomorrow") || selectedDay.equalsIgnoreCase("Today")){
                        if(selectedTime.equalsIgnoreCase("na")) {
                            Utility.showToast(PlaceOrderActivity.this,"Please select at lest one time slot");
                        }else{
                            try {
                                final Dialog dd = new Dialog(PlaceOrderActivity.this);
                                dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dd.setContentView(R.layout.edit_address_dialog);
                                dd.setCancelable(false);
                                dd.getWindow().setLayout(-1, -2);
                                dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                final EditText tempAddressEditText = (EditText) dd.findViewById(R.id.edt_temp_address);
                                tempAddressEditText.setVisibility(View.GONE);
                                ((TextView)dd.findViewById(R.id.tv_stored_address)).setText(Utility.getStringSharedPreferences(getApplicationContext(), Constant.PICK_UP_ADD));
                                isChangeClick = false;

                                (dd.findViewById(R.id.tv_ok_button)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            dd.dismiss();
                                            if (isChangeClick) {
                                                placeOrder(tempAddressEditText.getText().toString().trim());
                                            } else {
                                                placeOrder("");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                (dd.findViewById(R.id.tv_change_button)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //dd.dismiss();
                                        tempAddressEditText.setVisibility(View.VISIBLE);
                                        isChangeClick = true;
                                    }
                                });

                                dd.show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        });

    }

    private void init() {
        setDefaultTimeSlot();
        nowLinearLayout = (LinearLayout) findViewById(R.id.ll_now);
        nowLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorApp));

        nowTextView = (TextView) findViewById(R.id.tv_now);
        nowTextView.setTextColor(Color.WHITE);

        nowSubTextView = (TextView) findViewById(R.id.tv_now_sub);
        nowSubTextView.setTextColor(Color.WHITE);
        nowSubTextView.setTypeface(nowSubTextView.getTypeface(), Typeface.ITALIC);

        laterLinearLayout = (LinearLayout) findViewById(R.id.ll_today);
        laterLinearLayout.setBackgroundColor(Color.WHITE);

        laterTextView = (TextView) findViewById(R.id.tv_today);
        laterTextView.setTextColor(Color.BLACK);

        laterSubTextView = (TextView) findViewById(R.id.tv_today_sub);
        laterSubTextView.setTextColor(Color.parseColor("#3C4049"));
        laterSubTextView.setTypeface(laterSubTextView.getTypeface(), Typeface.ITALIC);

        placeButton = (RelativeLayout) findViewById(R.id.rl_footer);

        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);

    }

    private void openPopUp() {
        int startTime = 9;
        hashMap = new HashMap<>();
        for (int i = 1; i < 16; i++) {
            hashMap.put(startTime, i);
            startTime += 1;
        }

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        hour = Integer.parseInt(today.format("%k:%M:%S").split(":")[0].toString().trim());

        final Dialog dialog = new Dialog(PlaceOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(false);
        dialog.show();

        Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
        Button setButton = (Button) dialog.findViewById(R.id.btn_set);

        radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup1);
        final RadioButton todayRadioButton = (RadioButton) dialog.findViewById(R.id.rb_today);
        final RadioButton tomorrowRadioButton = (RadioButton) dialog.findViewById(R.id.rb_tomorrow);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todayRadioButton.isChecked()) {
                    selectedDay = "Today";
                } else {
                    selectedDay = "Tomorrow";
                }
                laterTextView.setText(selectedDay);
                //Log.i(TAG,"Selected day = "+selectedDay);
                dialog.dismiss();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int selectedId = radioGroup.indexOfChild(radioButton);
                RadioButton radioTimeButton = (RadioButton) group.getChildAt(selectedId);
                selectedTime = (String) radioTimeButton.getText();
                laterSubTextView.setText(selectedTime);
                //Log.i(TAG,"Selected Time = "+selectedTime);
            }
        });

        tomorrowRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    resetRadioButtonView();
                    todayRadioButton.setChecked(false);
                }
            }
        });

        todayRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeRadioButtonView();
                    tomorrowRadioButton.setChecked(false);
                }
            }
        });

        if (hour < 9) {
            resetRadioButtonView();
        } else if (hour > 19) {
            hour = 22;
            changeRadioButtonView();
        } else {
            changeRadioButtonView();
        }
        // Log.i(TAG, "Current hour: " + hour);
    }


    @Override
    public void onBackPressed() {

    }

    private void changeRadioButtonView() {
        int currentTime = hashMap.get(hour);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (i < currentTime) {
                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(getResources().getColor(R.color.colorTextOff));
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
                ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
            }
        }
    }


    private void resetRadioButtonView() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setTextColor(getResources().getColor(R.color.colorTextOn));
            ((RadioButton) radioGroup.getChildAt(i)).setEnabled(true);
            ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
        }
    }


    private void setDefaultTimeSlot() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String time = today.format("%k:%M:%S");

        selectedDay = "Now";
        selectedTime = time + " to " + (Integer.parseInt(time.split(":")[0].toString().trim()) + 1) + ":" + time.split(":")[1].toString().trim() + ":" + time.split(":")[2].toString().trim();

        hour = Integer.parseInt(today.format("%k:%M:%S").split(":")[0].toString().trim());

    }


    private void placeOrder(String address) {
        try {
            if (Utility.isConnectingToInternet(PlaceOrderActivity.this)) {
                mProgressBar.start();
                if(address.equalsIgnoreCase("")){
                    address = Utility.getStringSharedPreferences(getApplicationContext(), Constant.PICK_UP_ADD);
                }
                String url = (MyUrl.PLACE_ORDER_URL+Utility.getStringSharedPreferences(getApplicationContext(), Constant.USER_NAME)+"&mobile="+Utility.getStringSharedPreferences(getApplicationContext(), Constant.MOBILE)+"&pickupaddress="+address+"&time="+selectedTime+"&day="+selectedDay).replace(" ","%20");
                Log.e(TAG, "Url: " + url);


                new AQuery(mActivity).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        Log.e(TAG, "Response: " + json);
                        if (json != null) {
                            try {
                                if(json.getString("Succesfull").equalsIgnoreCase("true")) {

                                    Utility.showToast(mActivity,json.getString("message"));

                                   PlaceOrderActivity.this.finish();

                                }else{
                                    Utility.iPhone(json.getString("message")+"\nPlease try again!", mActivity);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mProgressBar.stop();
                            //startSlider();

                        } else {
                            mProgressBar.stop();
                            if (Utility.isConnectingToInternet(mActivity)) {
                                Utility.iPhone("Please try again!", mActivity);

                            } else {
                                Utility.iPhone("No internet connection", mActivity);
                            }
                        }

                        mProgressBar.stop();
                    }
                });


            } else {
                Utility.iPhone("No internet connection", PlaceOrderActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error in addressConformationPopup");
        }

    }
}
