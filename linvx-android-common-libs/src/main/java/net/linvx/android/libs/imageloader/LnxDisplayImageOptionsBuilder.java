package net.linvx.android.libs.imageloader;

import android.widget.ImageView;

/**
 * Created by lizelin on 16/3/14.
 */
public final class LnxDisplayImageOptionsBuilder {
    private String uri;
    private ImageView imageView;
    private LnxImageSize willImageSize;
    private OnImageLoadedListener onImageLoadedListener;
    private LnxBitmapProcessorInterface bitmapProcessor;
    private int loadingIconResId;
    private int loadErrorIconResId;
    private int uriEmptyIconResId;
    private boolean allowFromCacheFlag = true;
    private boolean allowCacheOnDiskFlag = true;

    private LnxDisplayImageOptionsBuilder() {
    }

    private static LnxDisplayImageOptionsBuilder defaultDisplayImageOptionsBuilder = new LnxDisplayImageOptionsBuilder();

    public static synchronized void initDefaultBuilder(LnxDisplayImageOptionsBuilder b) {
        defaultDisplayImageOptionsBuilder = b.clone();
    }

    @Override
    public LnxDisplayImageOptionsBuilder clone() {
        LnxDisplayImageOptionsBuilder builder = new LnxDisplayImageOptionsBuilder();
        builder.setUri(this.getUri())
                .setBitmapProcessor(this.getBitmapProcessor())
                .setAllowCacheOnDiskFlag(this.isAllowCacheOnDiskFlag())
                .setAllowFromCacheFlag(this.isAllowFromCacheFlag())
                .setImageView(this.getImageView())
                .setLoadErrorIconResId(this.getLoadErrorIconResId())
                .setLoadingIconResId(this.getLoadingIconResId())
                .setUriEmptyIconResId(this.getUriEmptyIconResId())
                .setWillImageSize(this.getWillImageSize())
                .setOnImageLoadedListener(this.getOnImageLoadedListener());
        return builder;
    }

    public static LnxDisplayImageOptionsBuilder createLnxDisplayImageOptionsBuilder() {
        return defaultDisplayImageOptionsBuilder.clone();
    }


    public String getUri() {
        return uri;
    }

    public LnxDisplayImageOptionsBuilder setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public LnxDisplayImageOptionsBuilder setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public LnxImageSize getWillImageSize() {
        return willImageSize;
    }

    public LnxDisplayImageOptionsBuilder setWillImageSize(LnxImageSize willImageSize) {
        this.willImageSize = willImageSize;
        return this;
    }

    public OnImageLoadedListener getOnImageLoadedListener() {
        return onImageLoadedListener;
    }

    public LnxDisplayImageOptionsBuilder setOnImageLoadedListener(OnImageLoadedListener onImageLoadedListener) {
        this.onImageLoadedListener = onImageLoadedListener;
        return this;
    }

    public LnxBitmapProcessorInterface getBitmapProcessor() {
        return bitmapProcessor;
    }

    public LnxDisplayImageOptionsBuilder setBitmapProcessor(LnxBitmapProcessorInterface bitmapProcessor) {
        this.bitmapProcessor = bitmapProcessor;
        return this;
    }

    public int getLoadingIconResId() {
        return loadingIconResId;
    }

    public LnxDisplayImageOptionsBuilder setLoadingIconResId(int loadingIconResId) {
        this.loadingIconResId = loadingIconResId;
        return this;
    }

    public int getLoadErrorIconResId() {
        return loadErrorIconResId;
    }

    public LnxDisplayImageOptionsBuilder setLoadErrorIconResId(int loadErrorIconResId) {
        this.loadErrorIconResId = loadErrorIconResId;
        return this;
    }

    public int getUriEmptyIconResId() {
        return uriEmptyIconResId;
    }

    public LnxDisplayImageOptionsBuilder setUriEmptyIconResId(int uriEmptyIconResId) {
        this.uriEmptyIconResId = uriEmptyIconResId;
        return this;
    }

    public boolean isAllowCacheOnDiskFlag() {
        return allowCacheOnDiskFlag;
    }

    public LnxDisplayImageOptionsBuilder setAllowCacheOnDiskFlag(boolean allowCacheOnDiskFlag) {
        this.allowCacheOnDiskFlag = allowCacheOnDiskFlag;
        return this;
    }

    public boolean isAllowFromCacheFlag() {
        return allowFromCacheFlag;
    }

    public LnxDisplayImageOptionsBuilder setAllowFromCacheFlag(boolean allowFromCacheFlag) {
        this.allowFromCacheFlag = allowFromCacheFlag;
        return this;
    }
}
