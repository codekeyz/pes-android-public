package org.afrikcode.pesmanager.models;

import java.util.HashMap;
import java.util.Map;

public class Transaction {

    private String uid;
    private String clientID;
    private String clientName;
    private String serviceID;
    private String branchID;
    private String branchName;
    private String managerID;
    private double amount;

    private String yearID;
    private String monthID;
    private String weekID;
    private String dayID;
    private String createdAt;

    public Transaction() {
    }

    public Transaction(String serviceID, String clientName, String clientID, String branchName, String branchID, String managerID, double amount, String yearID, String monthID, String weekID, String dayID) {
        this.serviceID = serviceID;
        this.clientID = clientID;
        this.clientName = clientName;
        this.branchID = branchID;
        this.managerID = managerID;
        this.amount = amount;
        this.yearID = yearID;
        this.monthID = monthID;
        this.weekID = weekID;
        this.dayID = dayID;
        this.branchName = branchName;
    }

    public Map<String, Object> datatoMap() {
        Map<String, Object> data = new HashMap<>();

        // Ensuring that we don't pass null values which can override existing database data

        if (serviceID != null){
            data.put("serviceID", serviceID);
        }
        if (clientID != null) {
            data.put("clientID", clientID);
        }
        if (clientName != null) {
            data.put("clientName", clientName);
        }
        if (branchID != null) {
            data.put("branchID", branchID);
        }
        if (managerID != null) {
            data.put("managerID", managerID);
        }

            data.put("amount", amount);

        if (yearID != null) {
            data.put("year", yearID);
        }
        if (monthID != null) {
            data.put("month", monthID);
        }
        if (weekID != null) {
            data.put("week", weekID);
        }
        if (dayID != null) {
            data.put("day", dayID);
        }
        if (uid != null) {
            data.put("uid", uid);
        }
        if (branchName != null) {
            data.put("branchName", branchName);
        }
        return data;
    }

    public Transaction maptoData(Map<String, Object> data) {
        Transaction m = new Transaction();
        if (data.get("serviceID") != null) {
            m.setServiceID(data.get("serviceID").toString());
        }
        if (data.get("clientID") != null) {
            m.setClientID(data.get("clientID").toString());
        }
        if (data.get("clientName") != null) {
            m.setClientName(data.get("clientName").toString());
        }
        if (data.get("branchID") != null) {
            m.setBranchID(data.get("branchID").toString());
        }
        if (data.get("managerID") != null) {
            m.setManagerID(data.get("managerID").toString());
        }
        if (data.get("year") != null) {
            m.setYearID(data.get("year").toString());
        }
        if (data.get("month") != null) {
            m.setMonthID(data.get("month").toString());
        }
        if (data.get("week") != null) {
            m.setWeekID(data.get("week").toString());
        }
        if (data.get("day") != null) {
            m.setDayID(data.get("day").toString());
        }
        if (data.get("branchName") != null) {
            m.setBranchName(data.get("branchName").toString());
        }
        if (data.get("uid") != null) {
            m.setUid(data.get("uid").toString());
        }
        m.setAmount(Double.valueOf(data.get("amount").toString()));

        if (data.get("createdAt") != null) {
            m.setCreatedAt(data.get("createdAt").toString());
        }
        return m;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String time) {
        this.createdAt = time;
    }

    public String getYearID() {
        return yearID;
    }

    public void setYearID(String yearID) {
        this.yearID = yearID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getMonthID() {
        return monthID;
    }

    public void setMonthID(String monthID) {
        this.monthID = monthID;
    }

    public String getWeekID() {
        return weekID;
    }

    public void setWeekID(String weekID) {
        this.weekID = weekID;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDayID() {
        return dayID;
    }

    public void setDayID(String dayID) {
        this.dayID = dayID;
    }
}
