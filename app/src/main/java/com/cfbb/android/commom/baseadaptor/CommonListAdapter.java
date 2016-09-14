package com.cfbb.android.commom.baseadaptor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 适配器基类
 * Created by MrChang45 on 2015-03-30
 */
public class CommonListAdapter<T> extends BaseAdapter {

    public List<T> mDataSource;
    protected Context mContext;

    public CommonListAdapter(Context context) {
        mContext = context;
        mDataSource = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public T getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void addItem(T item) {
        mDataSource.add(item);
    }

    public void addFirstItem(T item) {
        mDataSource.add(0, item);
    }

    public void removeItem(int position) {
        mDataSource.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(int position, T item) {
        mDataSource.add(position, item);
        notifyDataSetChanged();
    }

    public void addItem(T[] items) {
        mDataSource.addAll(Arrays.asList(items));
    }

    public void addAll(List<T> items) {
        mDataSource.addAll(items);
    }

    public void clear() {
        if (mDataSource.size() > 0) {
            mDataSource.clear();
        }
    }
}
