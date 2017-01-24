package com.waracle.androidtest.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waracle.androidtest.R;
import com.waracle.androidtest.customimageview.SmartImageView;
import com.waracle.androidtest.fragment.pojo.Cake;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    // TODO Can you think of a better way to represent these items???
    // Use a list of Cakes
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
            SmartImageView smartImageView = (SmartImageView) root.findViewById(R.id.image);
            Cake cake = (Cake) getItem(position);
            title.setText(cake.getTitle());
            desc.setText(cake.getDesc());
            smartImageView.loadBitmap(cake.getImage());
        }
        return root;
    }

    public void setItems(List<Cake> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
}
