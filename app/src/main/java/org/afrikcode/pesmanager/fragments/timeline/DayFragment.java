package org.afrikcode.pesmanager.fragments.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.Utils;
import org.afrikcode.pesmanager.activities.HomeActivity;
import org.afrikcode.pesmanager.adapter.TimelineAdapter;
import org.afrikcode.pesmanager.base.BaseFragment;
import org.afrikcode.pesmanager.decorator.ItemOffsetDecoration;
import org.afrikcode.pesmanager.fragments.ClientsFragment;
import org.afrikcode.pesmanager.impl.TimelineImpl;
import org.afrikcode.pesmanager.listeners.OnitemClickListener;
import org.afrikcode.pesmanager.models.Day;
import org.afrikcode.pesmanager.models.Month;
import org.afrikcode.pesmanager.models.Service;
import org.afrikcode.pesmanager.models.Week;
import org.afrikcode.pesmanager.models.Year;
import org.afrikcode.pesmanager.views.TimeStampView;

import java.util.List;

public class DayFragment extends BaseFragment<TimelineImpl> implements OnitemClickListener<Day>, TimeStampView, SearchView.OnQueryTextListener {

    private TimelineAdapter<Day> mDayAdapter;
    private String serviceID, yearID, monthID, weekID;

    public DayFragment() {
        setTitle("Select Day");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.getItem(0);
        search.setVisible(true);

        HomeActivity activity = (HomeActivity) getContext();
        activity.getSearchView().setQueryHint("Search for a day...");

        activity.getSearchView().setOnQueryTextListener(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivity activity = (HomeActivity) getContext();
        Utils utils = new Utils();

        if (this.getArguments() != null) {
            Bundle b = this.getArguments();
            serviceID = b.getString("ServiceID");
            yearID = b.getString("YearID");
            monthID = b.getString("MonthID");
            weekID = b.getString("WeekID");
        }

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

        mDayAdapter = new TimelineAdapter<>();
        mDayAdapter.setOnclicklistener(this);

        getFab().setVisibility(View.GONE);

        getSwipeRefresh().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImpl().getDaysinWeeK(yearID, monthID, weekID);
            }
        });

        getImpl().getDaysinWeeK(yearID, monthID, weekID);
    }

    @Override
    public void ongetDaysinWeek(List<Day> dayList) {
        if (dayList.isEmpty()) {
            showErrorLayout("No Days Added yet");
            return;
        }

        hideErrorLayout();

        // create adapter and pass data to it
        mDayAdapter.setItemList(dayList);
        getRv_list().setAdapter(mDayAdapter);
        mDayAdapter.notifyDataSetChanged();

        getInfoText().setText("Select Day to view transactions made by current branch or add a new week.");
        getInfoText().setVisibility(View.VISIBLE);
    }


    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ongetServices(List<Service> serviceList) {

    }

    @Override
    public void showLoadingIndicator() {
        super.showLoadingIndicator();
        getInfoText().setText("Please wait... loading weeks from database");
        getInfoText().setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();
        getInfoText().setVisibility(View.GONE);
    }

    @Override
    public void onClick(Day data) {
        if (data.isActive()) {
            if (getFragmentListener() != null) {
                Bundle b = new Bundle();
                b.putString("ServiceID", serviceID);
                b.putString("YearID", yearID);
                b.putString("MonthID", monthID);
                b.putString("WeekID", weekID);
                b.putString("DayID", data.getId());
                ClientsFragment cf = new ClientsFragment();
                cf.setArguments(b);
                getFragmentListener().moveToFragment(cf);
            }
        } else {
            Toast.makeText(getContext(), data.getName() + " not activated, Contact Administrator for help", Toast.LENGTH_SHORT).show();
        }
    }


    //***************************** This callbacks won't work in this fragment *****************//


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
    public boolean onQueryTextSubmit(String query) {
        mDayAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mDayAdapter.getFilter().filter(newText);
        return false;
    }
}
