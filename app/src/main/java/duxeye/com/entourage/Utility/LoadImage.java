package duxeye.com.entourage.Utility;

import android.app.Activity;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Ondoor (Hasnain) on 8/29/2016.
 */
public class LoadImage {

    public static void load(final Activity mActivity, final String imageUrl, final ImageView mImageView) {
        Picasso.with(mActivity).load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(mImageView, new Callback() {
            @Override
            public void onSuccess() {
                /**
                 * do nothing ig image download successfully
                 */
            }

            @Override
            public void onError() {
                /**
                 * Try again online if cache / download failed
                 */
                Picasso.with(mActivity).load(imageUrl).networkPolicy(NetworkPolicy.NO_CACHE).into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        /**
                         * do nothing ig image download successfully
                         */
                    }

                    @Override
                    public void onError() {
                        /**
                         * don't try for 3rd time
                         */
                    }
                });
            }
        });
    }

}
