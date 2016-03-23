package net.linvx.android.libs.imageloader;

import android.content.Context;
import android.graphics.Bitmap;

import net.linvx.android.libs.ui.LnxBitmapHelper;
import net.linvx.android.libs.utils.FileSystemUtils;
import net.linvx.android.libs.utils.OnFolderDeleteCompleteListener;
import net.linvx.android.libs.utils.StringUtils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by lizelin on 16/3/13.
 */
public class LnxImageCache {
    /**
     * 单例
     */
    private volatile static LnxImageCache instance;
    private int MIN_SPACE_FREE = 100; // 100M
    private int MAX_CACHE_SIZE = 80;  // 80M
    /**
     * 内存缓存
     */
    private HashMap<String, SoftReference<Bitmap>> mapMemoryBitmaps;
    /**
     * Cache folder for internal storage
     */
    private File cacheFolderInternal;
    /**
     * cache folder for sd card
     */
    private File cacheFolderSdCard;
    /**
     * 缓存路径
     */
    private File cacheFolder;
    /**
     * 缓存子目录
     */
    private final String cache_sub_directory = "/linvx";
    /**
     * 上下文
     */
    private Context context;

    private boolean isCacheEnabled = false;

    public static void init(Context context) {
        if (instance == null) {
            synchronized (LnxImageCache.class) {
                if (instance == null) {
                    instance = new LnxImageCache(context);
                }
            }
        }
    }


    public static LnxImageCache getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("LnxImageCache must be initialize by call LnxImageCache.init(Context context)! ");
        }
        return instance;
    }

    private LnxImageCache(Context context) {
        this.context = context;
        mapMemoryBitmaps = new HashMap<String, SoftReference<Bitmap>>();
        ensureCacheDir();
    }

    private void ensureCacheDir() {
        String cacheInternal = FileSystemUtils.getAppRootCacheDir(context);
        cacheFolderInternal = new File(cacheInternal + cache_sub_directory);

        String cacheSdCard = FileSystemUtils.getSdCardAppCacheDir(context);
        cacheFolderSdCard = new File(cacheSdCard + cache_sub_directory);

        if (!StringUtils.isEmpty(cacheSdCard)) {
            cacheFolder = cacheFolderSdCard;
        } else {
            cacheFolder = cacheFolderInternal;
        }
        cacheFolder.mkdirs();

        if (FileSystemUtils.getFreeSpaceM(cacheFolder.getAbsolutePath()) < MIN_SPACE_FREE
                || FileSystemUtils.getFolderSizeM(cacheFolder) > MAX_CACHE_SIZE) {
            isCacheEnabled = false;
            FileSystemUtils.delFilesByFolderInNewThread(cacheFolder, new OnFolderDeleteCompleteListener() {
                @Override
                public void onFolderDeleteComplete(File folder) {
                    if (FileSystemUtils.getFreeSpaceM(folder.getAbsolutePath()) > MIN_SPACE_FREE) {
                        isCacheEnabled = true;
                    }
                }
            });
        } else {
            isCacheEnabled = true;
        }
    }

    public Bitmap fromCache(String url) {
        Bitmap bitmap = null;
        SoftReference<Bitmap> softBitmap = mapMemoryBitmaps.get(url);

        if (softBitmap != null) {
            bitmap = softBitmap.get();
        }

        if (bitmap != null && !bitmap.isRecycled()) {
            return bitmap;
        }

        String filename = LnxFileNameGenerator.generate(url);

        File file = new File(cacheFolder, filename);

        bitmap = LnxBitmapHelper.loadBitmapFromFile(file.getAbsolutePath());


        SoftReference<Bitmap> softBitmap2 = new SoftReference<Bitmap>(bitmap);
        mapMemoryBitmaps.put(url, softBitmap2);

        return bitmap;
    }

    public boolean cache(String url, Bitmap bitmap) {
        if (bitmap == null || isCacheEnabled == false)
            return false;

        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
        mapMemoryBitmaps.put(url, softBitmap);

        String filename = LnxFileNameGenerator.generate(url);
        File file = new File(cacheFolder, filename);

        Bitmap.CompressFormat format = (url.toLowerCase().endsWith("png")) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
        LnxImageSize s = new LnxImageSize(bitmap.getWidth(), bitmap.getHeight());
        LnxImageSize snew = s.computeStoreSize(context);
        if (s == snew)
            LnxBitmapHelper.bitmap2File(bitmap, file.getAbsolutePath(), format);
        else
            LnxBitmapHelper.bitmap2File(LnxBitmapHelper.zoomBitmap(bitmap, snew.getWidth(), snew.getHeight()), file.getAbsolutePath(), format);
//        if (!bitmap.isRecycled())
//            bitmap.recycle();
        return true;
    }
}
