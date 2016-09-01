package duxeye.com.entourage.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.callback.ChangeTab;
import duxeye.com.entourage.R;
import duxeye.com.entourage.adapter.ViewPagerAdapter;
import duxeye.com.entourage.callback.Refresh;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.customViews.CustomTextView;
import duxeye.com.entourage.customViews.MyDialog;
import duxeye.com.entourage.fragment.AccountInfoFragment;
import duxeye.com.entourage.fragment.HomeFragment;
import duxeye.com.entourage.fragment.PhotosFragment;
import duxeye.com.entourage.fragment.UploadFragment;
import duxeye.com.entourage.fragment.YearbookFragment;

public class HomeActivity extends AppCompatActivity implements ChangeTab {
    private Refresh mRefresh;
    private Activity mActivity;
    private int pos;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.redhome,
            R.drawable.redpicture,
            R.drawable.redupload,
            R.drawable.redbook,
            R.drawable.redprofile
    };
    private int[] utabIcons = {
            R.drawable.grayhome,
            R.drawable.graypicture,
            R.drawable.grayupload,
            R.drawable.graybook,
            R.drawable.grayprofile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mActivity = this;
        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set view pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //set tabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons(0);

        // call when tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos = tab.getPosition();
                viewPager.setCurrentItem(pos);
                View view = (View) tabLayout.getTabAt(pos).getCustomView();
                // set tabs text color and icon red
                ((TextView) view.findViewById(R.id.tv_tab)).setTextColor(getResources().getColor(R.color.colorPrimary));
                ((ImageView) view.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(tabIcons[pos]));
                tabLayout.getTabAt(pos).setCustomView(view);

                /***
                 * Refresh photo List
                 */
                if(pos == 1 && Utility.getBoolean(mActivity, Constant.isBackFromImageUploadDetails)){
                    try{
                        changeTabPosition(1);
                        Utility.setBoolean(mActivity, Constant.isBackFromImageUploadDetails,false);

                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // set tabs text color and icon red
                View view = (View) tabLayout.getTabAt(pos).getCustomView();
                ((TextView) view.findViewById(R.id.tv_tab)).setTextColor(getResources().getColor(R.color.textColorTab));
                ((ImageView) view.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(utabIcons[pos]));
                tabLayout.getTabAt(pos).setCustomView(view);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    // this method set swipe function in this activity
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "ONE");
        adapter.addFragment(new PhotosFragment(), "TWO");
        adapter.addFragment(new UploadFragment(), "THREE");
        adapter.addFragment(new YearbookFragment(), "FOUR");
        adapter.addFragment(new AccountInfoFragment(), "FIVE");
        viewPager.setAdapter(adapter);
    }

    // set custom tabs
    private void setupTabIcons(int pos) {

        View tabOne = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView) tabOne.findViewById(R.id.tv_tab)).setText(getString(R.string.tabHome));
        ((ImageView) tabOne.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(R.drawable.grayhome));
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View tabTwo = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView) tabTwo.findViewById(R.id.tv_tab)).setText(getString(R.string.tabphotos));
        ((ImageView) tabTwo.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(R.drawable.graypicture));
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        View tabThree = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView) tabThree.findViewById(R.id.tv_tab)).setText(getString(R.string.tabUpload));
        ((ImageView) tabThree.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(R.drawable.grayupload));
        tabLayout.getTabAt(2).setCustomView(tabThree);

        View tabfour = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView) tabfour.findViewById(R.id.tv_tab)).setText(getString(R.string.tabyearbook));
        ((ImageView) tabfour.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(R.drawable.graybook));
        tabLayout.getTabAt(3).setCustomView(tabfour);

        View tabfive = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView) tabfive.findViewById(R.id.tv_tab)).setText(getString(R.string.tabacount));
        ((ImageView) tabfive.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(R.drawable.grayprofile));
        tabLayout.getTabAt(4).setCustomView(tabfive);

        View view = (View) tabLayout.getTabAt(pos).getCustomView();
        ((TextView) view.findViewById(R.id.tv_tab)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((ImageView) view.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(tabIcons[pos]));
        tabLayout.getTabAt(pos).setCustomView(view);

    }

    @Override
    public void onBackPressed() {
        final android.app.Dialog mDialog = MyDialog.appExitDialog("Are you sure you want to exit?", mActivity);
        ((CustomTextView) mDialog.findViewById(R.id.yes)).setText("Yes");
        ((CustomTextView) mDialog.findViewById(R.id.no)).setText("No");
        (mDialog.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);

            }
        });

        (mDialog.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

    }


    @Override
    public void changeTabPosition(int position) {
        //un-select previous tab
        View mView = tabLayout.getTabAt(pos).getCustomView();
        ((TextView) mView.findViewById(R.id.tv_tab)).setTextColor(getResources().getColor(R.color.textColorTab));
        ((ImageView) mView.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(utabIcons[pos]));
        tabLayout.getTabAt(pos).setCustomView(mView);

        //Select tab on provided position
        viewPager.setCurrentItem(position);
        View view = tabLayout.getTabAt(position).getCustomView();
        ((TextView) view.findViewById(R.id.tv_tab)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((ImageView) view.findViewById(R.id.img_tab)).setImageDrawable(getResources().getDrawable(tabIcons[position]));
        tabLayout.getTabAt(position).setCustomView(view);

    }
}
