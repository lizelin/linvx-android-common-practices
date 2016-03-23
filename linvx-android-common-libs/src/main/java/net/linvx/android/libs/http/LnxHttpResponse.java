package net.linvx.android.libs.http;


import net.linvx.android.libs.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lizelin on 16/3/11.
 */
public class LnxHttpResponse {
    private String responseCharset;

    private byte[] responseBytes;

    private Map<String, List<String>> responseHeaders;

    private List<LnxCookie> responseCookies;

    private String tid;

    private int responseCode;

    public LnxHttpResponse() {
        initResponse();
    }

    public LnxHttpResponse initResponse() {
        setResponseCharset(LnxHttpConstants.DEFAULT_CHARSET);
        responseHeaders = new HashMap<String, List<String>>();
        responseCookies = new ArrayList<LnxCookie>();
        this.setTid(LnxHttpConstants.DEFAULT_HTTP_CONNECTION_TID);
        responseCode = 0;
        return this;
    }

    public void releaseMe() {
        this.setResponseBytes(null);
        if (null != getResponseCookies()) {
            getResponseCookies().clear();
            responseCookies = null;
        }
        if (null != getResponseHeaders()) {
            getResponseHeaders().clear();
            responseHeaders = null;
        }

        if (null!=this.extraDatas) {
            extraDatas.clear();
        }


    }

    public LnxHttpResponse(String tid) {
        initResponse();
        this.setTid(tid);
    }


    /**
     * 响应内容的编码
     */
    public String getResponseCharset() {
        return responseCharset;
    }

    public LnxHttpResponse setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
        return this;
    }

    /**
     * 响应数据
     */
    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public LnxHttpResponse setResponseBytes(byte[] responseBytes) {
        this.responseBytes = responseBytes;
        return this;
    }

    /**
     * 响应的headers
     */
    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public LnxHttpResponse fetchResponseHeaders(Map<String, List<String>> map) {
        this.getResponseHeaders().clear();
        this.getResponseHeaders().putAll(map);
        if (this.getResponseHeaders().containsKey(LnxHttpConstants.HTTP_HEADER_NAME_RES_COOKIE))
            this.getResponseHeaders().remove(LnxHttpConstants.HTTP_HEADER_NAME_RES_COOKIE);
        return this;
    }

    /**
     * 获取header信息
     *
     * @param name
     * @return
     */
    public List<String> getResponseHeader(String name) {
        if (StringUtils.isEmpty(name) || !this.getResponseHeaders().containsKey(name))
            return null;
        return getResponseHeaders().get(name);
    }

    /**
     * 获取header的第一个值（一般来讲都是只有一个值）
     *
     * @param name
     * @return
     */
    public String getResponseHeaderFirstValue(String name) {
        List<String> list = this.getResponseHeader(name);
        return list!=null && list.size()>0 ? list.get(0) : "";
    }


    /**
     * 响应的cookies（通过header Set-Cookie头获得）
     */
    public List<LnxCookie> getResponseCookies() {
        return responseCookies;
    }

    /**
     * 根据响应设置cookies
     *
     * @param cookiestrings
     * @return
     */
    public LnxHttpResponse fetchResponseCookies(List<String> cookiestrings) {
        this.getResponseCookies().clear();
        if (null != cookiestrings && cookiestrings.size() > 0) {
            for (String cookie_str : cookiestrings) {
                LnxCookie lc = LnxCookie.parseCookieString(cookie_str);
                if (null != lc)
                    this.getResponseCookies().add(lc);
            }
        }
        return this;
    }

    /**
     * 获取Cookie
     *
     * @param name
     * @return
     */
    public LnxCookie getResponseCookie(String name) {
        if (StringUtils.isEmpty(name))
            return null;
        for (LnxCookie c : responseCookies) {
            if (name.equals(c.getName()))
                return c;
        }
        return null;
    }

    /**
     * 事务ID，为异步回调服务
     */
    public String getTid() {
        return tid;
    }

    public LnxHttpResponse setTid(String tid) {
        this.tid = tid;
        return this;
    }

    /**
     * 获取字符串
     *
     * @return
     */
    public String getResponseString() {
        return StringUtils.getString(this.getResponseBytes(), this.getResponseCharset());
    }

    /**
     * 根据编码获取字符串
     *
     * @param charset
     * @return
     */
    public String getResponseString(String charset) {
        return StringUtils.getString(this.getResponseBytes(), charset);
    }

    public int getResponseCode() {
        return responseCode;
    }

    public LnxHttpResponse setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    private Map<String, Object> extraDatas = new HashMap<String, Object>();
    public Object getExtraData(String key) {
        if (extraDatas.containsKey(key))
            return extraDatas.get(key);
        else
            return null;
    }

    public LnxHttpResponse addExtraData(String key, Object obj) {
        if (StringUtils.isNotEmpty(key) && obj!=null)
            extraDatas.put(key, obj);
        return this;
    }
}
