package com.waracle.androidtest.imageloader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.waracle.androidtest.ImageLoader;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Another AsyncTask unfortunately.
 * We use it to get the bitmaps.
 */
public class BitmapTask extends AsyncTask<Void, Void, Bitmap> {
    private final String TAG = getClass().getSimpleName();
    private final WeakReference<ImageView> imageViewReference;
    private final String url;

    public BitmapTask(ImageView imageView, String url) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.url = url;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            return ImageLoader.getBitmap(url);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public String getUrl() {
        return url;
    }
}