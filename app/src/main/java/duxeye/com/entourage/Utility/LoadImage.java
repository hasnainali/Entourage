package duxeye.com.entourage.Utility;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Ondoor (Hasnain) on 8/29/2016.
 */
public class LoadImage {

    public static void load (final Activity mActivity, final String imageUrl, final ImageView mImageView) {

        Picasso.with(mActivity)
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(mActivity)
                                .load(imageUrl)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(mImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                });


    }

}
