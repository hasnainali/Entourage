package duxeye.com.entourage.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import duxeye.com.entourage.amazone.ScalingUtilities;

/**
 * Created by (Hasnain) on 6/22/2016.
 */
public class Utility {
    private static String ENTOURAGE_DATA = "ENTOURAGE_DATA";

    public static boolean isConnectingToInternet() {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(2000, TimeUnit.MILLISECONDS);
            future.cancel(true);

        } catch (Exception e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }

    public static void setSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(ENTOURAGE_DATA, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(ENTOURAGE_DATA, 0);
        return settings.getString(name, "");
    }

    public static void setBoolean(Context context, String name, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(ENTOURAGE_DATA, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(ENTOURAGE_DATA, 0);
        return settings.getBoolean(name, false);
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile (EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static void showInternetAlert(Activity mActivity) {
        new SweetAlertDialog(mActivity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("No internet connection!\nTry again")
                .show();
    }

    public static void clearSharedPreference(Context context){
        SharedPreferences settings = context.getSharedPreferences(ENTOURAGE_DATA, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().apply();

    }

    public static ArrayList<String> getImageArrayList(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(ENTOURAGE_DATA, 0);
        Gson gson = new Gson();
        String json = settings.getString(name, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return  gson.fromJson(json, type);
    }

    public static void saveImageArrayList(Context context, String name, ArrayList<String> filterArrayList) {
        SharedPreferences settings = context.getSharedPreferences(ENTOURAGE_DATA, 0);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(filterArrayList);
        editor.putString(name, json);
        editor.commit();
    }



    public static JSONArray removeJsonObjectAtJsonArrayIndex(JSONArray source) throws JSONException {
      /*  if (index < 0 || index > source.length() - 1) {
            throw new IndexOutOfBoundsException();
        }*/

        final JSONArray copy = new JSONArray();
        return copy;
    }


    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }

}
