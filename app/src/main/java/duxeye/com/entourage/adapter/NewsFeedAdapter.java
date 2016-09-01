//package duxeye.com.entourage.adapter;
//
//import android.app.Activity;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.androidquery.AQuery;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//import duxeye.com.entourage.R;
//import duxeye.com.entourage.model.NewsFeed;
//import duxeye.com.entourage.model.YearBook;
//
///**
// * Created by User on 23-07-2016.
// */
//public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.MyViewHolder> {
//    private static String TAG = NewsFeedAdapter.class.getSimpleName();
//    private ArrayList<NewsFeed> mArrayList;
//    private ItemClickListener onItemClickListener;
//    private Activity mActivity;
//    private static final int HEADER = 1;
//    private static final int ITEM = 2;
//
//    public interface ItemClickListener {
//        void onYearBookClick(NewsFeed newsFeed);
//    }
//
//
//    public NewsFeedAdapter(Activity mActivity, ArrayList<NewsFeed> mArrayList, ItemClickListener listener) {
//        this.mArrayList = mArrayList;
//        this.onItemClickListener = listener;
//        this.mActivity = mActivity;
//    }
//
//
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == HEADER) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_header, parent, false);
//            return new MyHeaderViewHolder(v);
//        }
//        else if (viewType == ITEM){
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_feed, parent, false);
//            return new MyViewHolder(v);
//        }
//        else
//            throw new RuntimeException("Could not inflate layout");
//
//    }
//
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.bind(mArrayList.get(position), onItemClickListener);
//
//    }
//
//
//    //increasing getItemcount to 1. This will be the row of header.
//    @Override
//    public int getItemCount() {
//        return mArrayList.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        private TextView yearBookNameTextView, notesTextView,detailsTextView;
//        private ImageView thumbImageView;
//        private CardView mCardView;
//
//
//        public MyViewHolder(View view) {
//            super(view);
//            yearBookNameTextView = (TextView) view.findViewById(R.id.tv_name);
//            detailsTextView = (TextView) view.findViewById(R.id.tv_detail);
//            notesTextView = (TextView) view.findViewById(R.id.tv_nots);
//            thumbImageView = (ImageView) view.findViewById(R.id.iv_thumb);
//            mCardView = (CardView) view.findViewById(R.id.cv_row);
//        }
//
//
//        public void bind(final NewsFeed newsFeed, final ItemClickListener onItemClickListener) {
//            yearBookNameTextView.setText(newsFeed.getTitle());
//            notesTextView.setText(newsFeed.getNotes());
//            detailsTextView.setText(newsFeed.getAuthorName()+" - "+newsFeed.getCreatedDate());
//            Picasso.with(mActivity).load(newsFeed.getIcon()).into(thumbImageView);
//
//            mCardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onYearBookClick(newsFeed);
//                }
//            });
//        }
//    }
//
//    class MyHeaderViewHolder extends RecyclerView.ViewHolder {
//
//        public MyHeaderViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//}