package org.afrikcode.pesmanager.contracts;

import org.afrikcode.pesmanager.models.Transaction;

public interface TransactionContract {
    void getTransactions(String branchID, String serviceID, String clientID, String yearID, String monthID, String weekID, String dayID);

    void addTransaction(Transaction mTransaction);
}
