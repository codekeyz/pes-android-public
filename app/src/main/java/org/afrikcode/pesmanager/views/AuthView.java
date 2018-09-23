package org.afrikcode.pesmanager.views;

import org.afrikcode.pesmanager.base.BaseView;
import org.afrikcode.pesmanager.models.Manager;

public interface AuthView extends BaseView {
    void onAuthSuccess();
    void onAuthError(String error);

    void onAccountCreateSuccess(Manager manager);
}
