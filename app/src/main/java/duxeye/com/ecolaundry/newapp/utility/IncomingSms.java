package duxeye.com.ecolaundry.newapp.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import duxeye.com.ecolaundry.newapp.constant.Constant;

public class IncomingSms extends BroadcastReceiver {
    private static final String TAG = IncomingSms.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                try{
                    Object[] mObjects = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < mObjects.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) mObjects[i]);
                        String message = currentMessage.getDisplayMessageBody();
                        String messageFinal = currentMessage.getDisplayMessageBody();

                        String otp = message.replace("Hello ", "").replace(" Welcome to ECO Laundry your otp Code is ","").replace(Utility.getStringSharedPreferences(context,Constant.NAME),"").trim();
//                        Log.e(TAG,"OTP: "+otp);

                        Utility.setStringSharedPreference(context, Constant.MESSAGE, otp);

                        if(messageFinal.contains(Utility.getStringSharedPreferences(context,Constant.NAME))) {
                            LocalBroadcastManager.getInstance(context).sendBroadcast (new Intent ("MESSAGE SENT"));
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG,"Error in smsReceiver: "+e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }
    }    
}