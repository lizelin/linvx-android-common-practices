package net.linvx.android.libs.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by lizelin on 16/4/6.
 */
public class MyApplication extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
