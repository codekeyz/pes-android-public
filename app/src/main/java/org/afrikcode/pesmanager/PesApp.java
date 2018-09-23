package org.afrikcode.pesmanager;

import android.app.Application;

import org.afrikcode.pesmanager.impl.DatabaseImp;

public class PesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseImp databaseImp = new DatabaseImp();
        databaseImp.enableOfflinePersistence();
    }
}
