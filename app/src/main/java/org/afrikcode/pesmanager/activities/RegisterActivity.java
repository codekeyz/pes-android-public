package org.afrikcode.pesmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.impl.AuthImp;
import org.afrikcode.pesmanager.impl.ManagerImpl;
import org.afrikcode.pesmanager.models.Manager;
import org.afrikcode.pesmanager.views.AuthView;
import org.afrikcode.pesmanager.views.ManagerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements ManagerView, AuthView {


    @BindView(R.id.etManagerName)
    EditText managerName;

    @BindView(R.id.etEmail)
    EditText email;

    @BindView(R.id.et_password)
    EditText password;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.ln_loading_view)
    ProgressBar ln_loading;
    private AuthImp authImp;
    private ManagerImpl managerImpl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        authImp = new AuthImp();
        authImp.setView(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = managerName.getText().toString().trim();
                String eml = email.getText().toString().trim();
                String pass = password.getText().toString();

                Manager manager = new Manager(name);
                manager.setEmail(eml);
                manager.setPassword(pass);

                authImp.signup(manager);
            }
        });
    }

    @Override
    public void ongetManagerDetails(Manager manager) {

    }

    @Override
    public void onError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateManager() {
        Toast.makeText(this, "Account created success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAuthSuccess() {
    }

    @Override
    public void onAuthError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccountCreateSuccess(Manager manager) {
        managerImpl = new ManagerImpl();
        managerImpl.setView(this);

        managerImpl.registerDetails(manager);
    }

    @Override
    public void showLoadingIndicator() {
        btn_login.setVisibility(View.INVISIBLE);
        ln_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        btn_login.setVisibility(View.VISIBLE);
        ln_loading.setVisibility(View.GONE);
    }

}
