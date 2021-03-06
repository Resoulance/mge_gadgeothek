package net.resoulance.gadgeothek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView splashtext = (TextView) findViewById(R.id.splash_textview);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        boolean alwaysLogin;
        String eMail, password, serveraddress;

        alwaysLogin = sharedPreferences.getBoolean("loginpref_switch", false);

        if (sharedPreferences.getBoolean("loginpref_firstlogin", true)) {
            prefEditor.putBoolean("loginpref_firstlogin", false);
            prefEditor.putBoolean("loginpref_logout", true);
            prefEditor.putString("loginpref_serveraddress", "http://mge1.dev.ifs.hsr.ch/public");
            prefEditor.putString("loginpref_email", "");
            prefEditor.putString("loginpref_password", "");
            prefEditor.putBoolean("loginpref_switch", true);
            prefEditor.commit();
        }


        if (alwaysLogin && !sharedPreferences.getBoolean("loginpref_logout", true)) {
            splashtext.setText("Waiting for Server Response...");
            serveraddress = sharedPreferences.getString("loginpref_serveraddress", "");
            LibraryService.setServerAddress(serveraddress);
            eMail = sharedPreferences.getString("loginpref_email", "");
            password = sharedPreferences.getString("loginpref_password", "");
            LibraryService.login(eMail, password, new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Automatisches Login fehlgeschlagen", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });


        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


    }

}
