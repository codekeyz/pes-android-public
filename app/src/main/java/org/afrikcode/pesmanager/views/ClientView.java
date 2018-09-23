package org.afrikcode.pesmanager.views;


import org.afrikcode.pesmanager.base.BaseView;
import org.afrikcode.pesmanager.models.Client;

import java.util.List;

public interface ClientView extends BaseView {

    void ongetClients(List<Client> clientList);

    void onclientListEmpty();

    void onAddClient();

    void onError(String error);
}
