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
import duxeye.com.entourage.model.PhotoGrid;

/**
 * Created by User on 23-07-2016.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.MyViewHolder> {
    private static String TAG = PhotoGridAdapter.class.getSimpleName();
    private ArrayList<PhotoGrid> mArrayList;
    private ItemClickListener onItemClickListener;
    private Activity mActivity;

    public interface ItemClickListener {
        void onPhotoClick(PhotoGrid mPhotoGrid,int position);
    }


    public PhotoGridAdapter(Activity mActivity, ArrayList<PhotoGrid> mArrayList, ItemClickListener listener) {
        this.mArrayList = mArrayList;
        this.onItemClickListener = listener;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_photo_grid, parent, false);
            return new MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(mArrayList.get(position),position, onItemClickListener);
    }

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


        public void bind(final PhotoGrid mPhotoGrid, final int position, final ItemClickListener onItemClickListener) {
            LoadImage.load(mActivity, mPhotoGrid.getImageUrl(), photoImageView);

            photoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onPhotoClick(mPhotoGrid,position);
                }
            });
        }
    }

}