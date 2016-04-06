package net.linvx.android.libs.http;

import android.support.annotation.Nullable;

import net.linvx.android.libs.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by lizelin on 16/3/23.
 */
public class LnxHttpHelper {
    private LnxHttpHelper() {
    }

    /**
     * http get helper
     *
     * @param uri
     * @return
     */
    public static String httpGet(String uri) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_GET, null, null, null, null).getResponseString();
    }

    /**
     * http get helper
     *
     * @param uri
     * @param tid
     * @return
     */
    public static String httpGet(String uri, String tid) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_GET, null, null, null, tid).getResponseString();
    }

    /**
     * http get helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @return
     */
    public static String httpGet(String uri, Map<String, String> headers, List<LnxCookie> cookies) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_GET, headers, cookies, null, null).getResponseString();
    }

    /**
     * http get helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @param tid
     * @return
     */
    public static String httpGet(String uri, Map<String, String> headers, List<LnxCookie> cookies, String tid) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_GET, headers, cookies, null, tid).getResponseString();
    }

    /**
     * http post helper
     *
     * @param uri
     * @param postData
     * @return
     */
    public static String httpPost(String uri, byte[] postData) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_POST, null, null, postData, null).getResponseString();
    }

    /**
     * http post helper
     *
     * @param uri
     * @param postData
     * @param tid
     * @return
     */
    public static String httpPost(String uri, byte[] postData, String tid) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_POST, null, null, postData, tid).getResponseString();
    }

    /**
     * http post helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @param postData
     * @return
     */
    public static String httpPost(String uri, Map<String, String> headers, List<LnxCookie> cookies, byte[] postData) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_POST, headers, cookies, postData, null).getResponseString();
    }

    /**
     * @param uri
     * @param headers
     * @param cookies
     * @param postData
     * @param tid
     * @return
     */
    public static String httpPost(String uri, Map<String, String> headers, List<LnxCookie> cookies, byte[] postData, String tid) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_POST, headers, cookies, postData, tid).getResponseString();
    }

    /**
     * 执行http request
     *
     * @param uri
     * @param method
     * @param headers
     * @param cookies
     * @param postData
     * @param tid
     * @return LnxHttpResponse
     */
    public static LnxHttpResponse execute(String uri,
                                          String method,
                                          @Nullable Map<String, String> headers,
                                          @Nullable List<LnxCookie> cookies,
                                           byte[] postData,
                                          @Nullable String tid
    ) {
        if (StringUtils.isEmpty(uri))
            return null;
        LnxHttpRequest req = null;
        if (StringUtils.isEmpty(method))
            method = LnxHttpConstants.HTTP_METHOD_GET;

        if (method.equalsIgnoreCase(LnxHttpConstants.HTTP_METHOD_GET))
            req = LnxHttpRequest.createHttpRequest(uri);
        else
            req = LnxHttpRequest.createHttpPostRequest(uri, postData);
        req.setTid(tid);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                req.addHttpHeader(entry.getKey(), entry.getValue());
            }
        }
        if (cookies != null) {
            for (LnxCookie cookie : cookies) {
                req.addHttpCookie(cookie);
            }
        }
        if (postData!=null && postData.length>0)
            req.setPostData(postData);

        return req.getResponse();
    }

    /**
     * http get helper
     *
     * @param uri
     * @return
     */
    public static LnxHttpResponse httpGetResponse(String uri) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_GET, null, null, null, null);
    }

    /**
     * http get helper
     *
     * @param uri
     * @param tid
     * @return
     */
    public static LnxHttpResponse httpGetResponse(String uri, String tid) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_GET, null, null, null, tid);
    }

    /**
     * http get helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @return
     */
    public static LnxHttpResponse httpGetResponse(String uri, Map<String, String> headers, List<LnxCookie> cookies) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_GET, headers, cookies, null, null);
    }

    /**
     * http get helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @param tid
     * @return
     */
    public static LnxHttpResponse httpGetResponse(String uri, Map<String, String> headers, List<LnxCookie> cookies, String tid) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_GET, headers, cookies, null, tid);
    }

    /**
     * http post helper
     *
     * @param uri
     * @param postData
     * @return
     */
    public static LnxHttpResponse httpPostResponse(String uri, byte[] postData) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_POST, null, null, postData, null);
    }

    /**
     * http post helper
     *
     * @param uri
     * @param postData
     * @param tid
     * @return
     */
    public static LnxHttpResponse httpPostResponse(String uri, byte[] postData, String tid) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_POST, null, null, postData, tid);
    }

    /**
     * http post helper
     *
     * @param uri
     * @param headers
     * @param cookies
     * @param postData
     * @return
     */
    public static LnxHttpResponse httpPostResponse(String uri, Map<String, String> headers, List<LnxCookie> cookies, byte[] postData) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_POST, headers, cookies, postData, null);
    }

    /**
     * @param uri
     * @param headers
     * @param cookies
     * @param postData
     * @param tid
     * @return
     */
    public static LnxHttpResponse httpPostResponse(String uri,
                                                   Map<String, String> headers,
                                                   List<LnxCookie> cookies,
                                                   byte[] postData,
                                                   String tid) {
        return LnxHttpHelper.execute(uri, LnxHttpConstants.HTTP_METHOD_POST, headers,
                cookies, postData, tid);
    }

}
