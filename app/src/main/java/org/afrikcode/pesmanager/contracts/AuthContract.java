package org.afrikcode.pesmanager.contracts;

import com.google.firebase.auth.FirebaseAuth;

import org.afrikcode.pesmanager.models.Manager;

public interface AuthContract {

    void loginwithEmailandPassword(String email, String password);

    void signup(Manager manager);

    String getUserID();

    boolean isAuthenticated();

    void logout();

    FirebaseAuth getAuth();
}
