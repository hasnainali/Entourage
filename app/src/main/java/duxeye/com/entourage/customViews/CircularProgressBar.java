package duxeye.com.entourage.customViews;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.RelativeLayout;

import duxeye.com.entourage.R;


/**
 * Created by Ondoor (Hasnain) on 5/25/2016.
 */
public class CircularProgressBar {
    private Dialog dd;

    public CircularProgressBar(Context context) {
        dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.getWindow().setDimAmount(0);
            dd.setContentView(R.layout.circulor_progress_layout);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setCancelable(boolean cancelable) {
        dd.setCancelable(cancelable);

    }

    public void start() {
        dd.show();

    }

    public void stop() {
        dd.dismiss();

    }
}
