package com.waracle.androidtest.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waracle.androidtest.R;
import com.waracle.androidtest.fragment.pojo.Cake;
import com.waracle.androidtest.imageloader.AsyncDrawable;
import com.waracle.androidtest.imageloader.BitmapTask;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    // TODO Can you think of a better way to represent these items???
    // use a list of Cakes
    private List<Cake> mItems;

    public MyAdapter() {
        mItems = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // We should be using a ViewHolder (and a RecyclerView)
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.list_item_layout, parent, false);
        if (root != null) {
            TextView title = (TextView) root.findViewById(R.id.title);
            TextView desc = (TextView) root.findViewById(R.id.desc);
            ImageView image = (ImageView) root.findViewById(R.id.image);
            Cake cake = (Cake) getItem(position);
            title.setText(cake.getTitle());
            desc.setText(cake.getDesc());
            loadBitmap(cake.getImage(), image);
        }
        return root;
    }

    public void setItems(List<Cake> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    // Start a task that loads the bitmap and store the task in the drawable itself
    private void loadBitmap(String url, ImageView imageView) {
        if (cancelPotentialWork(url, imageView)) {
            BitmapTask task = new BitmapTask(imageView, url);
            AsyncDrawable asyncDrawable = new AsyncDrawable(task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute();
        }
    }

    // We retrieve the ongoing task from the ImageView and cancel it if it's no longer
    // relevant
    private static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapTask task = getBitmapTask(imageView);
        if (task != null) {
            final String bitmapData = task.getUrl();
            if (bitmapData == null || bitmapData.equalsIgnoreCase(data)) {
                task.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    // We retrieve the task
    private static BitmapTask getBitmapTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}
