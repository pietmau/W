package com.waracle.androidtest;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Riad on 20/05/2015.
 */
public class StreamUtils {
    private static final String TAG = StreamUtils.class.getSimpleName();

    private StreamUtils() {
    }

    // Can you see what's wrong with this???
    // Possible Null Pointer Exception

    public static byte[] readUnknownFully(InputStream stream) throws IOException {
        // Read in stream of bytes
        byte[] bytes = new byte[0];
        if (stream == null) {
            return bytes;
        }
        ArrayList<Byte> data = new ArrayList<>();
        while (true) {
            int result = stream.read();
            if (result == -1) {
                break;
            }
            data.add((byte) result);
        }
        // Convert ArrayList<Byte> to byte[]
        bytes = new byte[data.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = data.get(i);
        }
        // Return the raw byte array.
        return bytes;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String parseCharset(String contentType) {
        if (contentType != null) {
            String[] params = contentType.split(",");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return "UTF-8";
    }
}
