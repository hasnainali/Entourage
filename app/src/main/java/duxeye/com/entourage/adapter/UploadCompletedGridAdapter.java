package duxeye.com.entourage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.LoadImage;

/**
 * Created by User on 23-07-2016.
 */
public class UploadCompletedGridAdapter extends RecyclerView.Adapter<UploadCompletedGridAdapter.MyViewHolder> {
    private static String TAG = UploadCompletedGridAdapter.class.getSimpleName();
    private ArrayList<String> mArrayList;
    private ItemClickListener onItemClickListener;
    private Activity mActivity;

    public interface ItemClickListener {
        void onPhotoClick(String url);
    }


    public UploadCompletedGridAdapter(Activity mActivity, ArrayList<String> mArrayList, ItemClickListener listener) {
        this.mArrayList = mArrayList;
        this.onItemClickListener = listener;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_completed_photo_grid, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mArrayList.get(position), onItemClickListener);

    }


    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView photoImageView;

        public MyViewHolder(View view) {
            super(view);
            photoImageView = (ImageView) view.findViewById(R.id.iv_grid_photo);
        }


        public void bind(final String url, final ItemClickListener onItemClickListener) {
            LoadImage.load(mActivity, url, photoImageView);

            photoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onPhotoClick(url);
                }
            });
        }
    }
}