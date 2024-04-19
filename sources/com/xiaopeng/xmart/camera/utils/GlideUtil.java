package com.xiaopeng.xmart.camera.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xiaopeng.xmart.camera.App;
/* loaded from: classes.dex */
public class GlideUtil {
    public static void loadCircleThumbnail(String path, final ImageView imageView, int errorId, int placeHold, MultiTransformation transformation) {
        Glide.with(App.getInstance()).load(path).diskCacheStrategy(DiskCacheStrategy.RESOURCE).apply((BaseRequestOptions<?>) RequestOptions.errorOf(errorId)).listener(new RequestListener<Drawable>() { // from class: com.xiaopeng.xmart.camera.utils.GlideUtil.1
            @Override // com.bumptech.glide.request.RequestListener
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override // com.bumptech.glide.request.RequestListener
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                ImageView imageView2 = imageView;
                if (imageView2 != null) {
                    imageView2.setImageResource(0);
                    imageView.setImageDrawable(resource);
                }
                return false;
            }
        }).transform(transformation).into(imageView);
    }

    public static void loadWithListener(String path, ImageView imageView, int errorId, int placeHold, MultiTransformation transformation, RequestListener<Drawable> listener) {
        Glide.with(App.getInstance()).load(path).diskCacheStrategy(DiskCacheStrategy.RESOURCE).apply((BaseRequestOptions<?>) RequestOptions.errorOf(errorId)).listener(listener).transform(transformation).into(imageView);
    }
}
