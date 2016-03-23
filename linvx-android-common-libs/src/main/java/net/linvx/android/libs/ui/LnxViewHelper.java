package net.linvx.android.libs.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by lizelin on 16/3/13.
 */
public class LnxViewHelper {
    private LnxViewHelper() {
    }

    /**
     * 使用TransitionDrawable实现渐变效果（比使用AlphaAnimation效果要好，可避免出现闪烁问题）
     *
     * @param imageView
     * @param bitmap
     * @param ms        动画毫秒数
     * @param context
     */
    public static void setImageViewFadeIn(ImageView imageView, Bitmap bitmap, int ms, Context context) {
        // Use TransitionDrawable to fade in.
        final TransitionDrawable td = new TransitionDrawable(
                new Drawable[]{new ColorDrawable(Color.TRANSPARENT),
                        new BitmapDrawable(context.getResources(), bitmap)});
        //noinspection deprecation
        imageView.setBackgroundDrawable(imageView.getDrawable());
        imageView.setImageDrawable(td);
        td.startTransition(ms);
    }


    /**
     * 设置View的大小
     *
     * @param v
     * @param w
     * @param h
     */
    public static void setViewSize(View v, int w, int h) {
        if (v == null || (w == 0 && h == 0))
            return;
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (params == null) {
            return;
        }
        if (params.width == w && params.height == h)
            return;
        if (w > 0) params.width = w;
        if (h > 0) params.height = h;
        v.setLayoutParams(params);
    }

    /**
     * 判断view是否是LinearLayout
     *
     * @param v
     * @return
     */
    public static boolean isLinearLayout(View v) {
        if (null == v)
            return false;
        if (v instanceof LinearLayout)
            return true;
        else
            return false;
    }

    /**
     * 判断view是否是RelativeLayout
     *
     * @param v
     * @return
     */
    public static boolean isRelativeLayout(View v) {
        if (null == v)
            return false;
        if (v instanceof RelativeLayout)
            return true;
        else
            return false;
    }

    /**
     * 设置图片以及imageview的大小
     *
     * @param iv
     * @param b
     * @param w
     * @param h
     */
    public static void setImage(ImageView iv, Bitmap b, int w, int h) {
        if (null == iv || b == null)
            return;
        LnxViewHelper.setViewSize(iv, w, h);
        iv.setImageBitmap(b);
    }

    /**
     * 设置图片
     *
     * @param iv
     * @param b
     */
    public static void setImage(ImageView iv, Bitmap b) {
        LnxViewHelper.setImage(iv, b, 0, 0);
    }


    /**
     * 获取view的大小（在oncreate之时，view尚未经历onMessure，故其大小可能为0，需要通过添加
     * OnGlobalLayoutListener 来延迟获取view大小）
     *
     * @param view
     * @param listener
     */
    public static void getViewSize(final View view, final OnGetViewSizeListener listener) {
        if (listener == null)
            return;

        if (view.getHeight() > 0 || view.getWidth() > 0) {
            listener.onGetViewSize(view, view.getWidth(), view.getHeight());
            return;
        }

        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= 16) {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        int w = view.getWidth(); // 获取宽度
                        int h = view.getHeight(); // 获取高度
                        if (listener != null) listener.onGetViewSize(view, w, h);
                    }
                });

    }


}
