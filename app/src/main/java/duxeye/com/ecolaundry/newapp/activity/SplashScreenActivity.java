package duxeye.com.ecolaundry.newapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import duxeye.com.ecolaundry.newapp.constant.Constant;
import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.utility.Utility;

public class SplashScreenActivity extends Activity {
    private  static final String TAG = SplashScreenActivity.class.getSimpleName();
    private final int SCREEN_DURATION = 4000;
    private Activity mActivity;
    private  String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mActivity = SplashScreenActivity.this;

        userName = Utility.getStringSharedPreferences(mActivity.getApplicationContext(), Constant.USER_NAME).trim();

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SCREEN_DURATION);
                    if(userName.equalsIgnoreCase("")){
                        startActivity(new Intent(SplashScreenActivity.this,RegistrationActivity.class));
                    }else{
                        SplashScreenActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utility.showToast(mActivity, "Welcome back to Eco-Laundry");
                            }
                        });

                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }

}
