package org.afrikcode.pesmanager.models;

import org.afrikcode.pesmanager.base.BaseTimeline;

import java.util.Map;

public class Month extends BaseTimeline<Month> {

    private String yearID;

    public Month() {
    }

    public Month(String name, String yearID) {
        super.setName(name);
        this.yearID = yearID;
    }

    @Override
    public Map<String, Object> datatoMap() {
        Map<String, Object> map = super.datatoMap();
        map.put("yearID", getYearID());
        return map;
    }

    @Override
    public Month maptoData(Map<String, Object> data) {
        Month m = new Month(data.get("name").toString(), data.get("yearID").toString());
        m.setActive(Boolean.valueOf(data.get("isActive").toString()));
        return m;
    }

    public String getYearID() {
        return yearID;
    }

    public void setYearID(String yearID) {
        this.yearID = yearID;
    }
}
