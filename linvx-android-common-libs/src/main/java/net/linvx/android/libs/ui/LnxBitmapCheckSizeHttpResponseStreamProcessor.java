package net.linvx.android.libs.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.linvx.android.libs.http.LnxHttpResponse;
import net.linvx.android.libs.http.LnxHttpResponseStreamProcessor;

import java.io.InputStream;

/**
 * Created by lizelin on 16/3/20.
 */
public class LnxBitmapCheckSizeHttpResponseStreamProcessor implements LnxHttpResponseStreamProcessor {
//    private  int maxImageStoreSize = 1024 ;
//    private  int maxImagePixels = 120*120;
//    private int minSideLength = 320;
    private  int maxImageStoreSize = 1024 * 1024 * 5;
    private  int maxImagePixels = 1024*1024;
    private int minSideLength = 1024;

    public int getMaxImageStoreSize() {
        return maxImageStoreSize;
    }

    public LnxBitmapCheckSizeHttpResponseStreamProcessor setMaxImageStoreSize(int maxImageStoreSize) {
        this.maxImageStoreSize = maxImageStoreSize;
        return this;
    }

    public int getMaxImagePixels() {
        return maxImagePixels;
    }

    public LnxBitmapCheckSizeHttpResponseStreamProcessor setMaxImagePixels(int maxImagePixels) {
        this.maxImagePixels = maxImagePixels;
        return this;
    }

    public int getMinSideLength() {
        return minSideLength;
    }

    public LnxBitmapCheckSizeHttpResponseStreamProcessor setMinSideLength(int minSideLength) {
        this.minSideLength = minSideLength;
        return this;
    }

    @Override
    public boolean consumeInputStream(LnxHttpResponse response, InputStream is) {
        int length = Integer.parseInt(response.getResponseHeaderFirstValue("Content-Length"));
        BitmapFactory.Options opt = new BitmapFactory.Options();
        if (length > maxImageStoreSize) {
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, opt);
            opt.inSampleSize = computeSampleSize(opt, minSideLength, maxImagePixels);
            response.addExtraData("BitmapFactory.inSampleSize", new Integer(opt.inSampleSize));

        } else {
            opt.inJustDecodeBounds = false;
            opt.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
            response.addExtraData("Bitmap", bitmap);
        }
        return true;
    }

    private   int computeSampleSize(BitmapFactory.Options options,
                                        int _minSideLength, int _maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, _minSideLength,
                _maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private  int computeInitialSampleSize(BitmapFactory.Options options,
                                            int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
