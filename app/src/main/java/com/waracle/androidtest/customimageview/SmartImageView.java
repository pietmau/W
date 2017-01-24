package com.waracle.androidtest.customimageview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * A simple ImageView that loads its content asynchronously
 */
public class SmartImageView extends ImageView {

    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Start a task that loads the bitmap and store the task in the drawable itself
    public void loadBitmap(String url, LruCache<String, Bitmap> lruCache) {
        if (cancelTaskIfUrlNotLongerRelevant(url)) {
            BitmapTask task = new BitmapTask(SmartImageView.this, url, lruCache);
            AsyncDrawable asyncDrawable = new AsyncDrawable(task);
            setImageDrawable(asyncDrawable);
            task.execute();
        }
    }

    // Retrieves the ongoing task from the ImageView and cancelTaskIfUrlNotLongerRelevant it if it's no longer
    // relevant
    private boolean cancelTaskIfUrlNotLongerRelevant(String url) {
        final BitmapTask task = getBitmapTask();
        if (task != null) {
            final String bitmapData = task.getUrl();
            if (bitmapData == null || !bitmapData.equalsIgnoreCase(url)) {
                task.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    // We retrieve the task
    private BitmapTask getBitmapTask() {
        final Drawable drawable = getDrawable();
        if (drawable instanceof AsyncDrawable) {
            final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
            return asyncDrawable.getBitmapWorkerTask();
        }
        return null;
    }
}
