package duxeye.com.ecolaundry.newapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;

import java.util.HashMap;

import duxeye.com.ecolaundry.newapp.customView.CircularProgressBar;
import duxeye.com.ecolaundry.newapp.R;
import duxeye.com.ecolaundry.newapp.utility.MyUrl;
import duxeye.com.ecolaundry.newapp.utility.Utility;

public class MainActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SliderLayout mDemoSlider;
    private HashMap<String, String> image_url_maps;
    //    private HashMap<String, Integer> url_maps;
    private ImageButton callImageButton, servicesImageButton, priceImageButton, ecoWashImageButton, tickTalkImageButton;
    private TextView callTextView, servicesTextView, priceTextView, ecoWashTextView;
    private LinearLayout menulayout;
    private Animation animHide;
    private Animation animShow;
    private Activity mActivity;
    private CircularProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        init();
        getImageUrl();


        /**
         * Click on Tic-Tak Button
         */
        tickTalkImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = event.getActionMasked();
                switch (id) {
                    case MotionEvent.ACTION_DOWN:
                        tickTalkImageButton.setImageResource(R.drawable.bookwash_selected);
                        break;

                    case MotionEvent.ACTION_UP:
                        tickTalkImageButton.setImageResource(R.drawable.bookwash);
                        startActivity(new Intent(MainActivity.this, PlaceOrderActivity.class));
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        tickTalkImageButton.setImageResource(R.drawable.bookwash);
                        break;
                }
                return true;

            }
        });

        /**
         * Click on Phone call button
         */

        callImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = event.getActionMasked();
                switch (id) {
                    case MotionEvent.ACTION_DOWN:
                        callImageButton.setImageResource(R.mipmap.call_selected);
                        callTextView.setTextColor(getResources().getColor(R.color.selectedTextColor));
                        break;

                    case MotionEvent.ACTION_UP:
                        callImageButton.setImageResource(R.mipmap.call);
                        callTextView.setTextColor(getResources().getColor(R.color.colorApp));
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:+917247249999"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                // public void requestPermissions(@NonNull String[] permissions, int requestCode)
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for Activity#requestPermissions for more details.
                                return false;

                            }
                        } else {
                            startActivity(intent);
                        }

                        break;

                    case MotionEvent.ACTION_CANCEL:
                        callImageButton.setImageResource(R.mipmap.call);
                        callTextView.setTextColor(getResources().getColor(R.color.colorApp));
                        break;
                }
                return false;
            }
        });

        /**
         * Click on Services button
         */

        servicesImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = event.getActionMasked();
                switch (id) {
                    case MotionEvent.ACTION_DOWN:
                        servicesImageButton.setImageResource(R.mipmap.services_selected);
                        servicesTextView.setTextColor(getResources().getColor(R.color.selectedTextColor));
                        break;

                    case MotionEvent.ACTION_UP:
                        servicesImageButton.setImageResource(R.mipmap.services);
                        servicesTextView.setTextColor(getResources().getColor(R.color.colorApp));
                        startActivity(new Intent(MainActivity.this, ServicesActivity.class));
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        servicesImageButton.setImageResource(R.mipmap.services);
                        servicesTextView.setTextColor(getResources().getColor(R.color.colorApp));
                        break;
                }
                return true;

            }
        });

        /**
         * click on Price button
         */

        priceImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = event.getActionMasked();
                switch (id) {
                    case MotionEvent.ACTION_DOWN:
                        priceImageButton.setImageResource(R.mipmap.rupee_selected);
                        priceTextView.setTextColor(getResources().getColor(R.color.selectedTextColor));
                        break;

                    case MotionEvent.ACTION_UP:
                        priceImageButton.setImageResource(R.mipmap.rupee);
                        priceTextView.setTextColor(getResources().getColor(R.color.colorApp));
                        if (Utility.isConnectingToInternet(mActivity)) {

                            startActivity(new Intent(MainActivity.this, PricesActivity.class));

                        } else {
                            Utility.iPhone("No internet connection", mActivity);
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        priceImageButton.setImageResource(R.mipmap.rupee);
                        priceTextView.setTextColor(getResources().getColor(R.color.colorApp));
                        break;
                }
                return true;

            }
        });

        ecoWashImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menulayout.getVisibility() == View.GONE) {
                    menulayout.setVisibility(View.VISIBLE);
                    menulayout.startAnimation(animShow);

                   /* Create Animation */
                    Animation rotation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotation);
                    rotation.setRepeatCount(0);
                    rotation.setFillAfter(true);
                    ecoWashImageButton.startAnimation(rotation);
                } else {
                    closeMainMenu();
                }
            }
        });

        /**
         * History Activity
         */

        findViewById(R.id.tv_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewActivity(HistoryActivity.class);
                //MainActivity.this.finish();
            }
        });

        /**
         * Testimonial Activity
         */

        findViewById(R.id.tv_testimonial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewActivity(TestimonialActivity.class);
                //MainActivity.this.finish();
            }
        });

        /**
         * Story Activity
         */

        findViewById(R.id.tv_story).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewActivity(StoryActivity.class);
                //MainActivity.this.finish();
            }
        });

        /**
         * About us Activity
         */

        findViewById(R.id.tv_about_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isConnectingToInternet(mActivity)) {

                    gotoNewActivity(AboutUsActivity.class);

                } else {
                    Utility.iPhone("No internet connection", mActivity);
                }

            }
        });

        /**
         * Profile Activity
         */

        findViewById(R.id.tv_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewActivity(ProfileActivity.class);
                //MainActivity.this.finish();
            }
        });
    }

    private void init() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        callImageButton = (ImageButton) findViewById(R.id.ib_call);
        servicesImageButton = (ImageButton) findViewById(R.id.ib_services);
        priceImageButton = (ImageButton) findViewById(R.id.ib_prices);
        ecoWashImageButton = (ImageButton) findViewById(R.id.ib_eco_wash);
        tickTalkImageButton = (ImageButton) findViewById(R.id.ib_one_touch);

        servicesTextView = (TextView) findViewById(R.id.tv_service);
        priceTextView = (TextView) findViewById(R.id.tv_price);
        ecoWashTextView = (TextView) findViewById(R.id.tv_eco_wash);
        callTextView = (TextView) findViewById(R.id.tv_call);

        menulayout = (LinearLayout) findViewById(R.id.menulayout);

        animShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);


        mProgressBar = new CircularProgressBar(mActivity);
        mProgressBar.setCancelable(false);

        image_url_maps = new HashMap<>();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //Log.i(TAG, "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Utility.showToast(this, "You have click at: "+slider.getBundle().get("extra") + "");
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onStop() {
        /**
         *  To prevent a memory leak on rotation,
         *  make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
         */

        //mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Eco Laundry")
                .setMessage("Want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    private void closeMainMenu() {
        menulayout.setVisibility(View.GONE);
        menulayout.startAnimation(animHide);

        /* Create Animation */
        Animation rotation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anticlockwise);
        rotation.setRepeatCount(0);
        rotation.setFillAfter(true);
        ecoWashImageButton.startAnimation(rotation);


    }

    private void gotoNewActivity(final Class activityName) {
        closeMainMenu();
        animHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(MainActivity.this, activityName);
                startActivity(i);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void getImageUrl() {
        mProgressBar.start();

        String url = MyUrl.GET_BANNER_IMAGE_URL;
        Log.e(TAG, "Url: " + url);

        new AQuery(mActivity).ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                Log.i(TAG, "Response: " + json);
                if (json != null) {
                    try {
                        for (int i = 0; i < json.length(); i++) {

                            String imageUrl = json.getJSONObject(i).getString("image");
                            String description = json.getJSONObject(i).getString("description");
                            image_url_maps.put(description, imageUrl);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mProgressBar.stop();
                    startSlider();

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
    }

    private void startSlider() {
        for (String name : image_url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            //initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(image_url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation() {

            @Override
            public void onNextItemAppear(View view) {
                //super.onNextItemAppear(view);
                view.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);
            }
        });
        mDemoSlider.setDuration(4000);
    }
}
