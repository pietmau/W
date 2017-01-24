package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private ImageLoader() { /**/ }

    private static byte[] loadImageData(String url) throws IOException {
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

    public static Bitmap getBitmap(String url) throws IOException {
        byte[] data = loadImageData(url);
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
