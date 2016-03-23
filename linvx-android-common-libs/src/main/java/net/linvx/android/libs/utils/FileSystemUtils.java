package net.linvx.android.libs.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by lizelin on 16/2/5.
 * 文件系统常用函数
 */
public class FileSystemUtils {
    /**
     * 获取可用空间，单位为M
     *
     * @param path
     * @return
     */
    public static long getFreeSpaceM(String path) {
        StatFs fs = new StatFs(path);
        return 1L * fs.getFreeBlocks() * fs.getBlockSize() / 1024L / 1024L;
    }

    /**
     * 判断sd卡是否已经ready
     *
     * @return
     */
    public static boolean isSdCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡的根目录
     *
     * @return
     */
    public static String getSdCardRootPath() {
        if (isSdCardAvailable()) {
            File sdDir = Environment.getExternalStorageDirectory();
            return sdDir.getAbsolutePath();
        } else
            return "";
    }

    /**
     * 获取SD卡上应用的存储根目录（注意，这个目录当应用卸载后不会被删除）
     *
     * @param context
     * @return
     */
    public static String getSdCardAppRootFilesDir(Context context) {
        if (isSdCardAvailable()) {
            File dir = context.getExternalFilesDir(null);
            if (dir != null)
                return dir.getAbsolutePath();
        }
        return "";
    }

    /**
     * 获取sd卡上应用的图片存储目录（在应用的根目录下）
     *
     * @param context
     * @return
     */
    public static String getSdCardAppPicturesFilesDir(Context context) {
        if (isSdCardAvailable()) {
            File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return dir.getAbsolutePath();
        }
        return "";
    }

    /**
     * 获取sd卡上应用的缓存目录
     *
     * @param context
     * @return
     */
    public static String getSdCardAppCacheDir(Context context) {
        if (isSdCardAvailable()) {
            File dir = context.getExternalCacheDir();
            if (dir != null)
                return dir.getAbsolutePath();
        }

        return "";
    }

    /**
     * 获取app在主ram上的文件根路径
     *
     * @param context
     * @return
     */
    public static String getAppRootFilesDir(Context context) {
        File dir = context.getFilesDir();
        if (dir != null)
            return dir.getAbsolutePath();
        else
            return "";
    }

    /**
     * 获取app在主ram上的缓存根路径
     *
     * @param context
     * @return
     */
    public static String getAppRootCacheDir(Context context) {
        File dir = context.getCacheDir();
        if (dir != null)
            return dir.getAbsolutePath();
        else
            return "";
    }

    /**
     * 删除一个目录下的文件（递归，并且起一个新的线程执行删除操作）
     *
     * @param folder
     */
    public static void delFilesByFolderInNewThread(final File folder) {
        if (folder == null)
            return;

        new Thread() {
            public void run() {
                delFiles(folder);
            }
        }.start();
    }

    /**
     * 删除一个目录下的文件（递归，并且起一个新的线程执行删除操作）
     *
     * @param folder
     */
    public static void delFilesByFolderInNewThread(final File folder, final OnFolderDeleteCompleteListener l) {
        if (folder == null)
            return;

        new Thread() {
            public void run() {
                delFiles(folder);
                l.onFolderDeleteComplete(folder);
            }
        }.start();
    }

    /**
     * 删除一个目录下的文件（递归）
     *
     * @param folder
     */
    private static void delFiles(File folder) {
        if (folder == null)
            return;

        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                delFiles(file);
            } else {
                file.delete();
            }
        }
    }

    /**
     * 删除目录下的某个文件
     *
     * @param theFilename
     * @param folder
     * @param context
     */
    public static void delFilesByFilename(final String theFilename, final File folder, final Context context) {
        if (theFilename == null || folder == null)
            return;

        new Thread() {
            public void run() {
                File[] files = folder.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (theFilename.equals(filename))
                            return true;
                        else
                            return false;
                    }
                });
                for (File file : files) {
                    file.delete();
                }
            }
        }.start();
    }

    /**
     * 获取目录大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFolderSizeM(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSizeM(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) (1.0f*size/1024/1024);
    }
}
