package net.akhyar.android.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FilteredListAdapter<T> extends BaseAdapter {

    private Filter<T> filter;
    private List<T> list;
    private List<T> shown;

    public FilteredListAdapter() {
        super();
        this.list = new ArrayList<T>();
        this.shown = new ArrayList<T>();
    }

    @Override
    public int getCount() {
        return shown.size();
    }

    @Override
    public T getItem(int position) {
        return shown.get(position);
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

    @Override
    public void notifyDataSetChanged() {
        if (filter == null) {
            shown = list;
        } else {
            shown.clear();
            for (T object : list) {
                if (filter.isShownFor(object))
                    shown.add(object);
            }
        }

        super.notifyDataSetChanged();
    }

    public Filter<T> getFilter() {
        return filter;
    }

    public void setFilter(Filter<T> filter) {
        this.filter = filter;
    }

    public static interface Filter<T> {
        public boolean isShownFor(T object);
    }
}
