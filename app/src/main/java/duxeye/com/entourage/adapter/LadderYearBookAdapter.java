package duxeye.com.entourage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.LoadImage;
import duxeye.com.entourage.model.LadderYearBook;

/**
 * Created by User on 23-07-2016.
 */
public class LadderYearBookAdapter extends RecyclerView.Adapter<LadderYearBookAdapter.MyViewHolder> {
    private static String TAG = LadderYearBookAdapter.class.getSimpleName();
    private ArrayList<LadderYearBook> mArrayList;
    private ItemClickListener onItemClickListener;
    private Activity mActivity;

    public interface ItemClickListener {
        void onPhotoClick(LadderYearBook mLadderYearBook,int position,boolean isLeft,boolean isRight);
    }



    public LadderYearBookAdapter(Activity mActivity, ArrayList<LadderYearBook> mArrayList, ItemClickListener listener) {
        this.mArrayList = mArrayList;
        this.onItemClickListener = listener;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ladder_yeard_book, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mArrayList.get(position),position, onItemClickListener);

    }


    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView photoImageViewLeft,photoImageViewRight;
        private TextView ladderTextViewLeft,ladderTextViewRight;

        public MyViewHolder(View view) {
            super(view);
            photoImageViewLeft = (ImageView) view.findViewById(R.id.iv_ladder_grid_photo_left);
            photoImageViewRight = (ImageView) view.findViewById(R.id.iv_ladder_grid_photo_right);
            ladderTextViewLeft = (TextView) view.findViewById(R.id.tv_ladder_yearbook_title_left);
            ladderTextViewRight = (TextView) view.findViewById(R.id.tv_ladder_yearbook_title_right);
        }


        public void bind(final LadderYearBook mLadderYearBook,final int position, final ItemClickListener onItemClickListener) {
            LoadImage.load(mActivity, mLadderYearBook.getLeftPageImg(), photoImageViewLeft);
            LoadImage.load(mActivity, mLadderYearBook.getRightPageImg(),photoImageViewRight);
            ladderTextViewLeft.setText(mLadderYearBook.getLeftPageLabel());
            ladderTextViewRight.setText(mLadderYearBook.getRightPageLabel());

            photoImageViewLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onPhotoClick(mLadderYearBook,position,true,false);
                }
            });

            photoImageViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onPhotoClick(mLadderYearBook,position,false,true);
                }
            });
        }
    }
}