package net.akhyar.android.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {

    private List<T> list;

    public BaseListAdapter() {
        super();
        this.list = new ArrayList<T>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    public abstract View getView(int pos, View view, ViewGroup parent);

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        if (list == null) {
            this.list = Collections.<T>emptyList();
        } else {
            this.list = list;
        }
    }

    public void mergeList(List<T> list, Comparator<T> sorter) {
        // TODO merge with list
    }

    public void prependList(List<T> list) {
        int i = 0;
        for (T item : list) {
            this.list.add(i, item);
            i++;
        }
    }

    public void appendList(List<T> list) {
        for (T item : list) {
            this.list.add(item);
        }
    }

}
