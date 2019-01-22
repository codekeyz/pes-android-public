package org.afrikcode.pesmanager.impl;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.afrikcode.pesmanager.base.BaseImp;
import org.afrikcode.pesmanager.contracts.TransactionContract;
import org.afrikcode.pesmanager.models.Transaction;
import org.afrikcode.pesmanager.views.TransactionView;

import java.util.ArrayList;
import java.util.List;


public class TransactionImpl extends BaseImp<TransactionView> implements TransactionContract {

    private CollectionReference transactionsRef;

    public TransactionImpl() {
        DatabaseImp databaseImp = new DatabaseImp();
        transactionsRef = databaseImp.getTransactionsReference();
    }


    @Override
    public void getTransactions(String branchID, String serviceID, String clientID, String yearID, String monthID, String weekID) {
        getView().showLoadingIndicator();
        transactionsRef.whereEqualTo("branchID", branchID)
                .whereEqualTo("serviceID", serviceID)
                .whereEqualTo("year", yearID)
                .whereEqualTo("month", monthID)
                .whereEqualTo("week", weekID)
                .whereEqualTo("clientID", clientID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        getView().hideLoadingIndicator();
                        List<Transaction> transactionList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                            Transaction transaction = new Transaction().maptoData(snapshot.getData());
                            transaction.setUid(snapshot.getId());
                            transactionList.add(transaction);
                        }

                        if (!transactionList.isEmpty()) {
                            getView().ongetTransactions(transactionList);
                        } else {
                            getView().onTransactionsEmpty();
                        }
                    }
                });
    }

    @Override
    public void addTransaction(Transaction mTransaction) {
        getView().showLoadingIndicator();
        transactionsRef.add(mTransaction.datatoMap()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                getView().hideLoadingIndicator();
                if (task.isSuccessful()) {
                    getView().onAddSuccess();
                } else {
                    getView().onError("Transaction could not be processed");
                }
            }
        });
    }

}
