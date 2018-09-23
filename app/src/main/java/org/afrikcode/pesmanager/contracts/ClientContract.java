package org.afrikcode.pesmanager.contracts;


import org.afrikcode.pesmanager.models.Client;

public interface ClientContract {

    void getClientsinBranch(String branchID);

    void addClient(Client client);
}
