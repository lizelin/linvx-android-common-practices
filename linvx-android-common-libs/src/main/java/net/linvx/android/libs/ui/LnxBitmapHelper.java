package net.linvx.android.libs.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;

import net.linvx.android.libs.http.LnxHttpResponse;
import net.linvx.android.libs.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Graphic相关的一些函数
 * Created by lizelin on 16/3/12.
 */
public class LnxBitmapHelper {


    private LnxBitmapHelper() {
    }

    /**
     * 根据网址获取bitmap
     *
     * @param uri
     * @return
     */
    public static Bitmap loadBitmapFromNetwork(String uri) {
        Bitmap b = null;
        LnxHttpResponse res = net.linvx.android.libs.http.LnxHttpRequest.createHttpRequest(uri)
                .setResponseStreamProcessor(new LnxBitmapCheckSizeHttpResponseStreamProcessor())
                .getResponse();
        if (res.getExtraData("Bitmap")!=null) {
            LogUtils.debug("loadBitmapFromNetwork: From extraData bitmap");
            return (Bitmap) res.getExtraData("Bitmap");
        }
        if (res.getExtraData("BitmapFactory.inSampleSize")!=null) {
            int size = (Integer) res.getExtraData("BitmapFactory.inSampleSize");
            res.releaseMe();
            LogUtils.debug("loadBitmapFromNetwork: From extraData inSampleSize :"+size);
            res = net.linvx.android.libs.http.LnxHttpRequest.createHttpRequest(uri)
                    .setResponseStreamProcessor(new LnxGetInSampleSizeBitmapHttpResponseStreamProcessor(size))
                    .getResponse();
            if (res.getExtraData("Bitmap")!=null) {
                LogUtils.debug("loadBitmapFromNetwork: From extraData Bitmap using inSampleSize :"+size);
                return (Bitmap) res.getExtraData("Bitmap");
            }
        }
        if (res.getResponseBytes()!=null && res.getResponseBytes().length>0) {
            LogUtils.debug("loadBitmapFromNetwork: From bytes");
            return BitmapFactory.decodeByteArray(res.getResponseBytes(), 0, res.getResponseBytes().length);
        }
        return null;
    }


    /**
     * 根据resource id获取bitmap
     *
     * @param context
     * @param resource_id
     * @return
     */
    public static Bitmap loadBitmapByResourceId(Context context, int resource_id) {
        return BitmapFactory.decodeResource(context.getResources(), resource_id);
    }

    /**
     * 从文件中创建bitmap
     *
     * @param file
     * @return
     */
    public static Bitmap loadBitmapFromFile(String file) {
        File f = new File(file);
        if (f != null && f.exists() && f.isFile())
            return BitmapFactory.decodeFile(file);
        else
            return null;
    }

