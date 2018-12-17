package org.afrikcode.pesmanager.contracts;

import com.google.firebase.firestore.CollectionReference;

public interface DatabaseContract {

    void enableOfflinePersistence();

    void disableOfflinePersistence();

    CollectionReference getManagersReference();

    CollectionReference getClientsReference();

    CollectionReference getTransactionsReference();

    CollectionReference getYearsReference();

    CollectionReference getMonthsReference();

    CollectionReference getWeeksReference();

    CollectionReference getDaysReference();

    CollectionReference getServicesReference();

}
