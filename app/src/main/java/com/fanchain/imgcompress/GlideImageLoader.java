package com.fanchain.imgcompress;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

public class GlideImageLoader implements ImageLoader {


    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        RequestOptions options = new RequestOptions().override(width, height).centerInside();
        Glide.with(activity).load(Uri.fromFile(new File(path)))
                .apply(options).into(imageView);
    }

    @Override
    public void clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }
}
