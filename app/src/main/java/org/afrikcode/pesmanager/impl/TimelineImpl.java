package org.afrikcode.pesmanager.impl;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.afrikcode.pesmanager.base.BaseImp;
import org.afrikcode.pesmanager.contracts.TimeStampContract;
import org.afrikcode.pesmanager.models.Month;
import org.afrikcode.pesmanager.models.Week;
import org.afrikcode.pesmanager.models.Year;
import org.afrikcode.pesmanager.views.TimeStampView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimelineImpl extends BaseImp<TimeStampView> implements TimeStampContract {

    private CollectionReference yearsRef, monthsRef, mWeeksRef;
    private String amountIndex, totalIndex;

    public TimelineImpl(String branchID, String branchName) {
        DatabaseImp databaseImp = new DatabaseImp();
        yearsRef = databaseImp.getYearsReference();
        monthsRef = databaseImp.getMonthsReference();
        mWeeksRef = databaseImp.getWeeksReference();
        amountIndex = branchID.concat(branchName).concat("Total");
        totalIndex = branchID.concat(branchName).concat("Number");
    }

    @Override
    public void getYears() {
        getView().showLoadingIndicator();
        yearsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                getView().hideLoadingIndicator();
                List<Year> yearList = new ArrayList<>();
                for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();

                    Year year = new Year().maptoData(data);

                    if (data.get(amountIndex) != null) {
                        year.setTotalAmount(Double.valueOf(String.valueOf(data.get(amountIndex))));
                    } else {
                        year.setTotalAmount(0.0);
                    }

                    if (data.get(totalIndex) != null) {
                        year.setTotalTransactions(Double.valueOf(String.valueOf(data.get(totalIndex))));
                    } else {
                        year.setTotalTransactions(0);
                    }

                    year.setId(snapshot.getId());
                    yearList.add(year);
                }

                getView().ongetYears(yearList);
            }
        });
    }

    @Override
    public void getMonthsinYear(String yearID) {
        getView().showLoadingIndicator();
        monthsRef.whereEqualTo("yearID", yearID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                getView().hideLoadingIndicator();
                List<Month> monthList = new ArrayList<>();
                for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();

                    Month month = new Month().maptoData(data);
                    Log.d("Month" + amountIndex, data.toString());

                    if (data.get(amountIndex) != null) {
                        month.setTotalAmount(Double.valueOf(String.valueOf(data.get(amountIndex))));
                    } else {
                        month.setTotalAmount(0.0);
                    }

                    if (data.get(totalIndex) != null) {
                        month.setTotalTransactions(Double.valueOf(String.valueOf(data.get(totalIndex))));
                    } else {
                        month.setTotalTransactions(0);
                    }

                    month.setId(snapshot.getId());
                    monthList.add(month);
                }

                getView().ongetMonthsinYear(monthList);
            }
        });
    }

    @Override
    public void getWeeksinMonth(String yearID, String monthID) {
        getView().showLoadingIndicator();
        mWeeksRef.whereEqualTo("yearID", yearID).whereEqualTo("monthID", monthID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                getView().hideLoadingIndicator();
                List<Week> weekList = new ArrayList<>();
                for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();

                    Week week = new Week().maptoData(data);
                    if (data.get(amountIndex) != null) {
                        week.setTotalAmount(Double.valueOf(String.valueOf(data.get(amountIndex))));
                    } else {
                        week.setTotalAmount(0.0);
                    }

                    if (data.get(totalIndex) != null) {
                        week.setTotalTransactions(Double.valueOf(String.valueOf(data.get(totalIndex))));
                    } else {
                        week.setTotalTransactions(0);
                    }

                    week.setId(snapshot.getId());
                    weekList.add(week);
                }

                getView().ongetWeeksinMonth(weekList);
            }
        });
    }


}
