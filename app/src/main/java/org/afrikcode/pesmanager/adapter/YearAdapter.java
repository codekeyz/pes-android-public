package org.afrikcode.pesmanager.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.base.BaseAdapter;
import org.afrikcode.pesmanager.listeners.OnitemClickListener;
import org.afrikcode.pesmanager.models.Year;
import org.afrikcode.pesmanager.viewholder.TimelineVH;


public class YearAdapter extends BaseAdapter<Year, OnitemClickListener<Year>, TimelineVH> {

    public YearAdapter() { }

    @NonNull
    @Override
    public TimelineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        return new TimelineVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineVH holder, int position) {
        final Year year = getItemList().get(position);
        holder.getName().setText(year.getName());
        holder.getTotalAmount().setText(String.valueOf(year.getTotalAmount()));

        if (year.isActive()) {
            holder.getStatusImage().setImageResource(R.drawable.status_active);
        } else {
            holder.getStatusImage().setImageResource(R.drawable.status_deactive);
        }

        if (getOnclicklistener() != null) {
            //set listener
            holder.getParent().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnclicklistener().onClick(year);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
