package com.waracle.androidtest.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.waracle.androidtest.StreamUtils;
import com.waracle.androidtest.fragment.pojo.Cake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



/**
 * I use an AsyncTask because I cannot use external libraries (like Retrofit and Gson)
 * This AsyncTask will not leak the Activity/Fragment because the reference to the fragment (the Callback)
 * is removed when the fragment is detached.
 */
class LoadDataTask extends AsyncTask<Object, Object, List<Cake>> {
    private static final String TITLE = "title";
    private static final String DESC = "desc";
    private static final String IMAGE = "image";
    static final String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";
    private final String TAG = getClass().getSimpleName();
    private Callback callback;
    private List<Cake> cakes;

    public LoadDataTask(Callback callback, Context context) {
        this.callback = callback;
        enableHttpResponseCache(context);
    }

    @Override
    protected List<Cake> doInBackground(Object... voids) {
        HttpURLConnection urlConnection;
        URL url;
        try {
            url = new URL(JSON_URL);
        } catch (MalformedURLException e) {
            Log.d(TAG, e.toString());
            return null;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            return null;
        }
        InputStream in = null;
        try {
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            return null;
        }
        // TODO Can you think of a way to improve the performance of loading data using HTTP headers???
        // We can cache the results and check the expiration time form the header, if the cached results
        // are not stale we use them instead.
        // In any case I enable the HttpsURLConnection Cache

        // TODO Also, Do you trust any utils thrown your way????
        // Refactored calling method, added @NonNull annotation
        byte[] bytes = null;
        try {
            bytes = StreamUtils.readUnknownFully(in);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            return null;
        }
        String charset = StreamUtils.parseCharset(urlConnection.getRequestProperty("Content-Type"));
        String jsonText = null;
        try {
            jsonText = new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.toString());
            return null;
        }
        JSONArray array;
        try {
            array = new JSONArray(jsonText);
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
            return null;
        }
        urlConnection.disconnect();
        cakes = parseCakes(array);
        return cakes;
    }

    @Override
    protected void onPostExecute(List<Cake> jsonArray) {
        if (callback != null) {
            callback.onCakesDownloaded(jsonArray);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public List<Cake> getCakes() {
        return cakes;
    }



    private List<Cake> parseCakes(JSONArray array) {
        List<Cake> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                String title = object.getString(TITLE);
                String description = object.getString(DESC);
                String image = object.getString(IMAGE);
                list.add(new Cake(title,description,image));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private void enableHttpResponseCache(Context context) {
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(context.getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            Log.d(TAG, "HTTP response cache is unavailable.");
        }
    }

    interface Callback {
        void onCakesDownloaded(List<Cake> jsonArray);
    }
}
