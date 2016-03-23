package net.linvx.android.libs.imageloader;

import android.content.Context;

import net.linvx.android.libs.utils.DeviceUtils;


/**
 * Created by lizelin on 16/3/13.
 */
public class LnxImageSize {
    private int height;
    private int width;

    public LnxImageSize(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 计算存储的图片大小，为避免资源浪费，最宽取屏幕宽度
     * @param context
     * @return
     */
    public LnxImageSize computeStoreSize(Context context) {
        int maxWidth = DeviceUtils.getScreenWidth(context);
        if (width > maxWidth) {
            int h = (int)(1.0f*this.getHeight()*maxWidth/this.getWidth());
            return new LnxImageSize(maxWidth, h);
        } else {
            return this;
        }
    }

    /**
     * 计算等比压缩后的图片大小
     * @param willSize
     * @return
     */
    public LnxImageSize computeTargetSize(LnxImageSize willSize) {
        int willH, willW, finalH, finalW;
        willH = willSize.getHeight();
        willW = willSize.getWidth();
        if (willH==0 && willW == 0)
            return this;
        if (willH == this.getHeight() && willW == this.getWidth())
            return this;
        if (willW!=0) {
            finalW = willW;
            finalH = (int )(1.0f*this.getHeight() * finalW / this.getWidth());
            return new LnxImageSize(finalW, finalH);
        }
        if (willH!=0) {
            finalH = willH;
            finalW = (int )(1.0f*this.getWidth() * finalH / this.getHeight());
            return new LnxImageSize(finalW, finalH);
        }

        float scaleH = 1.0f*this.getHeight()/willH;
        float scaleW = 1.0f*this.getWidth()/willW;
        float scale = scaleW>scaleH?scaleW:scaleH;
        finalW = (int)(this.getWidth()/scale);
        finalH = (int)(this.getHeight()/scale);
        return new LnxImageSize(finalW, finalH);
    }
}
