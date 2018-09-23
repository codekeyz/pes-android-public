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
import org.afrikcode.pesmanager.models.Transaction;
import org.afrikcode.pesmanager.viewholder.TransactionVH;

import java.util.ArrayList;
import java.util.List;


public class TransactionAdapter extends BaseAdapter<Transaction, OnitemClickListener<Transaction>, TransactionVH> {

    @NonNull
    @Override
    public TransactionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionVH holder, int position) {
        final Transaction transaction = getFilteredList().get(position);
        holder.clientName.setText(transaction.getClientName());
        holder.amount.setText(String.valueOf(transaction.getAmount()));
        if (transaction.getCreatedAt() != null) {
            holder.date.setText(transaction.getCreatedAt());
        } else {
            holder.date.setVisibility(View.GONE);
        }

        if (getOnclicklistener() != null) {
            holder.parentCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnclicklistener().onClick(transaction);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new BaseFilter<Transaction, TransactionAdapter>(this) {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                List<Transaction> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = getItemList();
                } else {
                    for (Transaction c : getItemList()) {
                        if (c.getClientName().toLowerCase().contains(query.toLowerCase())) {
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
