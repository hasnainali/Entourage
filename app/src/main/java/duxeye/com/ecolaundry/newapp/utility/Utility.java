package duxeye.com.ecolaundry.newapp.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import duxeye.com.ecolaundry.newapp.dao.ServicesDAO;
import duxeye.com.ecolaundry.newapp.R;

/**
 * Created by Duxeye on 17-11-2015.
 */
public class Utility {
    private static final String TAG = Utility.class.getSimpleName();
    private static final String PREF_NAME = "eco_pref";

    public static ArrayList<ServicesDAO> getServicesContent() {
        ArrayList<ServicesDAO> arrayList = new ArrayList<>();
        try {
            for (int i=0; i< HardCodeContent.TITLE.length; i++){
                String title = HardCodeContent.TITLE[i].toString();
                String desc = HardCodeContent.DESCRIPTION[i].toString();
                int id = HardCodeContent.THUMB_IMAGE[i];
                arrayList.add(new ServicesDAO(title,desc,id));
            }
        }catch (Exception e){
            Log.e(TAG,"Error in getServicesContent: "+e);
        }
        return arrayList;
    }




    public static void showToast(Activity mContext,String msg){
        LayoutInflater inflater = mContext.getLayoutInflater();
        // Inflate the Layout
        View layout = inflater.inflate(R.layout.my_custom_toast,(ViewGroup) mContext.findViewById(R.id.custom_toast_layout));
        TextView text = (TextView) layout.findViewById(R.id.textToShow);
        text.setText(msg);

        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


    public static void setStringSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getStringSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        return settings.getString(name, "");
    }

    public static void setIntegerSharedPreference(Context context, String name, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }

    public static int getIngerSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        return settings.getInt(name, 0);
    }


    public static void setBooleanSharedPreference(Context context, String name, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static boolean getBooleanSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        return settings.getBoolean(name, false);
    }


    public static void editSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static Dialog iPhone(String title, Context context) {
        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_iphone);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((TextView) dd.findViewById(R.id.title)).setText(title);
            ((TextView) dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });
            dd.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dd;
    }

}
