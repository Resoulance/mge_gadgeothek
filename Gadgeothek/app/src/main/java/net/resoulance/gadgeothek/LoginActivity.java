package net.resoulance.gadgeothek;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toRegister = (TextView) findViewById(R.id.toregisterTextView);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText eMailLogin = (EditText) findViewById(R.id.eMailText);
        final EditText passwordLogin = (EditText) findViewById(R.id.passwortText);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        String eMail, password, serveraddress;


        refreshServerAddress(sharedPreferences);

        // setzt letzte e-Mail Adresse in das Textfeld ein, wenn es nicht existiert dann einen leeren String
        eMailLogin.setText(sharedPreferences.getString("loginpref_email", ""));


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eMail = eMailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                prefEditor.putString("loginpref_email", eMail);
                prefEditor.putString("loginpref_password", password);
                prefEditor.putBoolean("loginpref_logout", false);
                prefEditor.commit();
                refreshServerAddress(sharedPreferences);


                LibraryService.login(eMail, password, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        passwordLogin.setText("");
                        Intent intent = new Intent(LoginActivity.this, ReservationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });


            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    private void refreshServerAddress(SharedPreferences sharedPreferences) {
        String serveraddress;
        serveraddress = sharedPreferences.getString("loginpref_serveraddress", "");
        LibraryService.setServerAddress(serveraddress);
    }
}
