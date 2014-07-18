package net.akhyar.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author akhyar
 */
public abstract class ViewHolderListAdapter<T> extends BaseListAdapter<T> {

    public View getView(int pos, View view, ViewGroup parent) {
        ViewHolder<T> holder = null;
        if (view == null) {
            holder = createHolder();
            view = holder.createView(parent.getContext(), parent);
            view.setTag(holder);
        } else {
            holder = extractHolder(view);
        }

        holder.bind(pos, getItem(pos));

        return view;
    }

    public abstract ViewHolder<T> createHolder();

    @SuppressWarnings("unchecked")
    private ViewHolder<T> extractHolder(View view) {
        return (ViewHolder<T>) view.getTag();
    }

    public static abstract class ViewHolder<T> {
        public abstract View createView(Context context, ViewGroup parent);

        public abstract void bind(int pos, T object);
    }
}
