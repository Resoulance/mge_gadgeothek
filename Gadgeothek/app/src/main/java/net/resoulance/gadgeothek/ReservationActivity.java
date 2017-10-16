package net.resoulance.gadgeothek;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.resoulance.gadgeothek.fragments.LoanListFragment;
import net.resoulance.gadgeothek.fragments.ReservationListFragment;
import net.resoulance.gadgeothek.service.ItemSelectionListener;

public class ReservationActivity extends AppCompatActivity implements ItemSelectionListener {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_loans:
                    LoanListFragment loanListFragment = new LoanListFragment();
                    fragmentTransaction.replace(R.id.fragment_container, loanListFragment);
                    fragmentTransaction.commit();

                    return true;
                case R.id.navigation_reservations:
                    ReservationListFragment reservationListFragment = new ReservationListFragment();
                    fragmentTransaction.replace(R.id.fragment_container, reservationListFragment);
                    fragmentTransaction.commit();
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onItemSelected(int position) {

    }
}
