package com.xiaopeng.xmart.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.net.URL;
/* loaded from: classes.dex */
public class GlideRequests extends RequestManager {
    @Override // com.bumptech.glide.RequestManager
    public /* bridge */ /* synthetic */ RequestManager addDefaultRequestListener(RequestListener listener) {
        return addDefaultRequestListener((RequestListener<Object>) listener);
    }

    public GlideRequests(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode treeNode, Context context) {
        super(glide, lifecycle, treeNode, context);
    }

    @Override // com.bumptech.glide.RequestManager
    public <ResourceType> GlideRequest<ResourceType> as(Class<ResourceType> resourceClass) {
        return new GlideRequest<>(this.glide, this, resourceClass, this.context);
    }

    @Override // com.bumptech.glide.RequestManager
    public synchronized GlideRequests applyDefaultRequestOptions(RequestOptions options) {
        return (GlideRequests) super.applyDefaultRequestOptions(options);
    }

    @Override // com.bumptech.glide.RequestManager
    public synchronized GlideRequests setDefaultRequestOptions(RequestOptions options) {
        return (GlideRequests) super.setDefaultRequestOptions(options);
    }

    @Override // com.bumptech.glide.RequestManager
    public GlideRequests addDefaultRequestListener(RequestListener<Object> listener) {
        return (GlideRequests) super.addDefaultRequestListener(listener);
    }

    @Override // com.bumptech.glide.RequestManager
    public GlideRequest<Bitmap> asBitmap() {
        return (GlideRequest) super.asBitmap();
    }

    @Override // com.bumptech.glide.RequestManager
    public GlideRequest<GifDrawable> asGif() {
        return (GlideRequest) super.asGif();
    }

    @Override // com.bumptech.glide.RequestManager
    public GlideRequest<Drawable> asDrawable() {
        return (GlideRequest) super.asDrawable();
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    public RequestBuilder<Drawable> load(Bitmap bitmap) {
        return (GlideRequest) super.load(bitmap);
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    public RequestBuilder<Drawable> load(Drawable drawable) {
        return (GlideRequest) super.load(drawable);
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    public RequestBuilder<Drawable> load(String string) {
        return (GlideRequest) super.load(string);
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    public RequestBuilder<Drawable> load(Uri uri) {
        return (GlideRequest) super.load(uri);
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    public RequestBuilder<Drawable> load(File file) {
        return (GlideRequest) super.load(file);
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    public RequestBuilder<Drawable> load(Integer id) {
        return (GlideRequest) super.load(id);
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    @Deprecated
    public RequestBuilder<Drawable> load(URL url) {
        return (GlideRequest) super.load(url);
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    public RequestBuilder<Drawable> load(byte[] bytes) {
        return (GlideRequest) super.load(bytes);
    }

    @Override // com.bumptech.glide.RequestManager, com.bumptech.glide.ModelTypes
    public RequestBuilder<Drawable> load(Object o) {
        return (GlideRequest) super.load(o);
    }

    @Override // com.bumptech.glide.RequestManager
    public GlideRequest<File> downloadOnly() {
        return (GlideRequest) super.downloadOnly();
    }

    @Override // com.bumptech.glide.RequestManager
    public GlideRequest<File> download(Object o) {
        return (GlideRequest) super.download(o);
    }

    @Override // com.bumptech.glide.RequestManager
    public GlideRequest<File> asFile() {
        return (GlideRequest) super.asFile();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.bumptech.glide.RequestManager
    public void setRequestOptions(RequestOptions toSet) {
        if (toSet instanceof GlideOptions) {
            super.setRequestOptions(toSet);
        } else {
            super.setRequestOptions(new GlideOptions().apply2((BaseRequestOptions<?>) toSet));
        }
    }
}
