package duxeye.com.entourage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.callback.CarouselCallback;
import duxeye.com.entourage.callback.NewsFeedCallback;
import duxeye.com.entourage.fragment.HomeFragment;
import duxeye.com.entourage.model.Carousel;
import duxeye.com.entourage.model.NewsFeed;
import duxeye.com.entourage.viewHolder.HomeNewsFeedAdapter;
import duxeye.com.entourage.viewHolder.HomeHeaderAdapter;

/*
* RecyclerView Adapter that allows to add a header view.
* */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = HomeAdapter.class.getSimpleName();
    private ArrayList<Carousel> carouselsArrayList;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private ArrayList<NewsFeed> newsFeedArrayList;
    private CarouselCallback carouselCallback;
    private NewsFeedCallback newsFeedCallback;
    private Activity mActivity;

//    public interface ItemClickListener {
//        void onYearBookClick(NewsFeed newsFeed);
//    }

    public HomeAdapter(Activity mActivity, ArrayList<NewsFeed> newsFeedArrayList, CarouselCallback carouselCallback, NewsFeedCallback newsFeedCallback, ArrayList<Carousel> carouselsArrayList) {
        this.newsFeedArrayList = newsFeedArrayList;
        this.mActivity = mActivity;
        this.carouselsArrayList = carouselsArrayList;
        this.carouselCallback = carouselCallback;
        this.newsFeedCallback = newsFeedCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.row_news_feed, parent, false);
            return HomeNewsFeedAdapter.newInstance(view);

        } else if (viewType == TYPE_HEADER) {
            final View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_header, parent, false);
            return new HomeHeaderAdapter(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position != 0) {
            HomeNewsFeedAdapter holder = (HomeNewsFeedAdapter) viewHolder;
            try {
                holder.bind(newsFeedArrayList.get(position-1), newsFeedCallback,mActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            HomeHeaderAdapter holder = (HomeHeaderAdapter) viewHolder;
            try {
                holder.setPager(carouselsArrayList, carouselCallback, mActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getItemCount() {
        return newsFeedArrayList == null ? 0 : (newsFeedArrayList.size() + 1);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

}


