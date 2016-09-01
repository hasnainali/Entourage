package duxeye.com.entourage.viewHolder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;

import duxeye.com.entourage.R;
import duxeye.com.entourage.Utility.Utility;
import duxeye.com.entourage.callback.CarouselCallback;
import duxeye.com.entourage.constant.Constant;
import duxeye.com.entourage.model.Carousel;

public class HomeHeaderAdapter extends RecyclerView.ViewHolder {

    private static SliderLayout mDemoSlider;
//    private static PagerIndicator mcustom_indicator;
    private TextView mTextView;

    public HomeHeaderAdapter(View itemView) {
        super(itemView);
        mDemoSlider = (SliderLayout) itemView.findViewById(R.id.slider);
        mDemoSlider.setCustomIndicator((PagerIndicator) itemView.findViewById(R.id.custom_indicator));
        mTextView = (TextView) itemView.findViewById(R.id.tv_select_year_book);
        mTextView.setText(Utility.getSharedPreferences(itemView.getContext(), Constant.YEARBOOK_NAME));
    }

    public void setPager(final ArrayList<Carousel> carouselsArrayList, final CarouselCallback carouselCallback, Context context) {
        mDemoSlider.removeAllSliders();
        for (int i = 0; i < carouselsArrayList.size(); i++) {
            try {
                TextSliderView textSliderView = new TextSliderView(context);
                textSliderView.description(carouselsArrayList.get(i).getLinkType())
                        .image(carouselsArrayList.get(i).getImageUrl())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                String linkType[] = slider.getBundle().getString("extra").split("@");

                                if (linkType[0].equalsIgnoreCase("LINK")) {
                                    carouselCallback.onLinkClick(linkType[1]);

                                } else if (linkType[0].equalsIgnoreCase("UPLOAD")) {
                                    carouselCallback.onUploadClick();
                                }
                            }
                        });

                //add your extra information
                textSliderView.bundle(new Bundle());
                String link;
                if (carouselsArrayList.get(i).getLinkValue().equalsIgnoreCase("")) {
                    link = "temp";
                } else {
                    link = carouselsArrayList.get(i).getLinkValue();
                }
                String meatData = carouselsArrayList.get(i).getLinkType() + "@" + link;
                textSliderView.getBundle().putString("extra", meatData);
                mDemoSlider.addSlider(textSliderView);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation() {

            @Override
            public void onNextItemAppear(View view) {
                view.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);
            }
        });
        mDemoSlider.stopAutoCycle();
    }


}
