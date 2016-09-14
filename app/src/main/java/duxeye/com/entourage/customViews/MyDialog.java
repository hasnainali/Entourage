package duxeye.com.entourage.customViews;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import duxeye.com.entourage.R;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by win8 on 9/11/2015.
 */
public class MyDialog {
    public static Dialog iPhone(String title, Context context) {
        final Dialog dd = new Dialog(context);
        try {

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_iphone);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((TextView) dd.findViewById(R.id.title)).setText(title);
            (dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
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

    public static Dialog logout(String title, Context context) {
        final Dialog dd = new Dialog(context);
        try {

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_logout);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((TextView) dd.findViewById(R.id.title)).setText(title);
            (dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
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


    public static Dialog zoomImage(Context context,ImageViewTouch mImageViewTouch) {
        final Dialog dd = new Dialog(context);
        try {

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(mImageViewTouch);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            //ImageViewTouch mImageViewTouch = (ImageViewTouch) dd.findViewById(R.id.iv_big_image);

//            dd.findViewById(R.id.iv_big_image).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dd.dismiss();
//                }
//            });
            dd.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dd;
    }

    //    public static Dialog tAndCDialog(Context context){
//        final Dialog dd = new Dialog(context);
//        final ProgressView mProgressBar;
//        WebView mWebView;
//        try {
//            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            dd.setContentView(R.layout.dialog_t_n_c);
//            dd.getWindow().setLayout (-1, -2);
//            dd.getWindow ().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            mProgressBar = (ProgressView)dd.findViewById(R.id.pv_mm);
//            mProgressBar.start();
//
//            /**
//             * Load Terms and Condition
//             */
//            mWebView = (WebView)dd.findViewById(R.id.webView);
//            mWebView.loadUrl(Constant.TNC);
//            mWebView.setWebViewClient(new WebViewClient(){
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//
//                    mProgressBar.stop();
//                }
//            });
//
//            (dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dd.dismiss();
//                }
//            });
//            dd.show();
//
//        }catch (Exception e){e.printStackTrace();}
//        return dd;
//    }
//
    public static Dialog appExitDialog(String title, Context context) {
        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.app_exit_dialog);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((CustomTextView) dd.findViewById(R.id.title)).setText(title);
            (dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
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

    public static Dialog createCategory(String title, Context context) {
        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_create_category);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((CustomTextView) dd.findViewById(R.id.title)).setText(title);
            (dd.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                }
            });

            (dd.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
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

//    public static Dialog createPoolDialog(String title, Context context){
//        final Dialog dd = new Dialog(context);
//        try {
//            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            dd.setContentView(R.layout.dialog_create_poll);
//            dd.getWindow().setLayout(-1, -2);
//            dd.getWindow ().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            ((CustomTextView)dd.findViewById(R.id.title)).setText(title);
//            (dd.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dd.dismiss();
//                }
//            });
//
//            (dd.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dd.dismiss();
//                }
//            });
//            dd.show();
//
//        }catch (Exception e){e.printStackTrace();}
//        return dd;
//    }

}


