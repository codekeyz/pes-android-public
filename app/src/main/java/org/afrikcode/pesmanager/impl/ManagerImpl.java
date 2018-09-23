package org.afrikcode.pesmanager.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.afrikcode.pesmanager.base.BaseImp;
import org.afrikcode.pesmanager.contracts.ManagerContract;
import org.afrikcode.pesmanager.models.Manager;
import org.afrikcode.pesmanager.views.ManagerView;

import java.util.HashMap;
import java.util.Map;

public class ManagerImpl extends BaseImp<ManagerView> implements ManagerContract {

    private DocumentReference managerRef;

    public ManagerImpl() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseImp databaseImp = new DatabaseImp();
        this.managerRef = databaseImp.getManagersReference().document(mUser.getUid());
    }

    @Override
    public void getDetails() {
        getView().showLoadingIndicator();
        managerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                getView().hideLoadingIndicator();
                if (documentSnapshot.exists()){
                    Manager mD = new Manager().maptoData(documentSnapshot.getData());
                    if (mD != null) {
                        mD.setUserID(documentSnapshot.getId());
                        getView().ongetManagerDetails(mD);
                    } else {
                        getView().onError("Couldn't get your account info");
                    }
                }else{
                    getView().onError("Couldn't get your account info");
                }

            }
        });
    }

    @Override
    public void registerDetails(Manager manager) {
        getView().showLoadingIndicator();
        managerRef.set(manager.datatoMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getView().hideLoadingIndicator();
                if (task.isSuccessful()) {
                    getView().onUpdateManager();
                } else {
                    getView().onError("There was an error writing your details");
                }
            }
        });
    }

    @Override
    public void setMyToken(final String token) {
        Map<String, Object> data = new HashMap<>();
        data.put("myToken", token);
        managerRef.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Token", token);
                }
            }
        });
    }


}
