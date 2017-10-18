package net.resoulance.gadgeothek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean alwaysLogin;
        String eMail, password, serveraddress;

        alwaysLogin = sharedPreferences.getBoolean("loginpref_switch", false);

        if (alwaysLogin && !sharedPreferences.getBoolean("loginpref_logout", true)) {
            serveraddress = sharedPreferences.getString("loginpref_serveraddress", "");
            LibraryService.setServerAddress(serveraddress);
            eMail = sharedPreferences.getString("loginpref_email", "");
            password = sharedPreferences.getString("loginpref_password", "");
            LibraryService.login(eMail, password, new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    Toast toast = Toast.makeText(getApplicationContext(), "automatisch eingeloggt", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }

                @Override
                public void onError(String message) {
                    Toast toast = Toast.makeText(getApplicationContext(), "automatisch eingeloggen fehlgeschlagen", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            });


        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);
        }


    }

}
