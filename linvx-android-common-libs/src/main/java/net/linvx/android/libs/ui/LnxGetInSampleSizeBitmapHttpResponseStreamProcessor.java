package net.linvx.android.libs.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import net.linvx.android.libs.http.LnxHttpResponse;
import net.linvx.android.libs.http.LnxHttpResponseStreamProcessor;

import java.io.InputStream;

/**
 * Created by lizelin on 16/3/20.
 */
public class LnxGetInSampleSizeBitmapHttpResponseStreamProcessor implements LnxHttpResponseStreamProcessor {
    @Override
    public boolean consumeInputStream(LnxHttpResponse response, InputStream is) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = this.getSampleSize();
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        response.addExtraData("Bitmap", bitmap);
        return true;

    }

    public LnxGetInSampleSizeBitmapHttpResponseStreamProcessor(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    private int sampleSize = 1;

    public int getSampleSize() {
        return sampleSize;
    }

    public LnxGetInSampleSizeBitmapHttpResponseStreamProcessor setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
        return this;
    }
}
