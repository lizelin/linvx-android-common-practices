package com.haishang360.inventory.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by lizelin on 16/3/30.
 */
public class HsBaseActivity extends AppCompatActivity {
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
