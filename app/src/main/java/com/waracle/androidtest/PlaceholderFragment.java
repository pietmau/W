package com.waracle.androidtest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class PlaceholderFragment extends Fragment implements LoadDataTask.Callback {
    static String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";
    private static final String TAG = PlaceholderFragment.class.getSimpleName();

    private ListView mListView;
    private MyAdapter mAdapter;

    private static LoadDataTask loadDataTask;

    public PlaceholderFragment() { /**/ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list);
        initAdapter();
        initLoaderTask();
        return rootView;
    }

    private void initLoaderTask() {
        if (loadDataTask == null) {
            loadDataTask = new LoadDataTask(PlaceholderFragment.this);
            loadDataTask.execute();
        } else {
            if (loadDataTask.getData() != null) {
                onJSONArrayDownloaded(loadDataTask.getData());
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initAdapter() {
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (loadDataTask != null) {
            loadDataTask.setCallback(PlaceholderFragment.this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loadDataTask.setCallback(null);
    }

    @Override
    public void onJSONArrayDownloaded(JSONArray jsonArray) {
        mAdapter.setItems(jsonArray);
    }
}
