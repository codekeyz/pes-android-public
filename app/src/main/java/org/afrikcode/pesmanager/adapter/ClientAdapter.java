package org.afrikcode.pesmanager.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.base.BaseAdapter;
import org.afrikcode.pesmanager.base.BaseFilter;
import org.afrikcode.pesmanager.listeners.OnitemClickListener;
import org.afrikcode.pesmanager.models.Client;
import org.afrikcode.pesmanager.viewholder.ClientVH;

import java.util.ArrayList;
import java.util.List;


public class ClientAdapter extends BaseAdapter<Client, OnitemClickListener<Client>, ClientVH> {


    public ClientAdapter() {
    }

    @NonNull
    @Override
    public ClientVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        return new ClientVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientVH holder, int position) {
        final Client client = getFilteredList().get(position);
        holder.clientName.setText(client.getName());
        holder.clientContact.setText(client.getTelephone());

        if (getOnclicklistener() != null) {
            holder.parentCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnclicklistener().onClick(client);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new BaseFilter<Client, ClientAdapter>(this) {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                List<Client> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = getItemList();
                } else {
                    for (Client c : getItemList()) {
                        if (c.getName().toLowerCase().contains(query.toLowerCase())) {
                            filtered.add(c);
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
