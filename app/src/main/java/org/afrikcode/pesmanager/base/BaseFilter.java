package org.afrikcode.pesmanager.base;

import android.widget.Filter;

import java.util.ArrayList;

public abstract class BaseFilter<D, A extends BaseAdapter> extends Filter {

    private A adapter;

    public BaseFilter(A madapter) {
        super();
        this.adapter = madapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        return null;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.setFilteredList((ArrayList<D>) filterResults.values);
        adapter.notifyDataSetChanged();
    }
}
