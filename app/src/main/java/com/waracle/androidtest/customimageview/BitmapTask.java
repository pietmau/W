package com.waracle.androidtest.customimageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.waracle.androidtest.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Another AsyncTask unfortunately.
 * We use it to get the bitmaps.
 */
public class BitmapTask extends AsyncTask<Void, Void, Bitmap> {
    private final String TAG = getClass().getSimpleName();
    // Use a WeakReference to avoid memory leaks
    private final WeakReference<ImageView> imageViewReference;
    private final String url;
    private final LruCache<String, Bitmap> lruCache;

    public BitmapTask(ImageView imageView, String url, LruCache<String, Bitmap> lruCache) {
        imageViewReference = new WeakReference<>(imageView);
        this.url = url;
        this.lruCache = lruCache;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            return getBitmap(url);
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

    private byte[] loadImageData(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        InputStream inputStream = null;
        try {
            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
            }

            // TODO Can you think of a way to make the entire HTTP more efficient using HTTP headers??

            // We could cache the images, and check the expiry header, if they are not expired we use the cached images.
            // The headers give us information about the type of data and the size, we could use a buffer to read the file
            // in chunks rather that byte by byte

            return StreamUtils.readUnknownFully(inputStream);
        } finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            connection.disconnect();
        }
    }

    public Bitmap getBitmap(String url) throws IOException {
        Bitmap bitmap = lruCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        byte[] data = loadImageData(url);
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (url != null && bitmap != null) {
            lruCache.put(url, bitmap);
        }
        return bitmap;
    }
}