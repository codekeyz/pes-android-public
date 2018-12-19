package org.afrikcode.pesmanager.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.afrikcode.pesmanager.R;
import org.afrikcode.pesmanager.Utils;
import org.afrikcode.pesmanager.base.BaseFragment;
import org.afrikcode.pesmanager.fragments.ClientsFragment;
import org.afrikcode.pesmanager.fragments.ServicesFragment;
import org.afrikcode.pesmanager.fragments.timeline.YearFragment;
import org.afrikcode.pesmanager.impl.AuthImp;
import org.afrikcode.pesmanager.impl.ManagerImpl;
import org.afrikcode.pesmanager.listeners.FragmentListener;
import org.afrikcode.pesmanager.models.Manager;
import org.afrikcode.pesmanager.viewholder.HeaderView;
import org.afrikcode.pesmanager.views.ManagerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentListener, FragmentManager.OnBackStackChangedListener, ManagerView, FirebaseAuth.AuthStateListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.container)
    FrameLayout container;
    private FragmentManager fragmentManager;
    private SearchView searchView;
    private AuthImp authImp;
    private ManagerImpl managerImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        FirebaseInstanceId mInstanceId = FirebaseInstanceId.getInstance();

        managerImpl = new ManagerImpl();
        authImp = new AuthImp();
        authImp.getAuth().addAuthStateListener(this);
        managerImpl.setView(this);

        setuptoolbarAndDrawer();
        openDefaultFragment();

        managerImpl.getDetails();

        String instanceID = mInstanceId.getToken();
        if (instanceID != null) {
            managerImpl.setMyToken(instanceID);
        }
    }


    private void openDefaultFragment() {
        navigationView.setCheckedItem(R.id.nav_home);
        ServicesFragment yf = new ServicesFragment();
        moveToFragment(yf);
    }

    private void setuptoolbarAndDrawer() {
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.center, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                openDefaultFragment();
                break;
            case R.id.nav_client:
                ClientsFragment cf = new ClientsFragment();
                cf.setTitle("Clients in Branch");
                moveToFragment(cf);
                break;
            case R.id.nav_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                showLogoutDialog();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                authImp.logout();
                Intent iii = new Intent(HomeActivity.this, SplashActivity.class);
                iii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(iii);
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void moveToFragment(BaseFragment fragment) {
        if (fragment != null) {
            if (getCurrentFragment() != null) {

                if (!getCurrentFragment().getTitle().equalsIgnoreCase(fragment.getTitle())) {
                    fragment.setFragmentListener(this);
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(container.getId(), fragment);
                    ft.commit();
                    ft.addToBackStack(getCurrentFragment().getTitle());
                    setTitle(fragment.getTitle());
                }

            } else {

                fragment.setFragmentListener(this);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(container.getId(), fragment);
                ft.commit();
                setTitle(fragment.getTitle());

            }
        }
    }

    public SearchView getSearchView() {
        return searchView;
    }

    @Override
    public void onBackStackChanged() {
        String title = getCurrentFragment().getTitle();
        setTitle(title);

        switch (title) {
            case "Select Year":
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case "Select a Client":
                navigationView.setCheckedItem(R.id.nav_client);
                break;
        }
    }

    private BaseFragment getCurrentFragment() {
        return (BaseFragment) fragmentManager.findFragmentById(container.getId());
    }

    @Override
    public void ongetManagerDetails(Manager manager) {
        HeaderView headerView = new HeaderView(navigationView.getHeaderView(0));
        headerView.email.setText(manager.getEmail());
        headerView.username.setText(manager.getUsername());
        new Utils().saveBranchID(HomeActivity.this, manager.getBranchID());
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onUpdateManager() {

    }

    @Override
    public void showLoadingIndicator() {

    }

    @Override
    public void hideLoadingIndicator() {

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(this, "You have been force logged out", Toast.LENGTH_SHORT).show();
            Intent iii = new Intent(HomeActivity.this, SplashActivity.class);
            iii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iii);
            finish();
        }
    }
}
