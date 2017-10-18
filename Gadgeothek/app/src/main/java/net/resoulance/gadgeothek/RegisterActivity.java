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

public class RegisterActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText nameEditText = (EditText) findViewById(R.id.nameText);
        final EditText emailEditText = (EditText) findViewById(R.id.eMailText);
        final EditText matrikelNumberEditText = (EditText) findViewById(R.id.matrikelnummerText);
        final EditText passwordOneEditText = (EditText) findViewById(R.id.passwortTextEins);
        final EditText passwordTwoEditText = (EditText) findViewById(R.id.passwortTextZwei);

        final TextInputLayout nameWrapper = (TextInputLayout) findViewById(R.id.textinputName);
        final TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.textinputEmail);
        final TextInputLayout matrikelnumberWrapper = (TextInputLayout) findViewById(R.id.textinputMatrikelNumber);
        final TextInputLayout passwordOneWrapper = (TextInputLayout) findViewById(R.id.textinputPassword);
        final TextInputLayout passwordTwoWrapper = (TextInputLayout) findViewById(R.id.textinputPasswordTwo);
        Button registerUser = (Button) findViewById(R.id.registrierenButton);
        TextView toLogin = (TextView) findViewById(R.id.toLoginTextView);
        String eMail, password, serveraddress;

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

                if (name != "" || matrikelNumber != "" || email != "" || passwordOne != "") {
                    // ToDo: Regex password
                    //ToDo: Regex email & matrikel number
                    if (!passwordOne.equals(passwordTwo)) {

                        passwordTwoWrapper.setError("Passwort stimmt nicht überein");

                    } else {
                        LibraryService.register(email, passwordOne, name, matrikelNumber, new Callback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                prefEditor.putString("loginpref_email", emailEditText.getText().toString());
                                prefEditor.putString("loginpref_password", passwordOneEditText.getText().toString());
                                prefEditor.putBoolean("loginpref_logout", false);
                                prefEditor.commit();

                                Intent intent = new Intent(RegisterActivity.this, ReservationActivity.class);
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

                }
            }
        });
    }


}
