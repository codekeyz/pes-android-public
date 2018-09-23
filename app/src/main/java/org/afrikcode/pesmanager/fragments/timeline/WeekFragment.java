package org.afrikcode.pesmanager.fragments.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.Utils;
import org.afrikcode.pesmanager.activities.HomeActivity;
import org.afrikcode.pesmanager.adapter.WeekAdapter;
import org.afrikcode.pesmanager.base.BaseFragment;
import org.afrikcode.pesmanager.decorator.ItemOffsetDecoration;
import org.afrikcode.pesmanager.impl.TimelineImpl;
import org.afrikcode.pesmanager.listeners.OnitemClickListener;
import org.afrikcode.pesmanager.models.Day;
import org.afrikcode.pesmanager.models.Month;
import org.afrikcode.pesmanager.models.Week;
import org.afrikcode.pesmanager.models.Year;
import org.afrikcode.pesmanager.views.TimeStampView;

import java.util.List;

import butterknife.BindArray;

public class WeekFragment extends BaseFragment<TimelineImpl> implements OnitemClickListener<Week>, TimeStampView {

    @BindArray(R.array.weeks_array)
    String[] weeks;
    private WeekAdapter mWeekAdapter;
    private String yearID, monthID, branchName, branchID;

    public WeekFragment() {
        setTitle("Select Week");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivity activity = (HomeActivity) getContext();
        Utils utils = new Utils();
        branchID = utils.getBranchID(activity);
        branchName = utils.getBranchName(activity);

        setImpl(new TimelineImpl(branchID, branchName));
        getImpl().setView(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.getArguments() != null) {
            Bundle b = this.getArguments();
            yearID = b.getString("YearID");
            monthID = b.getString("MonthID");
        }
        //setting a global layout manager
        getRv_list().setLayoutManager(new GridLayoutManager(getContext(), 2));
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(getContext(), R.dimen.recycler_grid_item_offset);
        getRv_list().addItemDecoration(itemOffsetDecoration);

        mWeekAdapter = new WeekAdapter();
        mWeekAdapter.setOnclicklistener(this);

        getFab().setVisibility(View.GONE);

        getSwipeRefresh().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImpl().getWeeksinMonth(yearID, monthID);
            }
        });

        getImpl().getWeeksinMonth(yearID, monthID);
    }

    @Override
    public void ongetWeeksinMonth(List<Week> weekList) {
        if (weekList.isEmpty()) {
            showErrorLayout("No weeks Added yet");
            return;
        }

        hideErrorLayout();

        // create adapter and pass data to it
        mWeekAdapter.setItemList(weekList);
        getRv_list().setAdapter(mWeekAdapter);
        mWeekAdapter.notifyDataSetChanged();

        getInfoText().setText("Select week to view transactions made by current branch or add a new week.");
        getInfoText().setVisibility(View.VISIBLE);
    }


    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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
    public void onClick(Week data) {
        if (data.isActive()){
            if (getFragmentListener() != null) {
                Bundle b = new Bundle();
                b.putString("BranchName", branchName);
                b.getString("BranchID", branchID);
                b.putString("YearID", yearID);
                b.putString("MonthID", monthID);
                b.putString("WeekID", data.getId());
                DayFragment df = new DayFragment();
                df.setArguments(b);
                getFragmentListener().moveToFragment(df);
            }
        }else {
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
    public void ongetDaysinWeek(List<Day> dayList) {
    }


}
