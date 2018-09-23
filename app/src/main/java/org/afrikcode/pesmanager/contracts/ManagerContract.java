package org.afrikcode.pesmanager.contracts;

import org.afrikcode.pesmanager.models.Manager;

public interface ManagerContract {

    void getDetails();

    void registerDetails(Manager manager);

    void setMyToken(String token);
}
