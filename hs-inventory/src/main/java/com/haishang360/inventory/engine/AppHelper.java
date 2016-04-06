package com.haishang360.inventory.engine;

import android.app.Activity;

import com.haishang360.inventory.activity.LoginActivity;

import net.linvx.android.libs.http.LnxHttpResponse;
import net.linvx.android.libs.utils.AppUtils;
import net.linvx.android.libs.utils.LogUtils;
import net.linvx.android.libs.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lizelin on 16/3/29.
 */
public class AppHelper {
    public static boolean preParseResponse(Activity activity, LnxHttpResponse response) {
        if (response == null) {
            AppUtils.showMsg(activity, "网络错误！");
            return false;
        }
        int res_code = response.getResponseCode();
        if (res_code != 200) {
            AppUtils.showMsg(activity, "网络错误！");
            return false;
        }
        if (StringUtils.isEmpty(response.getResponseString())) {
            AppUtils.showMsg(activity, "数据解析错误，返回内容为空！");
            return false;
        }
        String s = response.getResponseString();
        LogUtils.debug(s);
        JSONObject json = null;
        try {
            json = new JSONObject(response.getResponseString());
        } catch (JSONException e) {
            e.printStackTrace();
            AppUtils.showMsg(activity, "数据解析错误，返回内容不为JSON！");
            return false;
        }
        String code = json.optString("code");
        if (StringUtils.isEmpty(code)) {
            AppUtils.showMsg(activity, "数据解析错误，返回的JSON code字段为空！");
            return false;
        }
        if (code.equals("403")) {
            AppUtils.showMsg(activity, "请登录！");
            AppUtils.startActivity(activity, LoginActivity.class);
            return false;
        }
        if (!code.equals("200")) {
            AppUtils.showMsg(activity, json.optString("message"));
            return false;
        }

        response.addExtraData(Constants.HTTP_EXTRA_DATA_JSON_KEY, json);
        return true;
    }
}
