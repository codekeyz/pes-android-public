package org.afrikcode.pesmanager.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.afrikcode.pesmanager.impl.ManagerImpl;

/**
 * Created by precious on 2/12/18.
 */

public class IDService extends FirebaseInstanceIdService {

    private ManagerImpl managerImpl;
    private FirebaseUser muUser;

    @Override
    public void onCreate() {
        super.onCreate();
        muUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateTokenToFirebase(refreshedToken);
    }

    private void updateTokenToFirebase(String refreshedToken) {
        if (muUser != null) {
            if (managerImpl == null) {
                managerImpl = new ManagerImpl();
            }
            managerImpl.setMyToken(refreshedToken);
        }
    }
}
