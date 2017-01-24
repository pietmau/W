package com.waracle.androidtest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waracle.androidtest.R;
import com.waracle.androidtest.adapter.MyAdapter;
import com.waracle.androidtest.fragment.pojo.Cake;

import java.util.List;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class PlaceholderFragment extends Fragment implements LoadDataTask.Callback {
    private ListView mListView;
    private MyAdapter mAdapter;

    private LoadDataTask loadDataTask;

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
            loadDataTask = new LoadDataTask(PlaceholderFragment.this, getActivity().getApplicationContext());
            loadDataTask.execute();
        } else {
            if (loadDataTask.getCakes() != null) {
                onCakesDownloaded(loadDataTask.getCakes());
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment, so that we don't lose the network request (that might be still executing or already completed)
        // other approaches are available
        setRetainInstance(true);
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
    public void onCakesDownloaded(List<Cake> cakes) {
        mAdapter.setItems(cakes);
    }
}
