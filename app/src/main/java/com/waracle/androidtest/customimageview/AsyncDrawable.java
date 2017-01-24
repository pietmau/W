package com.waracle.androidtest.customimageview;

import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * A Drawable that loads its bitmap using an AsyncTask
 */
public class AsyncDrawable extends BitmapDrawable {
    // Weak reference to avoid memory leaks
    private final WeakReference<BitmapTask> weakReference;

    public AsyncDrawable(BitmapTask task) {
        weakReference = new WeakReference<>(task);
    }

    public BitmapTask getBitmapWorkerTask() {
        return weakReference.get();
    }

}