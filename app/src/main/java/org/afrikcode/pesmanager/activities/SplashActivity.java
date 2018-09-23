package org.afrikcode.pesmanager.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.Utils;
import org.afrikcode.pesmanager.impl.AuthImp;
import org.afrikcode.pesmanager.impl.ManagerImpl;
import org.afrikcode.pesmanager.models.Manager;
import org.afrikcode.pesmanager.views.ManagerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements ManagerView {

    AuthImp mAuthImp;
    @BindView(R.id.actions)
    LinearLayout actions;

    @BindView(R.id.btn_register)
    Button register;

    @BindView(R.id.btn_login)
    Button login;

    @BindView(R.id.ln_loading_view)
    ProgressBar loading;

    private ManagerImpl managerImpl;
    private Manager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        mAuthImp = new AuthImp();

        int SPLASH_TIME_OUT = 5000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                loading.setVisibility(View.GONE);

                if (mAuthImp.isAuthenticated()) {
                    init();
                    managerImpl.getDetails();
                    sendToken();
                    canStart();
                } else {
                    actions.setVisibility(View.VISIBLE);
                    register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actions.setVisibility(View.GONE);
                            // start authActivityforResult
                            Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                            startActivityForResult(intent, 12);
                        }
                    });

                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actions.setVisibility(View.GONE);
                            // start authActivityforResult
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivityForResult(intent, 11);
                        }
                    });
                }

            }
        }, SPLASH_TIME_OUT);
    }

    private void sendToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        managerImpl.setMyToken(refreshedToken);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 || requestCode == 12) {
            if (resultCode == RESULT_OK) {
                init();
                managerImpl.getDetails();
                canStart();
            } else {
                actions.setVisibility(View.VISIBLE);
            }
        }
    }

    private void canStart() {
        final ProgressDialog dialog = ProgressDialog.show(SplashActivity.this, "Please Wait", "Loading data, please wait....");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                if (manager != null) {
                    if (manager.isAccountConfirmed()) {
                        if (manager.getBranchID() != null && manager.getBranchName() != null) {
                            Utils utils = new Utils();
                            utils.saveBranchID(SplashActivity.this, manager.getBranchID());
                            utils.saveBranchName(SplashActivity.this, manager.getBranchName());
                            utils.saveManagerID(SplashActivity.this, manager.getUserID());
                            startHome();
                        } else {
                            showNotConfirmed("No branch has been set for your account.\nContact Administrator for help");
                        }
                    } else {
                        showNotConfirmed("Your account has not been confirmed.\nContact Administrator for help");
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("Request Info");
                    builder.setMessage("We couldn't get your details. Try again?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            canStart();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    AlertDialog dialog1 = builder.create();
                    dialog1.show();
                }
            }
        }, 2000);
    }

    private void init(){
        managerImpl = new ManagerImpl();
        managerImpl.setView(this);
    }

    private void startHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showNotConfirmed(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("Account Info");
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        builder.setMessage(text);
        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        if (!dialog.isShowing()){
            dialog.show();
        }
    }

    @Override
    public void showLoadingIndicator() {
       loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void ongetManagerDetails(Manager man) {
        this.manager = man;
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateManager() { }


}
