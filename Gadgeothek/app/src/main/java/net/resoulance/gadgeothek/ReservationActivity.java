package net.resoulance.gadgeothek;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import net.resoulance.gadgeothek.fragments.LoanListFragment;
import net.resoulance.gadgeothek.fragments.ReservationListFragment;
import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.ItemSelectionListener;
import net.resoulance.gadgeothek.service.LibraryService;

public class ReservationActivity extends BaseActivity implements ItemSelectionListener {




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_loans:
                    setTitle("Loans");
                    LoanListFragment loanListFragment = new LoanListFragment();
                    loadFragment(loanListFragment);

                    return true;
                case R.id.navigation_reservations:
                    setTitle("Reservations");
                    ReservationListFragment reservationListFragment = new ReservationListFragment();
                    loadFragment(reservationListFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        LibraryService.setServerAddress(sharedPreferences.getString("loginpref_serveraddress", ""));
        if(!LibraryService.isLoggedIn()){
            LibraryService.login(sharedPreferences.getString("loginpref_email", ""), sharedPreferences.getString("loginpref_password", ""), new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    prefEditor.putBoolean("preflogin_logout", true);
                    prefEditor.commit();
                }

                @Override
                public void onError(String message) {
                    prefEditor.putBoolean("preflogin_logout", false);
                    prefEditor.commit();
                    Intent intent = new Intent(ReservationActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            });
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        // ToDo Title etc in XML auslagern
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Gadgeothek");
        setSupportActionBar(toolbar);

        setTitle("Loans");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        LoanListFragment loanListFragment = new LoanListFragment();
        loadFragment(loanListFragment);
    }

    @Override
    public void onItemSelected(int position) {

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
