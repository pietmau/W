package com.waracle.androidtest.imageloader;

import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<BitmapTask> weakReference;
    
    public BitmapTask getBitmapWorkerTask() {
        return weakReference.get();
    }

    public AsyncDrawable(BitmapTask task) {
        weakReference = new WeakReference<BitmapTask>(task);
    }
}