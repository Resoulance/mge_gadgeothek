package net.resoulance.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText nameEditText = (EditText) findViewById(R.id.nameText);
        final EditText emailEditText = (EditText) findViewById(R.id.eMailText);
        final EditText matrikelNumberEditText = (EditText) findViewById(R.id.matrikelnummerText);
        final EditText passwordOneEditText = (EditText) findViewById(R.id.passwortTextEins);
        final EditText passwordTwoEditText = (EditText) findViewById(R.id.passwortTextZwei);
        Button registerUser = (Button) findViewById(R.id.registrierenButton);

        final LibraryService libraryService = new LibraryService();

        TextView toLogin = (TextView) findViewById(R.id.toLoginTextView);
        toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String matrikelNumber = matrikelNumberEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String passwordOne = passwordOneEditText.getText().toString();
                String passwordTwo = passwordTwoEditText.getText().toString();

                // --------- Hardcoded String for Library-Service for testing purpose!!!!!!!!!!!!!!!!

                libraryService.setServerAddress("http://mge7.dev.ifs.hsr.ch/public");



                if (name != "" || matrikelNumber != "" || email != "" || passwordOne != "") {
                    // simpler Passwortchecker: keine Längenüberprüfung, Sonderzeichen etc.
                    if (passwordOne != passwordTwo) {

                        passwordTwoEditText.setHint("Passwort stimmt nicht überein");
                        passwordTwoEditText.setHintTextColor(Color.RED);
                    } else {
                        libraryService.register(email, passwordOne, name, matrikelNumber, new Callback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                // ToDo
                                Context context = getApplicationContext();
                                CharSequence text = "Okay";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }

                            @Override
                            public void onError(String message) {
                                // ToDo
                                Context context = getApplicationContext();
                                CharSequence text = "Error";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        });
                    }

                }
            }
        });
    }


}
