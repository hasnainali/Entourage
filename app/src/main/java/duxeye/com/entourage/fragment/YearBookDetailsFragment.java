package duxeye.com.entourage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.constant.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearBookDetailsFragment extends Fragment {
    private static final String TAG = YearBookDetailsFragment.class.getSimpleName();
    private View mView;
    private ImageView backButton;
    private ViewPager mViewPager;

    public YearBookDetailsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_year_book_detail, container, false);

        init();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new YearbookFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.year_book_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return mView;
    }

    private void init() {
        ((TextView) mView.findViewById(R.id.pd_header_text)).setText("YEARBOOK DETAILS");
        backButton = (ImageView) mView.findViewById(R.id.iv_ybd_back);

        mViewPager = (ViewPager) mView.findViewById(R.id.vp_ybd);
        mViewPager.setAdapter(new YearBookPagerAdapter(getActivity().getSupportFragmentManager()));
        mViewPager.setCurrentItem(Integer.parseInt(Utility.getSharedPreferences(getActivity(), Constant.CURRENT_PAGE_INDEX)));
    }


    public class YearBookPagerAdapter extends FragmentStatePagerAdapter {
        public YearBookPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                position = 1;
            }
            return YearbookRowFragment.newInstance("" + position + "");
        }

        @Override
        public int getCount() {
            return Integer.parseInt(Utility.getSharedPreferences(getActivity(), Constant.NUMBER_PAGES_PROD));
        }

        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }


    }
}
