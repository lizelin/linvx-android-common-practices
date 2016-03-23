package net.linvx.android.libs.imageloader;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import net.linvx.android.libs.ui.LnxBitmapHelper;


/**
 * Created by lizelin on 16/3/14.
 */
public class LnxLoadImageTask extends AsyncTask<String, Integer, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... params) {
        if (params == null || params.length == 0)
            return null;
        String uri = params[0];
        return LnxBitmapHelper.loadBitmapFromNetwork(uri);

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (listener != null)
            listener.onImageLoaded(bitmap);
    }

    private OnImageLoadedListener listener;

    public LnxLoadImageTask setListener(OnImageLoadedListener listener) {
        this.listener = listener;
        return this;
    }
}
