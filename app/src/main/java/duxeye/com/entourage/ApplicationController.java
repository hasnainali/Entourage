package duxeye.com.entourage;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Ondoor (Hasnain) on 4/8/2016.
 */
public class ApplicationController extends Application {
    public static final String TAG = ApplicationController.class.getSimpleName();
    private static ApplicationController sInstance;
    private static ImageLoader imageLoader = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static ImageLoader getImageLoader() {
        if (imageLoader == null) {
            String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(sInstance.getBaseContext(), CACHE_DIR);

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();

            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(sInstance.getBaseContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .discCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

            return imageLoader;
        } else {
            return imageLoader;
        }
    }
}
