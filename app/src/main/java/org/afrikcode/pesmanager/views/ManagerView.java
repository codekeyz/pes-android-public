package org.afrikcode.pesmanager.views;


import org.afrikcode.pesmanager.base.BaseView;
import org.afrikcode.pesmanager.models.Manager;

public interface ManagerView extends BaseView {

    void ongetManagerDetails(Manager manager);
    void onError(String error);

    void onUpdateManager();

}
