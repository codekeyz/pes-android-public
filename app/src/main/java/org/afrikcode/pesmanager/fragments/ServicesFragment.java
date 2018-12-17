package org.afrikcode.pesmanager.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.TimestampType;
import org.afrikcode.pesmanager.Utils;
import org.afrikcode.pesmanager.activities.HomeActivity;
import org.afrikcode.pesmanager.adapter.TimelineAdapter;
import org.afrikcode.pesmanager.base.BaseFragment;
import org.afrikcode.pesmanager.decorator.ItemOffsetDecoration;
import org.afrikcode.pesmanager.fragments.timeline.YearFragment;
import org.afrikcode.pesmanager.impl.TimelineImpl;
import org.afrikcode.pesmanager.listeners.OnitemClickListener;
import org.afrikcode.pesmanager.models.Day;
import org.afrikcode.pesmanager.models.Month;
import org.afrikcode.pesmanager.models.Service;
import org.afrikcode.pesmanager.models.Week;
import org.afrikcode.pesmanager.models.Year;
import org.afrikcode.pesmanager.views.TimeStampView;

import java.util.List;

public class ServicesFragment extends BaseFragment<TimelineImpl> implements SearchView.OnQueryTextListener, OnitemClickListener<Service>,TimeStampView {

    private TimelineAdapter<Service> mServiceAdapter;
    private AlertDialog dialog;

    public ServicesFragment() {
        setTitle("Available Services");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.getItem(0);
        search.setVisible(true);

        HomeActivity activity = (HomeActivity) getContext();
        activity.getSearchView().setQueryHint("Search for a service...");

        activity.getSearchView().setOnQueryTextListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils utils = new Utils();
        HomeActivity activity = (HomeActivity) getContext();
        setImpl(new TimelineImpl(utils.getBranchID(activity), utils.getBranchName(activity)));
        getImpl().setView(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setting a global layout manager
        getRv_list().setLayoutManager(new GridLayoutManager(getContext(), 2));
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(getContext(), R.dimen.recycler_grid_item_offset);
        getRv_list().addItemDecoration(itemOffsetDecoration);
        getRv_list().setHasFixedSize(true);

        mServiceAdapter = new TimelineAdapter<>();
        mServiceAdapter.setOnclicklistener(this);

        getFab().setVisibility(View.GONE);

        getSwipeRefresh().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImpl().getServices();
            }
        });

        getImpl().getServices();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mServiceAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mServiceAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onClick(Service data) {
        if (data.isActive()) {
            if (getFragmentListener() != null) {
                Bundle b = new Bundle();
                b.putString("ServiceID", data.getId());
                YearFragment mf = new YearFragment();
                mf.setArguments(b);

                getFragmentListener().moveToFragment(mf);
            }
        } else {
            Toast.makeText(getContext(), data.getName() + " not activated,", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoadingIndicator() {
        super.showLoadingIndicator();

        getInfoText().setText("Please wait... loading data from database");
        getInfoText().setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();

        getInfoText().setVisibility(View.GONE);
    }

    @Override
    public void ongetServices(List<Service> serviceList) {
        if (serviceList.isEmpty()) {
            getFab().setVisibility(View.VISIBLE);
            showErrorLayout("No Services Added yet");
            return;
        }

        hideErrorLayout();
        getFab().setVisibility(View.VISIBLE);

        // pass data to adapter

        mServiceAdapter.setItemList(serviceList);

        getRv_list().setAdapter(mServiceAdapter);
        mServiceAdapter.notifyDataSetChanged();

        getInfoText().setText("Select service to view transactions made by current branch or add a new year.");
        getInfoText().setVisibility(View.VISIBLE);

    }


    @Override
    public void ongetYears(List<Year> yearList) {

    }

    @Override
    public void ongetMonthsinYear(List<Month> monthList) {

    }

    @Override
    public void ongetWeeksinMonth(List<Week> weekList) {

    }

    @Override
    public void ongetDaysinWeek(List<Day> dayList) {

    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
