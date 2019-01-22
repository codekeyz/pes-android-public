package org.afrikcode.pesmanager.impl;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.afrikcode.pesmanager.base.BaseImp;
import org.afrikcode.pesmanager.contracts.TimeStampContract;
import org.afrikcode.pesmanager.models.Day;
import org.afrikcode.pesmanager.models.Month;
import org.afrikcode.pesmanager.models.Service;
import org.afrikcode.pesmanager.models.Week;
import org.afrikcode.pesmanager.models.Year;
import org.afrikcode.pesmanager.views.TimeStampView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimelineImpl extends BaseImp<TimeStampView> implements TimeStampContract {

    private CollectionReference servicesRef, yearsRef, monthsRef, mWeeksRef, mDaysRef;
    private String branchID, branchName;

    public TimelineImpl(String branchID, String branchName) {
        this.branchID = branchID;
        this.branchName = branchName;
        DatabaseImp databaseImp = new DatabaseImp();
        servicesRef = databaseImp.getServicesReference();
        yearsRef = databaseImp.getYearsReference();
        monthsRef = databaseImp.getMonthsReference();
        mWeeksRef = databaseImp.getWeeksReference();
        mDaysRef = databaseImp.getDaysReference();
    }

    @Override
    public void getServices() {
        getView().showLoadingIndicator();
        servicesRef
                .whereEqualTo("branchID", branchID)
                .whereEqualTo("branchName", branchName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        getView().hideLoadingIndicator();
                        List<Service> serviceList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                            Map<String, Object> data = snapshot.getData();

                            Service service = new Service().maptoData(data);
                            service.setId(snapshot.getId());
                            serviceList.add(service);
                        }

                        getView().ongetServices(serviceList);
                    }
                });
    }

    @Override
    public void getYears(String serviceID) {
        getView().showLoadingIndicator();
        yearsRef.whereEqualTo("serviceID", serviceID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                getView().hideLoadingIndicator();
                List<Year> yearList = new ArrayList<>();
                for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();

                    Year year = new Year().maptoData(data);
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
                    week.setId(snapshot.getId());
                    weekList.add(week);
                }

                getView().ongetWeeksinMonth(weekList);
            }
        });
    }

    @Override
    public void getDaysinWeeK(String yearID, String monthID, String weekID) {
        getView().showLoadingIndicator();
        mDaysRef.whereEqualTo("yearID", yearID).whereEqualTo("monthID", monthID).whereEqualTo("weekID", weekID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                getView().hideLoadingIndicator();
                List<Day> dayList = new ArrayList<>();
                for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();

                    Day day = new Day().maptoData(data);
                    day.setId(snapshot.getId());
                    dayList.add(day);
                }

                getView().ongetDaysinWeek(dayList);
            }
        });
    }
}
