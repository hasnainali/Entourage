package duxeye.com.entourage.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import duxeye.com.entourage.ApplicationController;
import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Action;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.adapter.GalleryAdapter;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.model.CustomGallery;

public class CustomGalleryActivityNew extends Activity {
	private Activity mActivity;
	private GridView gridGallery;
	private Handler handler;
	private GalleryAdapter adapter;
	private ImageView imgNoMedia;
	private TextView galleryOkButton;

	String action;
//	private ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery);
		mActivity = this;
		action = getIntent().getAction();
		if (action == null) {
			finish();
		}
//		initImageLoader();
		init();
	}

//	private void initImageLoader() {
//		try {
//			String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.temp_tmp";
//			new File(CACHE_DIR).mkdirs();
//
//			File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),CACHE_DIR);
//
//			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//					.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
//					.bitmapConfig(Bitmap.Config.RGB_565).build();
//
//			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getBaseContext())
//					.defaultDisplayImageOptions(defaultOptions)
//					.discCache(new UnlimitedDiscCache(cacheDir))
//					.memoryCache(new WeakMemoryCache());
//
//			ImageLoaderConfiguration config = builder.build();
//			imageLoader = ImageLoader.getInstance();
//			imageLoader.init(config);
//
//		} catch (Exception e) {
//
//		}
//	}

	private void init() {
		handler = new Handler();
		gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext());
		PauseOnScrollListener listener = new PauseOnScrollListener(ApplicationController.getImageLoader(),true, true);
		gridGallery.setOnScrollListener(listener);

		if (action.equalsIgnoreCase(Action.ACTION_MULTIPLE_PICK)) {
			findViewById(R.id.llBottomContainer).setVisibility(View.GONE);
			gridGallery.setOnItemClickListener(mItemMulClickListener);
			adapter.setMultiplePick(true);

//		} else if (action.equalsIgnoreCase(Action.ACTION_PICK)) {
//
//			findViewById(R.id.llBottomContainer).setVisibility(View.GONE);
//			gridGallery.setOnItemClickListener(mItemSingleClickListener);
//			adapter.setMultiplePick(false);

		}

		gridGallery.setAdapter(adapter);
		imgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);

		galleryOkButton = (TextView) findViewById(R.id.textView_done);
		galleryOkButton.setOnClickListener(mOkClickListener);

		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {

					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
						checkImageStatus();
					}
				});
				Looper.loop();
			};

		}.start();

	}

	private void checkImageStatus() {
		if (adapter.isEmpty()) {
			imgNoMedia.setVisibility(View.VISIBLE);
		} else {
			imgNoMedia.setVisibility(View.GONE);
		}
	}

	View.OnClickListener mOkClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ArrayList<CustomGallery> selected = adapter.getSelected();

			String[] allPath = new String[selected.size()];
			for (int i = 0; i < allPath.length; i++) {
				allPath[i] = selected.get(i).getSdcardPath();
			}

			Intent data = new Intent().putExtra("all_path", allPath);
			setResult(RESULT_OK, data);
			finish();

		}
	};
	AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			adapter.changeSelection(v, position);

		}
	};

//	AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
//			CustomGallery item = adapter.getItem(position);
//			Intent data = new Intent().putExtra("single_path", item.sdcardPath);
//			setResult(RESULT_OK, data);
//			finish();
//		}
//	};

	private ArrayList<CustomGallery> getGalleryPhotos() {
		ArrayList<CustomGallery> galleryList = new ArrayList<>();

		try {
			final String[] columns = { MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;

			Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,null, null, orderBy);

			if (imagecursor != null && imagecursor.getCount() > 0) {
				ArrayList<String> mStringArrayList = Utility.getImageArrayList(mActivity, Constant.IMAGE_ARRAY_LIST);

				while (imagecursor.moveToNext()) {
					int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);

					String imagePatch = imagecursor.getString(dataColumnIndex);
					boolean isSelected = false;
					try{
						if(mStringArrayList != null){
							for (int i=0; i< mStringArrayList.size(); i++){
								String uri = mStringArrayList.get(i);

								if(imagePatch.equalsIgnoreCase(uri)){
									isSelected = true;
									break;
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}

					galleryList.add(new CustomGallery(imagePatch,isSelected));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// show newest photo at beginning of the list
		Collections.reverse(galleryList);
		return galleryList;
	}

}