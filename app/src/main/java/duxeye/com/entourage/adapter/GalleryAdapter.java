package duxeye.com.entourage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;

import duxeye.com.entourage.ApplicationController;
import duxeye.com.entourage.R;
import duxeye.com.entourage.model.CustomGallery;


public class GalleryAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<CustomGallery> data = new ArrayList<>();
//	ImageLoader imageLoader;

	private boolean isActionMultiplePick;

	public GalleryAdapter(Context c) {
		infalter = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = c;
//		this.imageLoader = imageLoader;
		// clearCache();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public CustomGallery getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setMultiplePick(boolean isMultiplePick) {
		this.isActionMultiplePick = isMultiplePick;
	}

//	public void selectAll(boolean selection) {
//		for (int i = 0; i < data.size(); i++) {
//			data.get(i).isSeleted = selection;
//
//		}
//		notifyDataSetChanged();
//	}

//	public boolean isAllSelected() {
//		boolean isAllSelected = true;
//
//		for (int i = 0; i < data.size(); i++) {
//			if (!data.get(i).isSeleted) {
//				isAllSelected = false;
//				break;
//			}
//		}
//
//		return isAllSelected;
//	}

//	public boolean isAnySelected() {
//		boolean isAnySelected = false;
//
//		for (int i = 0; i < data.size(); i++) {
//			if (data.get(i).isSeleted) {
//				isAnySelected = true;
//				break;
//			}
//		}
//
//		return isAnySelected;
//	}

	public ArrayList<CustomGallery> getSelected() {
		ArrayList<CustomGallery> dataT = new ArrayList<>();

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSelected()) {
				dataT.add(data.get(i));
			}
		}

		return dataT;
	}

	public void addAll(ArrayList<CustomGallery> files) {

		try {
			this.data.clear();
			this.data.addAll(files);

		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyDataSetChanged();
	}

	public void changeSelection(View v, int position) {

		if (data.get(position).isSelected()) {
			data.get(position).setSelected(false);
		} else {
			data.get(position).setSelected(true);
		}

		((ViewHolder) v.getTag()).imageCheckButton.setSelected(data.get(position).isSelected());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.gallery_item, null);
			holder = new ViewHolder();
			holder.mImage = (ImageView) convertView.findViewById(R.id.iv_Image);

			holder.imageCheckButton = (ImageView) convertView.findViewById(R.id.iv_check);

			if (isActionMultiplePick) {
				holder.imageCheckButton.setVisibility(View.VISIBLE);
			} else {
				holder.imageCheckButton.setVisibility(View.GONE);
			}

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mImage.setTag(position);

		try {

			ApplicationController.getImageLoader().displayImage("file://" + data.get(position).getSdcardPath(),
					holder.mImage, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.mImage
									.setImageResource(R.drawable.no_media);
							super.onLoadingStarted(imageUri, view);
						}
					});

			if (isActionMultiplePick) {

				holder.imageCheckButton.setSelected(data.get(position).isSelected());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public class ViewHolder {
		private ImageView mImage;
		private ImageView imageCheckButton;
	}

//	public void clearCache() {
//		imageLoader.clearDiscCache();
//		imageLoader.clearMemoryCache();
//	}

//	public void clear() {
//		data.clear();
//		notifyDataSetChanged();
//	}
}