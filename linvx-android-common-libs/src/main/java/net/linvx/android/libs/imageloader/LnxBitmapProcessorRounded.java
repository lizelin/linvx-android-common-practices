package net.linvx.android.libs.imageloader;

import android.graphics.Bitmap;

import net.linvx.android.libs.ui.LnxBitmapHelper;


/**
 * Created by lizelin on 16/3/14.
 */
public class LnxBitmapProcessorRounded implements LnxBitmapProcessorInterface {
    @Override
    public Bitmap process(Bitmap bitmap) {
        return LnxBitmapHelper.roundedCornerBitmap(bitmap, 14);
    }
}
