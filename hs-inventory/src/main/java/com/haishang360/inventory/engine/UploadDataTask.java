package com.haishang360.inventory.engine;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.haishang360.inventory.models.Inv;

import net.linvx.android.libs.dialog.LoadingDialog;
import net.linvx.android.libs.http.LnxCookie;
import net.linvx.android.libs.http.LnxHttpHelper;
import net.linvx.android.libs.http.LnxHttpResponse;
import net.linvx.android.libs.utils.AppUtils;
import net.linvx.android.libs.utils.LogUtils;
import net.linvx.android.libs.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizelin on 16/3/31.
 */
public class UploadDataTask extends AsyncTask<Void, Void, LnxHttpResponse> {
    private LoadingDialog dialog = null;
    private Activity activity;
    private String guid = "";
    private String toEmail = "";

    public UploadDataTask(Activity activity, String guid, String toEmail){
        this.activity = activity;
        this.guid = guid;
        this.toEmail = toEmail;
        dialog = new LoadingDialog(activity, "");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected void onPostExecute(LnxHttpResponse s) {
        super.onPostExecute(s);
        dialog.dismiss();
        dialog = null;
        if (s==null) {
            AppUtils.showMsg(activity, "服务器忙，请稍后再试！");
        }

        if (AppHelper.preParseResponse(activity, s)) {
            AppUtils.showMsg(activity, "邮件发送成功！");
        }  else {
            AppUtils.showMsg(activity, "服务器错误！");
        }

    }

    @Override
    protected LnxHttpResponse doInBackground(Void... params) {
        SQLiteDatabase db = DatabaseHelper.getReadableDb();
        List<Inv> list = Inv.getAlreadyInvStockFromDb(db, guid);
        db.close();
        if (list == null)
            return null;
        String format = "%s,\"%s\",\"%s\",%s,\"%s\"\n";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<list.size(); i++) {
            Inv inv = list.get(i);
            sb.append(String.format(format, ""+(i+1), inv.vc2mdse_code, inv.vc2mdse_name, ""+inv.numstock_count, ""+inv.vc2expiredesc));
        }

        String data = sb.toString();
        LogUtils.error(data);
        if (StringUtils.isNotEmpty(data))
        {
            try {

                String toServer = "email="+URLEncoder.encode(toEmail, "utf-8")+"&data="+ URLEncoder.encode(data, "utf-8");
                LogUtils.error(toServer);
                byte[] postData = StringUtils.getBytes(toServer);

                String cookie  = net.linvx.android.libs.utils.SharedPrefUtils.readFieldString(activity, Constants.SESSION_ID, "");
                List<LnxCookie> cookies = new ArrayList<LnxCookie>();
                LnxCookie c = new LnxCookie();
                c.setName(Constants.SESSION_ID).setValue(cookie);
                cookies.add(c);

                LnxHttpResponse res = LnxHttpHelper.httpPostResponse(Constants.URL_SERVER_PREFIX + "load_data", null, cookies, postData, "upload");
                return res;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
