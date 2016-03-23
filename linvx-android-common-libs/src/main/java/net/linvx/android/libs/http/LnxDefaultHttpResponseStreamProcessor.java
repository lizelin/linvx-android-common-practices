package net.linvx.android.libs.http;


import net.linvx.android.libs.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Created by lizelin on 16/3/20.
 */
public class LnxDefaultHttpResponseStreamProcessor implements LnxHttpResponseStreamProcessor {
    @Override
    public boolean consumeInputStream(LnxHttpResponse response, InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            int ch;
            GZIPInputStream gzip = null;
            String encoding = response.getResponseHeaderFirstValue("Content-Encoding");
            if (!StringUtils.isEmpty(encoding) && encoding.contains("gzip")) {
                gzip = new GZIPInputStream(is);
                while ((ch = gzip.read()) != -1) {
                    baos.write(ch);
                }
            }
            if (gzip == null) {
                while ((ch = is.read()) != -1) {
                    baos.write(ch);
                }
            }

            response.setResponseBytes(baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            response.setResponseBytes(null);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
