package com.xiaopeng.xmart.camera;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import java.io.File;
import java.net.URL;
import java.util.List;
/* loaded from: classes.dex */
public class GlideRequest<TranscodeType> extends RequestBuilder<TranscodeType> implements Cloneable {
    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ RequestBuilder apply(BaseRequestOptions options) {
        return apply((BaseRequestOptions<?>) options);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ BaseRequestOptions apply(BaseRequestOptions options) {
        return apply((BaseRequestOptions<?>) options);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ BaseRequestOptions decode(Class clazz) {
        return decode((Class<?>) clazz);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ BaseRequestOptions optionalTransform(Transformation transformation) {
        return optionalTransform((Transformation<Bitmap>) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ BaseRequestOptions set(Option option, Object y) {
        return set((Option<Option>) option, (Option) y);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ BaseRequestOptions transform(Transformation transformation) {
        return transform((Transformation<Bitmap>) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ BaseRequestOptions transform(Transformation[] transformations) {
        return transform((Transformation<Bitmap>[]) transformations);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    @Deprecated
    public /* bridge */ /* synthetic */ BaseRequestOptions transforms(Transformation[] transformations) {
        return transforms((Transformation<Bitmap>[]) transformations);
    }

    GlideRequest(Class<TranscodeType> transcodeClass, RequestBuilder<?> other) {
        super(transcodeClass, other);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GlideRequest(Glide glide, RequestManager requestManager, Class<TranscodeType> transcodeClass, Context context) {
        super(glide, requestManager, transcodeClass, context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<File> getDownloadOnlyRequest() {
        return new GlideRequest(File.class, this).apply((BaseRequestOptions<?>) DOWNLOAD_ONLY_OPTIONS);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> sizeMultiplier(float value) {
        return (GlideRequest) super.sizeMultiplier(value);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> useUnlimitedSourceGeneratorsPool(boolean flag) {
        return (GlideRequest) super.useUnlimitedSourceGeneratorsPool(flag);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> useAnimationPool(boolean flag) {
        return (GlideRequest) super.useAnimationPool(flag);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> onlyRetrieveFromCache(boolean flag) {
        return (GlideRequest) super.onlyRetrieveFromCache(flag);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> diskCacheStrategy(DiskCacheStrategy strategy) {
        return (GlideRequest) super.diskCacheStrategy(strategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> priority(Priority priority) {
        return (GlideRequest) super.priority(priority);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> placeholder(Drawable drawable) {
        return (GlideRequest) super.placeholder(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> placeholder(int id) {
        return (GlideRequest) super.placeholder(id);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> fallback(Drawable drawable) {
        return (GlideRequest) super.fallback(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> fallback(int id) {
        return (GlideRequest) super.fallback(id);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> error(Drawable drawable) {
        return (GlideRequest) super.error(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> error(int id) {
        return (GlideRequest) super.error(id);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> theme(Resources.Theme theme) {
        return (GlideRequest) super.theme(theme);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> skipMemoryCache(boolean skip) {
        return (GlideRequest) super.skipMemoryCache(skip);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> override(int width, int height) {
        return (GlideRequest) super.override(width, height);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> override(int size) {
        return (GlideRequest) super.override(size);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> signature(Key key) {
        return (GlideRequest) super.signature(key);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public <Y> GlideRequest<TranscodeType> set(Option<Y> option, Y y) {
        return (GlideRequest) super.set((Option<Option<Y>>) option, (Option<Y>) y);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> decode(Class<?> clazz) {
        return (GlideRequest) super.decode(clazz);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> encodeFormat(Bitmap.CompressFormat format) {
        return (GlideRequest) super.encodeFormat(format);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> encodeQuality(int value) {
        return (GlideRequest) super.encodeQuality(value);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> frame(long value) {
        return (GlideRequest) super.frame(value);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> format(DecodeFormat format) {
        return (GlideRequest) super.format(format);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> disallowHardwareConfig() {
        return (GlideRequest) super.disallowHardwareConfig();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> downsample(DownsampleStrategy strategy) {
        return (GlideRequest) super.downsample(strategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> timeout(int value) {
        return (GlideRequest) super.timeout(value);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> optionalCenterCrop() {
        return (GlideRequest) super.optionalCenterCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> centerCrop() {
        return (GlideRequest) super.centerCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> optionalFitCenter() {
        return (GlideRequest) super.optionalFitCenter();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> fitCenter() {
        return (GlideRequest) super.fitCenter();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> optionalCenterInside() {
        return (GlideRequest) super.optionalCenterInside();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> centerInside() {
        return (GlideRequest) super.centerInside();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> optionalCircleCrop() {
        return (GlideRequest) super.optionalCircleCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> circleCrop() {
        return (GlideRequest) super.circleCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> transform(Transformation<Bitmap> transformation) {
        return (GlideRequest) super.transform(transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> transform(Transformation<Bitmap>... transformations) {
        return (GlideRequest) super.transform(transformations);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    @Deprecated
    public GlideRequest<TranscodeType> transforms(Transformation<Bitmap>... transformations) {
        return (GlideRequest) super.transforms(transformations);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> optionalTransform(Transformation<Bitmap> transformation) {
        return (GlideRequest) super.optionalTransform(transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public <Y> GlideRequest<TranscodeType> optionalTransform(Class<Y> clazz, Transformation<Y> transformation) {
        return (GlideRequest) super.optionalTransform((Class) clazz, (Transformation) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public <Y> GlideRequest<TranscodeType> transform(Class<Y> clazz, Transformation<Y> transformation) {
        return (GlideRequest) super.transform((Class) clazz, (Transformation) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> dontTransform() {
        return (GlideRequest) super.dontTransform();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> dontAnimate() {
        return (GlideRequest) super.dontAnimate();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> lock() {
        return (GlideRequest) super.lock();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> autoClone() {
        return (GlideRequest) super.autoClone();
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.request.BaseRequestOptions
    public GlideRequest<TranscodeType> apply(BaseRequestOptions<?> options) {
        return (GlideRequest) super.apply(options);
    }

    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<TranscodeType> transition(TransitionOptions<?, ? super TranscodeType> options) {
        return (GlideRequest) super.transition((TransitionOptions) options);
    }

    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<TranscodeType> listener(RequestListener<TranscodeType> listener) {
        return (GlideRequest) super.listener((RequestListener) listener);
    }

    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<TranscodeType> addListener(RequestListener<TranscodeType> listener) {
        return (GlideRequest) super.addListener((RequestListener) listener);
    }

    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<TranscodeType> error(RequestBuilder<TranscodeType> builder) {
        return (GlideRequest) super.error((RequestBuilder) builder);
    }

    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<TranscodeType> error(Object o) {
        return (GlideRequest) super.error(o);
    }

    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<TranscodeType> thumbnail(RequestBuilder<TranscodeType> builder) {
        return (GlideRequest) super.thumbnail((RequestBuilder) builder);
    }

    @Override // com.bumptech.glide.RequestBuilder
    @SafeVarargs
    public final GlideRequest<TranscodeType> thumbnail(RequestBuilder<TranscodeType>... builders) {
        return (GlideRequest) super.thumbnail((RequestBuilder[]) builders);
    }

    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<TranscodeType> thumbnail(List<RequestBuilder<TranscodeType>> list) {
        return (GlideRequest) super.thumbnail((List) list);
    }

    @Override // com.bumptech.glide.RequestBuilder
    public GlideRequest<TranscodeType> thumbnail(float sizeMultiplier) {
        return (GlideRequest) super.thumbnail(sizeMultiplier);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    public GlideRequest<TranscodeType> load(Object o) {
        return (GlideRequest) super.load(o);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    public GlideRequest<TranscodeType> load(Bitmap bitmap) {
        return (GlideRequest) super.load(bitmap);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    public GlideRequest<TranscodeType> load(Drawable drawable) {
        return (GlideRequest) super.load(drawable);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    public GlideRequest<TranscodeType> load(String string) {
        return (GlideRequest) super.load(string);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    public GlideRequest<TranscodeType> load(Uri uri) {
        return (GlideRequest) super.load(uri);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    public GlideRequest<TranscodeType> load(File file) {
        return (GlideRequest) super.load(file);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    public GlideRequest<TranscodeType> load(Integer id) {
        return (GlideRequest) super.load(id);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    @Deprecated
    public GlideRequest<TranscodeType> load(URL url) {
        return (GlideRequest) super.load(url);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.ModelTypes
    public GlideRequest<TranscodeType> load(byte[] bytes) {
        return (GlideRequest) super.load(bytes);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: clone */
    public GlideRequest<TranscodeType> mo82clone() {
        return (GlideRequest) super.mo82clone();
    }
}
