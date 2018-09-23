package org.afrikcode.pesmanager.impl;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.afrikcode.pesmanager.base.BaseImp;
import org.afrikcode.pesmanager.contracts.AuthContract;
import org.afrikcode.pesmanager.models.Manager;
import org.afrikcode.pesmanager.views.AuthView;

public class AuthImp extends BaseImp<AuthView> implements AuthContract {

    private FirebaseAuth mAuth;

    public AuthImp() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean isAuthenticated() {
        return mAuth.getCurrentUser() != null;
    }

    @Override
    public String getUserID() {
        return mAuth.getCurrentUser().getUid();
    }

    @Override
    public void loginwithEmailandPassword(String email, String password) {
        if (email.isEmpty() || password.isEmpty())
            return;

        getView().showLoadingIndicator(); // show the loading indicator

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                getView().hideLoadingIndicator();
                if (task.isSuccessful()) {
                    //Process when login is successful
                    getView().onAuthSuccess();
                } else {
                    //Handling Login exceptions
                    getView().onAuthError("An error occurred while trying to authenticate you");
                }
            }
        });
    }

    @Override
    public void signup(final Manager manager) {
        getView().showLoadingIndicator();
        mAuth.createUserWithEmailAndPassword(manager.getEmail(), manager.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                getView().hideLoadingIndicator();
                if (task.isSuccessful()){
                    getView().onAccountCreateSuccess(manager);
                }else {
                    getView().onAuthError("An error occurred while creating your account");
                }
            }
        });
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    @Override
    public FirebaseAuth getAuth() {
        return mAuth;
    }

}
