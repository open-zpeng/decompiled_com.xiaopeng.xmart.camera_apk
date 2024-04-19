package com.xiaopeng.xmart.camera;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
/* loaded from: classes.dex */
public class MyAppGlideModule extends AppGlideModule {
    private static final int DISK_CACHE_SIZE = 102400;
    private static final int MEMORY_CACHE_SIZE = 262144;

    @Override // com.bumptech.glide.module.AppGlideModule, com.bumptech.glide.module.AppliesOptions
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setMemoryCache(new LruResourceCache(PlaybackStateCompat.ACTION_SET_REPEAT_MODE));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, OSSConstants.MIN_PART_SIZE_LIMIT));
    }
}
