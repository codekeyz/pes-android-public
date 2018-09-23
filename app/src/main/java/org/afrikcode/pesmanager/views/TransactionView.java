package org.afrikcode.pesmanager.views;


import org.afrikcode.pesmanager.base.BaseView;
import org.afrikcode.pesmanager.models.Transaction;

import java.util.List;

public interface TransactionView extends BaseView {

    void onAddSuccess();

    void onError(String error);

    void ongetTransactions(List<Transaction> transactionList);

    void onTransactionsEmpty();
}
