package com.waracle.androidtest.customimageview;

import android.graphics.Bitmap;
import android.util.LruCache;

public class StringBitmapLruCache extends LruCache<String, Bitmap> {
    public StringBitmapLruCache(int cacheSize) {
        super(cacheSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        return bitmap.getByteCount() / 1024;
    }
}
