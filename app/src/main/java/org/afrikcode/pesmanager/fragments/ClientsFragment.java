package org.afrikcode.pesmanager.fragments;

import android.app.Activity;
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
import org.afrikcode.pesmanager.adapter.ClientAdapter;
import org.afrikcode.pesmanager.base.BaseFragment;
import org.afrikcode.pesmanager.impl.ClientImpl;
import org.afrikcode.pesmanager.listeners.OnitemClickListener;
import org.afrikcode.pesmanager.models.Client;
import org.afrikcode.pesmanager.views.ClientView;

import java.util.List;


public class ClientsFragment extends BaseFragment<ClientImpl> implements ClientView, OnitemClickListener<Client>, SearchView.OnQueryTextListener {

    private ClientAdapter adapter;
    private AlertDialog dialog;

    private String serviceID, branchID, yearID, monthID, weekID, dayID;

    public ClientsFragment() {
        setTitle("Select a Client");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImpl(new ClientImpl());
        getImpl().setView(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.getItem(0);
        search.setVisible(true);

        HomeActivity activity = (HomeActivity) getContext();
        activity.getSearchView().setQueryHint("Search branches...");

        activity.getSearchView().setOnQueryTextListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null){
            Bundle b = getArguments();
            serviceID = b.getString("ServiceID");
            yearID = b.getString("YearID");
            monthID = b.getString("MonthID");
            weekID = b.getString("WeekID");
            dayID = b.getString("DayID");
        }

        branchID = new Utils().getBranchID((Activity) getContext());

        //setting a global layout manager
        getRv_list().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getRv_list().setHasFixedSize(true);

        adapter = new ClientAdapter();
        adapter.setOnclicklistener(this);

        getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new client
                showAddClientDialog();
            }
        });

        getSwipeRefresh().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImpl().getClientsinBranch(branchID);
            }
        });

        getImpl().getClientsinBranch(branchID);

    }

    @Override
    public void showLoadingIndicator() {
        super.showLoadingIndicator();
        getInfoText().setVisibility(View.VISIBLE);
        getInfoText().setText("Please wait... loading clients from database");
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();
        getInfoText().setVisibility(View.GONE);
    }

    private void showAddClientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        HomeActivity activity = (HomeActivity) getContext();
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_add_new_client_layout, null);

        final EditText et_name = view.findViewById(R.id.et_client_name);
        final EditText et_contact = view.findViewById(R.id.et_contact_number);
        final EditText et_address = view.findViewById(R.id.et_residential_address);

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
                String branchName = et_name.getText().toString().trim();
                String branchContact = et_contact.getText().toString().trim();
                String branchAddress = et_address.getText().toString().trim();

                Client c = new Client(branchName, branchContact, branchAddress, branchID);
                getImpl().addClient(c);

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
    public void ongetClients(List<Client> clientList) {
        hideErrorLayout();

        adapter.setItemList(clientList);
        getRv_list().setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getInfoText().setText("Select Client to view contributions made or add a new contribution.");
        getInfoText().setVisibility(View.VISIBLE);
    }

    @Override
    public void onclientListEmpty() {
        showErrorLayout("No Clients Added yet");
    }


    @Override
    public void onAddClient() {
        Toast.makeText(getContext(), "Client successfully added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Client data) {
        if (yearID != null && monthID != null && weekID != null) {
            if (getFragmentListener() != null) {
                Bundle b = new Bundle();
                b.putString("ServiceID", serviceID);
                b.putString("YearID", yearID);
                b.putString("MonthID", monthID);
                b.putString("WeekID", weekID);
                b.putString("DayID", dayID);

                b.putString("ClientID", data.getId());
                b.putString("ClientName", data.getName());
                TransactionsFragment tf = new TransactionsFragment();
                tf.setArguments(b);
                getFragmentListener().moveToFragment(tf);
            }
        } else {
            Toast.makeText(getContext(), data.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}
