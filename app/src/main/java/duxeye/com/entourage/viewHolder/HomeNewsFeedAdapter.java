package duxeye.com.entourage.viewHolder;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.LoadImage;
import duxeye.com.entourage.callback.NewsFeedCallback;
import duxeye.com.entourage.model.NewsFeed;


public class HomeNewsFeedAdapter extends RecyclerView.ViewHolder {
    private TextView yearBookNameTextView, notesTextView, detailsTextView;
    private ImageView thumbImageView;
    private CardView mCardView;

    public HomeNewsFeedAdapter(final View parent, TextView yearBookNameTextView, TextView detailsTextView,
                               TextView notesTextView, ImageView thumbImageView, CardView mCardView) {
        super(parent);
        this.yearBookNameTextView = yearBookNameTextView;
        this.notesTextView = notesTextView;
        this.detailsTextView = detailsTextView;
        this.thumbImageView = thumbImageView;
        this.mCardView = mCardView;

    }

    public static HomeNewsFeedAdapter newInstance(View view) {
        TextView yearBookNameTextView = (TextView) view.findViewById(R.id.tv_name);
        TextView detailsTextView = (TextView) view.findViewById(R.id.tv_detail);
        TextView notesTextView = (TextView) view.findViewById(R.id.tv_nots);
        ImageView thumbImageView = (ImageView) view.findViewById(R.id.iv_thumb);
        CardView mCardView = (CardView) view.findViewById(R.id.cv_row);
        return new HomeNewsFeedAdapter(view, yearBookNameTextView, detailsTextView,
                notesTextView, thumbImageView, mCardView);
    }

    public void bind(final NewsFeed newsFeed, final NewsFeedCallback newsFeedCallback, Activity mActivity) {
        yearBookNameTextView.setText(newsFeed.getTitle());
        notesTextView.setText(newsFeed.getNotes());
        detailsTextView.setText(newsFeed.getAuthorName() + " - " + newsFeed.getCreatedDate());
        LoadImage.load(mActivity, newsFeed.getIcon(), thumbImageView);

        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsFeedCallback.onNewsFeedClick(newsFeed);
            }
        });
    }

}
