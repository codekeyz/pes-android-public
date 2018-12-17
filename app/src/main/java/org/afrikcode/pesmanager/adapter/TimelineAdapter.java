package org.afrikcode.pesmanager.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.base.BaseAdapter;
import org.afrikcode.pesmanager.base.BaseFilter;
import org.afrikcode.pesmanager.base.BaseTimeline;
import org.afrikcode.pesmanager.listeners.OnitemClickListener;
import org.afrikcode.pesmanager.viewholder.TimelineVH;

import java.util.ArrayList;
import java.util.List;

/***
 * Data Model
 * @param <T>
 */
public class TimelineAdapter<T extends BaseTimeline<T>> extends BaseAdapter<T, OnitemClickListener<T>, TimelineVH> {

    public TimelineAdapter() { }

    @NonNull
    @Override
    public TimelineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        return new TimelineVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineVH holder, int position) {
        final T item = getFilteredList().get(position);
        holder.getName().setText(item.getName());
        holder.getTotalAmount().setText(String.valueOf(item.getTotalAmount()));

        if (item.isActive()) {
            holder.getStatusImage().setImageResource(R.drawable.status_active);
        } else {
            holder.getStatusImage().setImageResource(R.drawable.status_deactive);
        }

        if (getOnclicklistener() != null) {
            //set listener
            holder.getParent().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnclicklistener().onClick(item);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new BaseFilter<T, TimelineAdapter>(this) {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                List<T> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = getItemList();
                } else {
                    for (T b : getItemList()) {
                        if (b.getName().toLowerCase().contains(query.toLowerCase())) {
                            filtered.add(b);
                        }
                    }
                }

                Filter.FilterResults results = new Filter.FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }
        };
    }
}
