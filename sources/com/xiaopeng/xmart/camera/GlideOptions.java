package com.xiaopeng.xmart.camera;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
/* loaded from: classes.dex */
public final class GlideOptions extends RequestOptions implements Cloneable {
    private static GlideOptions centerCropTransform2;
    private static GlideOptions centerInsideTransform1;
    private static GlideOptions circleCropTransform3;
    private static GlideOptions fitCenterTransform0;
    private static GlideOptions noAnimation5;
    private static GlideOptions noTransformation4;

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ RequestOptions apply(BaseRequestOptions options) {
        return apply2((BaseRequestOptions<?>) options);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ RequestOptions decode(Class clazz) {
        return decode2((Class<?>) clazz);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ RequestOptions optionalTransform(Transformation transformation) {
        return optionalTransform2((Transformation<Bitmap>) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ RequestOptions set(Option option, Object y) {
        return set2((Option<Option>) option, (Option) y);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public /* bridge */ /* synthetic */ RequestOptions transform(Transformation transformation) {
        return transform2((Transformation<Bitmap>) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    @SafeVarargs
    public /* bridge */ /* synthetic */ RequestOptions transform(Transformation[] transformations) {
        return transform2((Transformation<Bitmap>[]) transformations);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    @SafeVarargs
    @Deprecated
    public /* bridge */ /* synthetic */ RequestOptions transforms(Transformation[] transformations) {
        return transforms2((Transformation<Bitmap>[]) transformations);
    }

    public static GlideOptions sizeMultiplierOf(float value) {
        return new GlideOptions().sizeMultiplier(value);
    }

    public static GlideOptions diskCacheStrategyOf(DiskCacheStrategy strategy) {
        return new GlideOptions().diskCacheStrategy(strategy);
    }

    public static GlideOptions priorityOf(Priority priority) {
        return new GlideOptions().priority(priority);
    }

    public static GlideOptions placeholderOf(Drawable drawable) {
        return new GlideOptions().placeholder(drawable);
    }

    public static GlideOptions placeholderOf(int id) {
        return new GlideOptions().placeholder(id);
    }

    public static GlideOptions errorOf(Drawable drawable) {
        return new GlideOptions().error(drawable);
    }

    public static GlideOptions errorOf(int id) {
        return new GlideOptions().error(id);
    }

    public static GlideOptions skipMemoryCacheOf(boolean skipMemoryCache) {
        return new GlideOptions().skipMemoryCache(skipMemoryCache);
    }

    public static GlideOptions overrideOf(int width, int height) {
        return new GlideOptions().override(width, height);
    }

    public static GlideOptions overrideOf(int size) {
        return new GlideOptions().override(size);
    }

    public static GlideOptions signatureOf(Key key) {
        return new GlideOptions().signature(key);
    }

    public static GlideOptions fitCenterTransform() {
        if (fitCenterTransform0 == null) {
            fitCenterTransform0 = new GlideOptions().fitCenter().autoClone();
        }
        return fitCenterTransform0;
    }

    public static GlideOptions centerInsideTransform() {
        if (centerInsideTransform1 == null) {
            centerInsideTransform1 = new GlideOptions().centerInside().autoClone();
        }
        return centerInsideTransform1;
    }

    public static GlideOptions centerCropTransform() {
        if (centerCropTransform2 == null) {
            centerCropTransform2 = new GlideOptions().centerCrop().autoClone();
        }
        return centerCropTransform2;
    }

    public static GlideOptions circleCropTransform() {
        if (circleCropTransform3 == null) {
            circleCropTransform3 = new GlideOptions().circleCrop().autoClone();
        }
        return circleCropTransform3;
    }

    public static GlideOptions bitmapTransform(Transformation<Bitmap> transformation) {
        return new GlideOptions().transform2(transformation);
    }

    public static GlideOptions noTransformation() {
        if (noTransformation4 == null) {
            noTransformation4 = new GlideOptions().dontTransform().autoClone();
        }
        return noTransformation4;
    }

    public static <T> GlideOptions option(Option<T> option, T t) {
        return new GlideOptions().set2((Option<Option<T>>) option, (Option<T>) t);
    }

    public static GlideOptions decodeTypeOf(Class<?> clazz) {
        return new GlideOptions().decode2(clazz);
    }

    public static GlideOptions formatOf(DecodeFormat format) {
        return new GlideOptions().format(format);
    }

    public static GlideOptions frameOf(long value) {
        return new GlideOptions().frame(value);
    }

    public static GlideOptions downsampleOf(DownsampleStrategy strategy) {
        return new GlideOptions().downsample(strategy);
    }

    public static GlideOptions timeoutOf(int value) {
        return new GlideOptions().timeout(value);
    }

    public static GlideOptions encodeQualityOf(int value) {
        return new GlideOptions().encodeQuality(value);
    }

    public static GlideOptions encodeFormatOf(Bitmap.CompressFormat format) {
        return new GlideOptions().encodeFormat(format);
    }

    public static GlideOptions noAnimation() {
        if (noAnimation5 == null) {
            noAnimation5 = new GlideOptions().dontAnimate().autoClone();
        }
        return noAnimation5;
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions sizeMultiplier(float value) {
        return (GlideOptions) super.sizeMultiplier(value);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions useUnlimitedSourceGeneratorsPool(boolean flag) {
        return (GlideOptions) super.useUnlimitedSourceGeneratorsPool(flag);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions useAnimationPool(boolean flag) {
        return (GlideOptions) super.useAnimationPool(flag);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions onlyRetrieveFromCache(boolean flag) {
        return (GlideOptions) super.onlyRetrieveFromCache(flag);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions diskCacheStrategy(DiskCacheStrategy strategy) {
        return (GlideOptions) super.diskCacheStrategy(strategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions priority(Priority priority) {
        return (GlideOptions) super.priority(priority);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions placeholder(Drawable drawable) {
        return (GlideOptions) super.placeholder(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions placeholder(int id) {
        return (GlideOptions) super.placeholder(id);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions fallback(Drawable drawable) {
        return (GlideOptions) super.fallback(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions fallback(int id) {
        return (GlideOptions) super.fallback(id);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions error(Drawable drawable) {
        return (GlideOptions) super.error(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions error(int id) {
        return (GlideOptions) super.error(id);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions theme(Resources.Theme theme) {
        return (GlideOptions) super.theme(theme);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions skipMemoryCache(boolean skip) {
        return (GlideOptions) super.skipMemoryCache(skip);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions override(int width, int height) {
        return (GlideOptions) super.override(width, height);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions override(int size) {
        return (GlideOptions) super.override(size);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions signature(Key key) {
        return (GlideOptions) super.signature(key);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: clone  reason: avoid collision after fix types in other method */
    public RequestOptions mo82clone() {
        return (GlideOptions) super.mo82clone();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: set  reason: avoid collision after fix types in other method */
    public <Y> RequestOptions set2(Option<Y> option, Y y) {
        return (GlideOptions) super.set((Option<Option<Y>>) option, (Option<Y>) y);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: decode  reason: avoid collision after fix types in other method */
    public RequestOptions decode2(Class<?> clazz) {
        return (GlideOptions) super.decode(clazz);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions encodeFormat(Bitmap.CompressFormat format) {
        return (GlideOptions) super.encodeFormat(format);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions encodeQuality(int value) {
        return (GlideOptions) super.encodeQuality(value);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions frame(long value) {
        return (GlideOptions) super.frame(value);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions format(DecodeFormat format) {
        return (GlideOptions) super.format(format);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions disallowHardwareConfig() {
        return (GlideOptions) super.disallowHardwareConfig();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions downsample(DownsampleStrategy strategy) {
        return (GlideOptions) super.downsample(strategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions timeout(int value) {
        return (GlideOptions) super.timeout(value);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions optionalCenterCrop() {
        return (GlideOptions) super.optionalCenterCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions centerCrop() {
        return (GlideOptions) super.centerCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions optionalFitCenter() {
        return (GlideOptions) super.optionalFitCenter();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions fitCenter() {
        return (GlideOptions) super.fitCenter();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions optionalCenterInside() {
        return (GlideOptions) super.optionalCenterInside();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions centerInside() {
        return (GlideOptions) super.centerInside();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions optionalCircleCrop() {
        return (GlideOptions) super.optionalCircleCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions circleCrop() {
        return (GlideOptions) super.circleCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: transform  reason: avoid collision after fix types in other method */
    public RequestOptions transform2(Transformation<Bitmap> transformation) {
        return (GlideOptions) super.transform(transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    @SafeVarargs
    /* renamed from: transform  reason: avoid collision after fix types in other method */
    public final RequestOptions transform2(Transformation<Bitmap>... transformations) {
        return (GlideOptions) super.transform(transformations);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    @SafeVarargs
    @Deprecated
    /* renamed from: transforms  reason: avoid collision after fix types in other method */
    public final RequestOptions transforms2(Transformation<Bitmap>... transformations) {
        return (GlideOptions) super.transforms(transformations);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalTransform  reason: avoid collision after fix types in other method */
    public RequestOptions optionalTransform2(Transformation<Bitmap> transformation) {
        return (GlideOptions) super.optionalTransform(transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public <Y> RequestOptions optionalTransform(Class<Y> clazz, Transformation<Y> transformation) {
        return (GlideOptions) super.optionalTransform((Class) clazz, (Transformation) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public <Y> RequestOptions transform(Class<Y> clazz, Transformation<Y> transformation) {
        return (GlideOptions) super.transform((Class) clazz, (Transformation) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions dontTransform() {
        return (GlideOptions) super.dontTransform();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions dontAnimate() {
        return (GlideOptions) super.dontAnimate();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: apply  reason: avoid collision after fix types in other method */
    public RequestOptions apply2(BaseRequestOptions<?> options) {
        return (GlideOptions) super.apply(options);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions lock() {
        return (GlideOptions) super.lock();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions autoClone() {
        return (GlideOptions) super.autoClone();
    }
}
