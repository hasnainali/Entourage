package duxeye.com.ecolaundry.newapp.utility;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by User on 01-12-2015.
 */
public class Util {
    private static final String TAG = Util.class.getSimpleName();

    public static float convertDpToPixel(float dp , Context mContext){
        try{
            Resources mResources = mContext.getResources();
            DisplayMetrics displayMetrics = mResources.getDisplayMetrics();
            float pixcel = dp * (displayMetrics.densityDpi) / 160.0f;
            return pixcel;
        } catch(Exception e) {
            Log.e(TAG,"Error on convertDpToPixel: "+e);
            e.printStackTrace();
        }

        return 0.0f;
    }

    public static float convertPixelToDp(float px , Context mContext){
        try{
            Resources mResources = mContext.getResources();
            DisplayMetrics displayMetrics = mResources.getDisplayMetrics();
            float dp = px / (displayMetrics.densityDpi / 160.0F);
            return dp;
        } catch(Exception e) {
            Log.e(TAG,"Error on convertPixelToDp: "+e);
            e.printStackTrace();
        }

        return 0.0f;
    }

    public static String logDisplayMatrices(Context mContext){
        try{
            Resources mResources = mContext.getResources();
            DisplayMetrics displayMetrics = mResources.getDisplayMetrics();
            return displayMetrics.toString() + "\ndensityDpi: " + displayMetrics.densityDpi;
        } catch(Exception e) {
            Log.e(TAG,"Error on logDisplayMatrices: "+e);
            e.printStackTrace();
        }

        return "";
    }
}
