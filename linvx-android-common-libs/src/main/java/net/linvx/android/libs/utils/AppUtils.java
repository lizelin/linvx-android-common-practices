package net.linvx.android.libs.utils;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Application级别的常用函数
 */
public class AppUtils {

    private AppUtils() {
    }

    /**
     * 显示提示信息
     * 主要为了调试时使用，生产系统应使用net.linvx.android.ui包下的LnxToast
     *
     * @param context
     * @param msg
     */
    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取App的版本号（数字版本，在AndroidManifest.xml中，以android:versionCode="7" 形式定义）
     *
     * @param context
     * @return 版本号
     * @throws NameNotFoundException
     */
    public static int getApplicationVersionCode(Context context) throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
    }

    /**
     * 获取App的版本名称（字符串版本，在AndroidManifest.xml中，一般以android:versionName="1.0.7" 形式定义）
     *
     * @param context
     * @return 版本名称
     * @throws NameNotFoundException
     */
    public static String getApplicationVersionName(Context context) throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
    }


    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) return true;
            }
        } else if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) for (NetworkInfo anInfo : info)
                if (anInfo.getState() == NetworkInfo.State.CONNECTED) return true;
        }
        return false;
    }


    /**
     * 打开系统浏览器
     *
     * @param url     入口链接地址
     * @param context 操作句柄
     */
    public static void openBrowser(String url, Context context) {
        if (url == null || "".equals(url.trim()))
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 拨打电话
     *
     * @param url     tel:1500000000
     * @param context
     */
    public static void callPhone(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            AppUtils.showMsg(context, "您未授权本APP拨打电话权限！");
            return;
        }
        context.startActivity(intent);
    }


    /**
     * 获取内置浏览器的User-Agent
     * Double-Check
     *
     * @param activity
     * @return
     */
    public static String getUserAgent(Context activity) {
        if (userAgent == null) {
            synchronized (AppUtils.class) {
                if (userAgent == null) {
                    WebView view = new WebView(activity);
                    WebSettings settings = view.getSettings();
                    userAgent = settings.getUserAgentString();
                    settings = null;
                    view = null;
                }
            }
        }
        return userAgent;

    }

    /**
     * http user－agent 静态 线程安全 变量
     */
    private static volatile String userAgent = null;

    /**
     * 是否是debug版本
     *
     * @param context
     * @return
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 启动apk
     *
     * @param ctx
     * @param packageName
     */
    public static void startApkActivity(final Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(packageName, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(pi.packageName);

            List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;
                intent.setComponent(new ComponentName(packageName, className));
                ctx.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得应用下所有activity
     *
     * @param ctx
     * @return
     */
    public static ArrayList<String> getActivities(Context ctx) {
        ArrayList<String> result = new ArrayList<String>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(ctx.getPackageName());
        for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo.name);
        }
        return result;
    }

    /**
     * 判断是否UI主线程
     *
     * @return
     */
    public static boolean isUiThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * @param context
     * @return
     */
    public static String getManifestValue(Context context, String meta_data_name) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo == null)
            return "";
        else
            return appInfo.metaData.getString("CHANNEL_NAME");
    }

    /**
     * 启动activity
     *
     * @param context
     * @param c
     */
    public static void startActivity(Context context, Class c) {
        AppUtils.startActivity(context, c, null);
    }

    /**
     * 启动activity
     *
     * @param context
     * @param c
     * @param bundle
     */
    public static void startActivity(Context context, Class c, Bundle bundle) {
        Intent intent = new Intent(context, c);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }
}