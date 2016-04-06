package com.haishang360.inventory.engine;

import android.app.Activity;
import android.os.AsyncTask;

import net.linvx.android.libs.dialog.LoadingDialog;
import net.linvx.android.libs.http.LnxCookie;
import net.linvx.android.libs.http.LnxHttpHelper;
import net.linvx.android.libs.http.LnxHttpResponse;
import net.linvx.android.libs.utils.LogUtils;
import net.linvx.android.libs.utils.SharedPrefUtils;
import net.linvx.android.libs.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lizelin on 16/3/29.
 */
public class HttpTask extends AsyncTask<Void, Integer, LnxHttpResponse> {
    private String uri, postData;
    private HttpResponseHandler handler;
    private Activity activity;
    private String tid;
    private static AtomicInteger counter = new AtomicInteger(0);

    public void reset() {
        counter.set(0);
    }

    public HttpTask(String uri, String postData, HttpResponseHandler handler, Activity activity, String tid) {
        this.uri = uri;
        this.postData = postData;
        this.handler = handler;
        this.activity = activity;
        this.tid = tid;
    }

    @Override
    protected LnxHttpResponse doInBackground(Void... params) {

        String cookie  = net.linvx.android.libs.utils.SharedPrefUtils.readFieldString(activity, Constants.SESSION_ID, "");
        LogUtils.debug(cookie);
        List<LnxCookie> cookies = new ArrayList<LnxCookie>();
        LnxCookie c = new LnxCookie();
        c.setName(Constants.SESSION_ID).setValue(cookie);
        cookies.add(c);

        return LnxHttpHelper.httpPostResponse(this.uri, null, cookies,
                StringUtils.getBytes(postData), this.tid);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        LogUtils.debug("onPreExecute!");

        showLoadingDialog();
    }

    @Override
    protected void onPostExecute(LnxHttpResponse response) {

        super.onPostExecute(response);
        if (response !=null) {
            LnxCookie cookie = response.getResponseCookie(Constants.SESSION_ID);
            if (cookie != null) {
                SharedPrefUtils.saveField(activity, Constants.SESSION_ID, cookie.getValue());
                LogUtils.debug(cookie.getValue());
            }
        }
        dismissLoadingDialog();
        if (this.handler != null)
            this.handler.handleResponse(response);


    }

    @Override
    protected void onCancelled(LnxHttpResponse response) {
        super.onCancelled(response);
        dismissLoadingDialog();
    }

    private static LoadingDialog dialog = null;

    private void showLoadingDialog() {
        int curr = counter.getAndIncrement();
        if (curr == 0) {
            dialog = new LoadingDialog(activity, "");
            dialog.show();
        }
    }

    private void dismissLoadingDialog() {
        int curr = counter.decrementAndGet();
        if (curr == 0 && dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public interface  HttpResponseHandler {
        void handleResponse(LnxHttpResponse response);
    }

    public void exec() {
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
