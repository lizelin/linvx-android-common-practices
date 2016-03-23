package net.linvx.android.libs.http;

import java.io.InputStream;

/**
 * Created by lizelin on 16/3/20.
 */
public interface LnxHttpResponseStreamProcessor {
    /**
     * 处理http的content
     * @param response
     * @param is
     * @return   是否已经处理了InputStream？
     */
    boolean consumeInputStream(LnxHttpResponse response, InputStream is);
}
