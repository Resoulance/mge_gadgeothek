package net.resoulance.gadgeothek;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.resoulance.gadgeothek.fragments.LoanListFragment;
import net.resoulance.gadgeothek.fragments.ReservationListFragment;
import net.resoulance.gadgeothek.service.ItemSelectionListener;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
}
