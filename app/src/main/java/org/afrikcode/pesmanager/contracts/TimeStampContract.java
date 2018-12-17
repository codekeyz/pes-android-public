package org.afrikcode.pesmanager.contracts;

public interface TimeStampContract {

    void getServices();

    void getYears(String serviceID);

    void getMonthsinYear(String yearID);

    void getWeeksinMonth(String yearID, String monthID);

    void getDaysinWeeK(String yearID, String monthID, String weekID);

}
