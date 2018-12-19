package org.afrikcode.pesmanager.models;

import org.afrikcode.pesmanager.base.BaseTimeline;

import java.util.Map;

public class Service extends BaseTimeline<Service> {

    public Service() {}

    public Service(String name) {
        setName(name);
    }

    @Override
    public Service maptoData(Map<String, Object> data) {
        Service m = new Service(data.get("name").toString());
        m.setActive(Boolean.valueOf(data.get("isActive").toString()));
        return m;
    }
}