    /**
     * drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * bitmap to drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap, Context context) {
        if (null == bitmap)
            return null;
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }

    /**
     * 将bitmap转为base64字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 将base64字符串转为bitmap
     *
     * @param string
     * @return
     */
    public static Bitmap base64ToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 压缩图片(循环压缩，直至大小小于maxKB)
     *
     * @param image
     * @param maxKB  最大的字节数（占用内存）
     * @param format
     * @return
     */
    private static Bitmap compressBitmap(Bitmap image, int maxKB, Bitmap.CompressFormat format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(format, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxKB) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 将view转化为bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap viewToBitmap(View view) {
        // Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        // Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            // does not have background drawable, then draw white background on
            // the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        // return the bitmap
        return returnedBitmap;
    }

    /**
     * 将view转化为bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap viewToBitmap1(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bm = view.getDrawingCache();
        return bm;
    }

    /**
     * 存储bitmap到文件
     *
     * @param bmp
     * @param filename 绝对路径
     * @param format   Bitmap.CompressFormat.JPEG， Bitmap.CompressFormat.PNG
     * @return
     */
    public static boolean bitmap2File(Bitmap bmp, String filename, Bitmap.CompressFormat format) {
        //Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        File f = new File(filename);
        if (f != null && f.exists()) {
            if (f.isFile())
                f.delete();
            else
                return false;
        }

        File dir = f.getParentFile();
        if (!dir.exists())
            dir.mkdirs();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            bmp.compress(format, quality, fos);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 根据url获取图片的bitmap
     *
     * @param uri
     * @return
     */
    public static Bitmap loadImageFromNetwork1(String uri) {
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new java.net.URL(uri).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }


    /**
     * 生成圆角图片
     *
     * @param bitmap
     * @return 圆角图片bitmap
     */
    public static Bitmap roundedCornerBitmap(Bitmap bitmap, int roundPx) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 缩放图片
     *
     * @param willWidth
     * @param willHeight
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int willWidth, int willHeight) {
        boolean isSame = false;
        if (willWidth == 0 && willHeight == 0)
            isSame = true;
        else if (willHeight == bitmap.getHeight() && willWidth == bitmap.getWidth())
            isSame = true;
        if (isSame)
            return bitmap.copy(bitmap.getConfig(), false);

        return Bitmap.createScaledBitmap(bitmap, willWidth, willHeight, true);

    }

    /**
     * 画阴影（阴影位置在四周环绕）
     *
     * @param originalBitmap
     * @param radius
     * @return
     */
    public static Bitmap drawBitmapShadow(Bitmap originalBitmap, int radius) {
        return drawBitmapShadow(originalBitmap, radius, 2);
    }

    /**
     * 画阴影
     *
     * @param originalBitmap
     * @param radius
     * @param direction      阴影位置：1，右下；2，居中（原图居中，阴影环绕）；3，右上
     * @return
     */
    public static Bitmap drawBitmapShadow(Bitmap originalBitmap, int radius, int direction) {

        BlurMaskFilter blurFilter = new BlurMaskFilter(radius,
                BlurMaskFilter.Blur.NORMAL);
        Paint shadowPaint = new Paint();
        shadowPaint.setAlpha(80);

        shadowPaint.setColor(Color.GRAY);
        shadowPaint.setMaskFilter(blurFilter);

        int[] offsetXY = new int[2];
        Bitmap shadowBitmap = originalBitmap
                .extractAlpha(shadowPaint, offsetXY);
        Bitmap canvasBgBitmap = Bitmap.createBitmap(shadowBitmap.getWidth(), shadowBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(canvasBgBitmap);
        canvas.drawBitmap(shadowBitmap, 0, 0, shadowPaint);
        if (direction == 1) {  // bottom and right
            offsetXY[0] = offsetXY[1] = 0;
        } else if (direction == 2) { // center
            offsetXY[0] = 0 - offsetXY[0];
            offsetXY[1] = 0 - offsetXY[1];
        } else if (direction == 3) {  // top and right
            offsetXY[0] = 0;
            offsetXY[1] = 0 - 2 * offsetXY[1];
        }

        canvas.drawBitmap(originalBitmap, offsetXY[0], offsetXY[1], null);
        shadowBitmap.recycle();
        return canvasBgBitmap;
    }

    /**
     * 设置图片倒影
     *
     * @param originalBitmap
     * @return
     */
    public static Bitmap createReflectedBitmap(Bitmap originalBitmap) {
        // 图片与倒影间隔距离
        final int reflectionGap = 4;

        // 图片的宽度
        int width = originalBitmap.getWidth();
        // 图片的高度
        int height = originalBitmap.getHeight();

        Matrix matrix = new Matrix();
        // 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
        matrix.preScale(1, -1);
        // 创建反转后的图片Bitmap对象，图片高是原图的一半。
        Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0,
                height / 2, width, height / 2, matrix, false);
        // 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。
        Bitmap withReflectionBitmap = Bitmap.createBitmap(width, (height
                + height / 2 + reflectionGap), Bitmap.Config.ARGB_8888);

        // 构造函数传入Bitmap对象，为了在图片上画图
        Canvas canvas = new Canvas(withReflectionBitmap);
        // 画原始图片
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        // 画间隔矩形
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

        // 画倒影图片
        canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, null);

        // 实现倒影效果
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                originalBitmap.getHeight(), 0,
                withReflectionBitmap.getHeight(), 0x70ffffff, 0x00ffffff,
                Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        // 覆盖效果
        canvas.drawRect(0, height, width, withReflectionBitmap.getHeight(),
                paint);

        return withReflectionBitmap;
    }

    /**
     * 释放图片资源
     *
     * @param bitmap
     */
    public static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null && bitmap.isRecycled() == false)
            bitmap.recycle();
        bitmap = null;
    }
}
