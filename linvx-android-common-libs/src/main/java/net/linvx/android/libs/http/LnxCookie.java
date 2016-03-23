package net.linvx.android.libs.http;

import net.linvx.android.libs.utils.DateUtils;
import net.linvx.android.libs.utils.StringUtils;

import java.text.ParseException;

/**
 * cookie class：
 * cookie format is ：
 * ck_client_id=9ae16b78-ab98-45b2-bdc2-cc6a8a21ba20; Expires=Sun, 13-Dec-2082 19:41:09 GMT; Path=/
 * <p>
 * Created by lizelin on 16/3/10.
 */
public class LnxCookie {
    /**
     * cookie name
     */
    private String name;
    /**
     * cookie value
     */
    private String value;
    /**
     * cookie domain
     */
    private String domain;
    /**
     * cookie expire time, eg:
     * Expires=Sun, 13-Dec-2082 19:41:09 GMT
     */
    private String expire;
    /**
     * cookie path
     */
    private String path;


    public LnxCookie() {
    }

    /**
     * 根据cookie字符串，解析成LnxCookie实例
     *
     * @param c cookie字符串，类似于：
     *          ck_client_id=9ae16b78-ab98-45b2-bdc2-cc6a8a21ba20; Expires=Sun, 13-Dec-2082 19:41:09 GMT; Path=/
     *          一般是http response中set-cookie header的值
     * @return
     */
    public static LnxCookie parseCookieString(String c) {
        if (StringUtils.isEmpty(c))
            return null;
        LnxCookie cookie = new LnxCookie();
        String[] parts = c.split(";");
        for (int i = 0; i < parts.length; i++) {
            String[] keyValue = parts[i].split("=");
            if (keyValue.length < 2)
                continue;
            String _key = keyValue[0].trim();
            String _value = keyValue[1].trim();
            if (i == 0) {
                cookie.setName(_key).setValue(_value);
                cookie.setValue(_value);
            } else if ("Expires".equalsIgnoreCase(_key)) {
                try {
                    cookie.setExpire(DateUtils.dateToGMTString(DateUtils.parseGMTString(_value)));
                } catch (ParseException e) {
                    cookie.setExpire(null);
                    e.printStackTrace();
                }
            } else if ("Path".equalsIgnoreCase(_key)) {
                cookie.setPath(_value);
            } else if ("Domain".equalsIgnoreCase(_key)) {
                cookie.setDomain(_value);
            }
        }
        return cookie;
    }

    /**
     * 将Lnx实例转回到cookie string：
     * 类似于：ck_client_id=9ae16b78-ab98-45b2-bdc2-cc6a8a21ba20; Expires=Sun, 13-Dec-2082 19:41:09 GMT; Path=/
     *
     * @return String
     */
    @Override
    public String toString() {
        //ck_client_id=9ae16b78-ab98-45b2-bdc2-cc6a8a21ba20; Expires=Sun, 13-Dec-2082 19:41:09 GMT; Path=/
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(StringUtils.nvlString(value, "")).append("; ");
        if (null != expire) {
            sb.append("Expires").append("=").append(expire).append("; ");
        }
        if (StringUtils.isEmpty(domain)) {
            sb.append("Domain").append("=").append(domain).append("; ");
        }
        if (StringUtils.isEmpty(path)) {
            sb.append("Path").append("=").append(path).append("; ");
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 将LnxCookie实例转为key＝value；形式，以符合http request header Cookie格式
     *
     * @return
     */
    public String toCookieHeaderFormat() {
        //ck_client_id=9ae16b78-ab98-45b2-bdc2-cc6a8a21ba20
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(StringUtils.nvlString(value, "")).append("; ");
        return sb.toString();
    }

    /**
     * 判断两个LnxCookie是否相同
     *
     * @param o
     * @return true/false
     */
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof LnxCookie && this.getName().equalsIgnoreCase(((LnxCookie) o).getName()))
            return true;
        else
            return super.equals(o);
    }

    public String getName() {
        return name;
    }

    public LnxCookie setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public LnxCookie setValue(String value) {
        this.value = value;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public LnxCookie setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getExpire() {
        return expire;
    }

    public LnxCookie setExpire(String expire) {
        this.expire = expire;
        return this;
    }

    public String getPath() {
        return path;
    }

    public LnxCookie setPath(String path) {
        this.path = path;
        return this;
    }
}
