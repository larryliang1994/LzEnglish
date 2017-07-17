package com.jiubai.lzenglish.widget;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by larry on 05/07/2017.
 */

public class UniversalImageLoader extends com.youth.banner.loader.ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage((String) path, imageView);
    }
}