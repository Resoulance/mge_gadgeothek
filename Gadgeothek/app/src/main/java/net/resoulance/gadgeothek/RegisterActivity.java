package net.resoulance.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // ToDo Title etc in XML auslagern
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Gadgeothek");


        final EditText nameEditText = (EditText) findViewById(R.id.nameText);
        final EditText emailEditText = (EditText) findViewById(R.id.eMailText);
        final EditText matrikelNumberEditText = (EditText) findViewById(R.id.matrikelnummerText);
        final EditText passwordOneEditText = (EditText) findViewById(R.id.passwortTextEins);
        final EditText passwordTwoEditText = (EditText) findViewById(R.id.passwortTextZwei);

        final TextInputLayout nameWrapper = (TextInputLayout) findViewById(R.id.textinputName);
        final TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.textinputEmail);
        final TextInputLayout matrikelnumberWrapper = (TextInputLayout) findViewById(R.id.textinputMatrikelNumber);
        final TextInputLayout passwordOneWrapper = (TextInputLayout) findViewById(R.id.textinputPasswordOne);
        final TextInputLayout passwordTwoWrapper = (TextInputLayout) findViewById(R.id.textinputPasswordTwo);
        Button registerUser = (Button) findViewById(R.id.registrierenButton);
        TextView toLogin = (TextView) findViewById(R.id.toLoginTextView);
        String serveraddress;
        // General Email Regex (RFC 5322 Official Standard)
        final Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        serveraddress = sharedPreferences.getString("loginpref_serveraddress", "");
        LibraryService.setServerAddress(serveraddress);


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                boolean check1 = p.matcher(email).matches();
                boolean check2 = passwordOne.length() > 5;
                boolean check3 = passwordOne.equals(passwordTwo);

                if (name == "" || matrikelNumber == "" || email == "" || passwordOne == "" || !check1 || !check2 || !check3) {
                    //ToDo: Regex Matrikel number

                    if (!check1) {
                        emailWrapper.setError("ungültige E-Mail Adresse");
                    } else {
                        emailWrapper.setError(null);
                    }

                    if (!check2) {
                        passwordOneWrapper.setError("zu kurzes Passwort");
                    } else {
                        passwordOneWrapper.setError(null);
                    }
                    if (!check3) {
                        passwordTwoWrapper.setError("Passwort stimmt nicht überein");
                    } else {
                        passwordTwoWrapper.setError(null);
                    }


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT);
                    toast.show();
                    LibraryService.register(email, passwordOne, name, matrikelNumber, new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean input) {
                            String email = emailEditText.getText().toString();
                            String password = passwordOneEditText.getText().toString();
                            prefEditor.putString("loginpref_email", email);
                            prefEditor.putString("loginpref_password", password);
                            prefEditor.commit();

                            LibraryService.login(email, password, new Callback<Boolean>() {
                                @Override
                                public void onCompletion(Boolean input) {
                                    prefEditor.putBoolean("loginpref_logout", false);
                                    prefEditor.commit();
                                    Intent intent = new Intent(RegisterActivity.this, ReservationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(String message) {
                                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });


                        }

                        @Override
                        public void onError(String message) {
                            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
    }


}
