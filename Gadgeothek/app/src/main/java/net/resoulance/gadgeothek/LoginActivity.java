package net.resoulance.gadgeothek;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView toRegister = (TextView) findViewById(R.id.toregisterTextView);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText matrikelNumberLogin = (EditText) findViewById(R.id.matrikelNummerText);
        final EditText passwordLogin = (EditText) findViewById(R.id.passwortText);

        final LibraryService libraryService = new LibraryService();

        // Hardcoded
        // mit lokalen Server testen --> "http://10.0.2.2:8080/public"
/*        name: 'Michael',
                password: "12345",
                email: "m@hsr.ch",
                studentnumber: "10"*/
        libraryService.setServerAddress("http://mge7.dev.ifs.hsr.ch/public");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String matrikelNumber = matrikelNumberLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                libraryService.login(matrikelNumber, password, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        Context context = getApplicationContext();
                        CharSequence text = "Okay";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // Start new Intent for Gadget Overview
                        // ToDo
                    }

                    @Override
                    public void onError(String message) {
                        Context context = getApplicationContext();
                        CharSequence text = "Error";
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
