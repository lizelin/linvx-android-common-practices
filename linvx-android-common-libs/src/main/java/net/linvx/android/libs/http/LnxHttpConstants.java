package net.linvx.android.libs.http;

/**
 * Created by lizelin on 16/3/10.
 */
public interface LnxHttpConstants {
    /**
     * time out ms of connecting to server
     */
    public static final int DEFAULT_CONNECT_TIME_OUT_MS = 30 * 1000;

    /**
     * time out ms of read from server
     */
    public static final int DEFAULT_READ_TIME_OUT_MS = 30 * 1000;

    /**
     * Default charset is utf-8
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 缺省的post时，content－type http header
     */
    public static final String DEFAULT_POST_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * HTTP Method GET
     */
    public static final String HTTP_METHOD_GET = "GET";

    /**
     * HTTP Method POST
     */
    public static final String HTTP_METHOD_POST = "POST";

    public static final String DEFAULT_HTTP_CONNECTION_TID = "TID_DEFAULT";

    public static final String HTTP_HEADER_NAME_REQ_COOKIE = "Cookie";
    public static final String HTTP_HEADER_NAME_RES_COOKIE = "Set-Cookie";

    public static final String DEFAULT_USER_AGENT = "net.linvx.android.default.user.agent";
}
