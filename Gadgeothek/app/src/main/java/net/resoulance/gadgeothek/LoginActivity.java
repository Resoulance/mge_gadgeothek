package net.resoulance.gadgeothek;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;


public class LoginActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);

        TextView toRegister = (TextView) findViewById(R.id.toregisterTextView);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText eMailLogin = (EditText) findViewById(R.id.eMailText);
        final EditText passwordLogin = (EditText) findViewById(R.id.passwortText);

        // Hardcoded
        // mit lokalen Server testen --> "http://10.0.2.2:8080/public"
/*        name: 'Michael',
                password: "12345",
                email: "m@hsr.ch",
                studentnumber: "10"*/

        LibraryService.setServerAddress("http://mge7.dev.ifs.hsr.ch/public");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eMail = eMailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                LibraryService.login(eMail, password, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        Context context = getApplicationContext();
                        CharSequence text = "Login erfolgreich";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // Start new Intent for Gadget Overview
                        // ToDo
                        Intent intent = new Intent(LoginActivity.this, ReservationActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        Context context = getApplicationContext();
                        CharSequence text = "Login fehlgeschlagen";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
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
