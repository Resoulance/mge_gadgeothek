package net.resoulance.gadgeothek;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

        // ToDo: Hardcoded

        LibraryService.setServerAddress("http://mge7.dev.ifs.hsr.ch/public");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eMail = eMailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                LibraryService.login(eMail, password, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {

                        Intent intent = new Intent(LoginActivity.this, ReservationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        CharSequence text = "Login fehlgeschlagen";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
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
}
