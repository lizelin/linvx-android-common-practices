package net.linvx.android.libs.common;

import android.os.Handler;

public class MyTimer {
    private Runnable runWraper = null;
    private Handler handler = new Handler();
    private int first = 0;
    private int interval = 1;
    private int maxDoTimes = 0;
    private int curDoTimes = 0;
    private OnStopListener listener = null;
    private boolean isRuning = false;

    public MyTimer(final Runnable run, OnStopListener l, int first, int intval, final int max) {
        this.listener = l;
        this.first = first;
        this.interval = intval;
        this.maxDoTimes = max;
        this.runWraper = new Runnable(){
            @Override
            public void run() {
                run.run();
                curDoTimes++;
                if (maxDoTimes==0 || maxDoTimes>curDoTimes)
                    handler.postDelayed(runWraper, interval*1000);
                else
                    stop();
            }
        };
    }

    public void start(){
        if (isRuning)
            stop();
        isRuning = true;
        curDoTimes = 0;
        handler.postDelayed(runWraper, first*1000);
    }

    public void stop(){
        if (this.runWraper != null)
            handler.removeCallbacks(runWraper);
        if (this.listener!=null)
            this.listener.onStop();
        isRuning = false;
    }

    public static interface OnStopListener {
        public void onStop();
    }
}
