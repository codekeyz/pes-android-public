package org.afrikcode.pesmanager.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.Utils;
import org.afrikcode.pesmanager.activities.HomeActivity;
import org.afrikcode.pesmanager.adapter.TransactionAdapter;
import org.afrikcode.pesmanager.base.BaseFragment;
import org.afrikcode.pesmanager.impl.TransactionImpl;
import org.afrikcode.pesmanager.listeners.OnitemClickListener;
import org.afrikcode.pesmanager.models.Transaction;
import org.afrikcode.pesmanager.views.TransactionView;

import java.util.List;


public class TransactionsFragment extends BaseFragment<TransactionImpl> implements TransactionView, OnitemClickListener<Transaction>, SearchView.OnQueryTextListener {

    private TransactionAdapter mAdapter;
    private String yearID, monthID, weekID, dayID, clientID, clientName, branchID, branchName, managerID;
    private AlertDialog dialog;

    public TransactionsFragment() {
        setTitle("Transactions List");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImpl(new TransactionImpl());
        getImpl().setView(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.getItem(0);
        search.setVisible(true);

        HomeActivity activity = (HomeActivity) getContext();
        activity.getSearchView().setQueryHint("Search Client Record...");

        activity.getSearchView().setOnQueryTextListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Bundle b = getArguments();
            yearID = b.getString("YearID");
            monthID = b.getString("MonthID");
            weekID = b.getString("WeekID");
            dayID = b.getString("DayID");
            clientID = b.getString("ClientID");
            clientName = b.getString("ClientName");
        }
        HomeActivity activity = (HomeActivity) getContext();
        Utils utils = new Utils();

        branchID = utils.getBranchID(activity);
        branchName = utils.getBranchName(activity);
        managerID = utils.getManagerID(activity);

        //setting a global layout manager
        getRv_list().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getRv_list().setHasFixedSize(true);

        mAdapter = new TransactionAdapter();
        mAdapter.setOnclicklistener(this);

        getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new transaction
                showAddTransactionDialog();
            }
        });

        getSwipeRefresh().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImpl().getTransactions(branchID, clientID, yearID, monthID, weekID);
            }
        });

        getImpl().getTransactions(branchID, clientID, yearID, monthID, weekID);
    }

    private void showAddTransactionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        HomeActivity activity = (HomeActivity) getContext();
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_add_new_trans_layout, null);

        final EditText et_name = view.findViewById(R.id.et_client_name);
        et_name.setText(clientName);
        et_name.setEnabled(false);

        final EditText et_amount = view.findViewById(R.id.et_amount);

        Button cancel = view.findViewById(R.id.btn_cancel);
        Button okay = view.findViewById(R.id.btn_submit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = et_amount.getText().toString().trim();
                Transaction transaction = new Transaction(clientName, clientID, branchName, branchID, managerID, Double.valueOf(amount), yearID, monthID, weekID, dayID);
                getImpl().addTransaction(transaction);

                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.setView(view);

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void showLoadingIndicator() {
        super.showLoadingIndicator();

        getInfoText().setText("Please wait... processing current request");
        getInfoText().setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();
        getInfoText().setVisibility(View.GONE);
    }

    @Override
    public void onAddSuccess() {
        Toast.makeText(getContext(), "Transaction successfully added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ongetTransactions(List<Transaction> transactionList) {
        hideErrorLayout();

        mAdapter.setItemList(transactionList);
        getRv_list().setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        getInfoText().setText("Select Transaction to view details or add a new transaction.");
        getInfoText().setVisibility(View.VISIBLE);
    }

    @Override
    public void onTransactionsEmpty() {
        showErrorLayout("No Transactions yet");
    }

    @Override
    public void onClick(Transaction data) {
        Toast.makeText(getContext(), data.getClientName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return false;
    }
}
