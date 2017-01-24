package com.waracle.androidtest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class LoadDataTask extends AsyncTask<Void, Void, JSONArray> {
    private final String TAG = getClass().getSimpleName();
    private Callback callback;
    private JSONArray array;

    public LoadDataTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(PlaceholderFragment.JSON_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Can you think of a way to improve the performance of loading data
            // using HTTP headers???

            // Also, Do you trust any utils thrown your way????

            byte[] bytes = StreamUtils.readUnknownFully(in);

            // Read in charset of HTTP content.
            String charset = parseCharset(urlConnection.getRequestProperty("Content-Type"));

            // Convert byte array to appropriate encoded string.
            String jsonText = new String(bytes, charset);

            // Read string as JSON.
            array = new JSONArray(jsonText);
            return array;
        } catch (IOException | JSONException e) {
            Log.d(TAG, e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        if (callback != null) {
            callback.onJSONArrayDownloaded(jsonArray);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public JSONArray getData() {
        return array;
    }

    interface Callback {
        void onJSONArrayDownloaded(JSONArray jsonArray);
    }


    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public String parseCharset(String contentType) {
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
