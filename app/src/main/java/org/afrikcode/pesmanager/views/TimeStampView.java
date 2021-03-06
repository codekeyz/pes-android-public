package org.afrikcode.pesmanager.views;

import org.afrikcode.pesmanager.base.BaseView;
import org.afrikcode.pesmanager.models.Day;
import org.afrikcode.pesmanager.models.Month;
import org.afrikcode.pesmanager.models.Service;
import org.afrikcode.pesmanager.models.Week;
import org.afrikcode.pesmanager.models.Year;

import java.util.List;

public interface TimeStampView extends BaseView {


    void ongetYears(List<Year> yearList);

    void ongetMonthsinYear(List<Month> monthList);

    void ongetWeeksinMonth(List<Week> weekList);

    void ongetDaysinWeek(List<Day> dayList);

    void onError(String error);

    void ongetServices(List<Service> serviceList);
}
