package net.linvx.android.libs.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import net.linvx.android.libs.ui.LnxBitmapHelper;
import net.linvx.android.libs.ui.LnxViewHelper;
import net.linvx.android.libs.utils.LogUtils;
import net.linvx.android.libs.utils.StringUtils;


/**
 * Created by lizelin on 16/3/13.
 * 用法：
 * ImageView iv = act.findViewById(R.id.*****);
 * LnxImageLoader.init(this);
 * String uri = "http://www.haishang360.com/data/afficheimg/1457640144791676954.jpg";
 * LnxImageLoader.getInstance().displayImage(uri, iv);
 */
public class LnxImageLoader {
    private static volatile LnxImageLoader instance;
    private LnxImageCache imageCacheInstance;

    public static void init(Context context) {
        if (instance == null) {
            synchronized (LnxImageLoader.class) {
                if (instance == null) {
                    instance = new LnxImageLoader(context);
                }
            }
        }
    }

    public static LnxImageLoader getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("LnxImageLoader must be initialize by calling LnxImageLoader.init(Context context)! ");
        }
        return instance;
    }

    private LnxImageLoader(Context context) {
        LnxImageCache.init(context);
        imageCacheInstance = LnxImageCache.getInstance();
    }


    public void displayImage(String uri) {
        displayImage(LnxDisplayImageOptionsBuilder
                .createLnxDisplayImageOptionsBuilder()
                .setUri(uri));
    }

    public void displayImage(String uri, ImageView imageView) {
        displayImage(LnxDisplayImageOptionsBuilder
                .createLnxDisplayImageOptionsBuilder()
                .setUri(uri)
                .setImageView(imageView));
    }

    public void displayImage(String uri, ImageView imageView, OnImageLoadedListener listener) {
        displayImage(LnxDisplayImageOptionsBuilder
                .createLnxDisplayImageOptionsBuilder()
                .setUri(uri)
                .setImageView(imageView)
                .setOnImageLoadedListener(listener));
    }

    public void displayImage(String uri, ImageView imageView, LnxBitmapProcessorInterface processor,
                             OnImageLoadedListener listener) {
        displayImage(LnxDisplayImageOptionsBuilder
                .createLnxDisplayImageOptionsBuilder()
                .setUri(uri)
                .setImageView(imageView)
                .setBitmapProcessor(processor)
                .setOnImageLoadedListener(listener));
    }

    public void displayImage(String uri, ImageView imageView, LnxBitmapProcessorInterface processor) {
        displayImage(LnxDisplayImageOptionsBuilder
                .createLnxDisplayImageOptionsBuilder()
                .setUri(uri)
                .setImageView(imageView)
                .setBitmapProcessor(processor));
    }

    public void displayImage(LnxDisplayImageOptionsBuilder options) {
        this.opts = options;
        // 预处理
        if (options.getImageView() != null && options.getLoadingIconResId() > 0)
            options.getImageView().setBackgroundResource(options.getLoadingIconResId());

        // 看看uri是否为空，如果为空，则直接返回
        if (StringUtils.isEmpty(options.getUri())) {
            if (options.getUriEmptyIconResId() > 0 && options.getImageView() != null) {
                options.getImageView().setBackgroundResource(options.getUriEmptyIconResId());
            }
            return;
        }

        Bitmap bitmap = null;
        // 从缓存中去取
        if (options.isAllowFromCacheFlag()) {
            bitmap = imageCacheInstance.fromCache(options.getUri());
        }

        if (bitmap != null && !bitmap.isRecycled()) {
            innerProcessImage(bitmap, LnxLoadedFrom.FROM_CACHE);
            return;
        }

        // 如果缓存没有，从服务器获取
        new LnxLoadImageTask().setListener(innerOnImageLoadedListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, options.getUri());
    }

    private OnImageLoadedListener innerOnImageLoadedListener = new OnImageLoadedListener() {
        @Override
        public boolean onImageLoaded(Bitmap bitmap) {
            innerProcessImage(bitmap, LnxLoadedFrom.FROM_NETWORK);
            return true;
        }
    };

    private void innerProcessImage(Bitmap bitmap, LnxLoadedFrom from) {
        if (bitmap != null) {
            LogUtils.debug("innerProcessImage Get image from : " +
                    (from == LnxLoadedFrom.FROM_NETWORK ? "NETWORK" : "CACHE"));
        }

        Bitmap zoomBitmap = null;
        Bitmap processedBitmap = null;
        // 未取到图片
        if (bitmap == null && opts.getImageView() != null) {
            if (opts.getLoadErrorIconResId() > 0)
                opts.getImageView().setBackgroundResource(opts.getLoadErrorIconResId());
        } else {
            // 取到了图片
            //    先缓存
            if (opts.isAllowCacheOnDiskFlag() && from == LnxLoadedFrom.FROM_NETWORK) {
                imageCacheInstance.cache(opts.getUri(), bitmap);
            }
            // 根据willImageSize进行缩放
            if (opts.getWillImageSize() != null) {
                LnxImageSize oriSize = new LnxImageSize(bitmap.getWidth(), bitmap.getHeight());
                LnxImageSize targetSize = oriSize.computeTargetSize(opts.getWillImageSize());
                if (oriSize != targetSize) {
                    zoomBitmap = LnxBitmapHelper.zoomBitmap(bitmap, targetSize.getWidth(), targetSize.getHeight());
                }
            }
            if (zoomBitmap != null) {
                LnxBitmapHelper.releaseBitmap(bitmap);
                bitmap = zoomBitmap;
            }

            //     调用处理
            if (opts.getBitmapProcessor() != null) {
                processedBitmap = opts.getBitmapProcessor().process(bitmap);
            }
            if (processedBitmap != null) {
                LnxBitmapHelper.releaseBitmap(bitmap);
                bitmap = zoomBitmap;
            }
        }

        if (opts.getOnImageLoadedListener() != null) {
            boolean done = opts.getOnImageLoadedListener().onImageLoaded(bitmap);
            if (done)
                return;
        }

        LogUtils.debug("bitmap: "+bitmap.getWidth() + "*" + bitmap.getHeight());

        // 需要显示图片
        showImageView(bitmap);

    }

    private LnxDisplayImageOptionsBuilder opts = null;

//    private Bitmap releaseBitmapAfterDone(Bitmap newBitmap, Bitmap oriBitmap) {
//        if (newBitmap == null || newBitmap == oriBitmap)
//            return oriBitmap;
//        if (oriBitmap != null && !oriBitmap.isRecycled()) {
////            oriBitmap.recycle();
//        }
//        return newBitmap;
//    }

    private void showImageView(Bitmap bitmap) {
        if (bitmap == null || opts.getImageView() == null)
            return;
        if (opts.getWillImageSize() != null) {
            LnxViewHelper.setImage(opts.getImageView(), bitmap, bitmap.getWidth(), bitmap.getHeight());
        } else {
            LnxViewHelper.setImage(opts.getImageView(), bitmap);
        }
    }
}
