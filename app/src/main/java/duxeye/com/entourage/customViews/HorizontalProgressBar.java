package duxeye.com.entourage.customViews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import duxeye.com.entourage.R;

/**
 * Created by Ondoor (Hasnain) on 7/26/2016.
 */
public class HorizontalProgressBar extends AlertDialog {
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private Button cancelButton;

    public HorizontalProgressBar(Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.custom_progress_bar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_pv_linear_colors);
        mTextView = (TextView) findViewById(R.id.timer);
        cancelButton = (Button) findViewById(R.id.btn_upload);


        int sdk = android.os.Build.VERSION.SDK_INT;
        int color = 0xFFFF0000;
        if (sdk < 21) {
            mProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            mProgressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    public void setProgressBar(int progress){
        mProgressBar.setProgress(progress);
        mTextView.setText(""+progress+"%");

    }


}
