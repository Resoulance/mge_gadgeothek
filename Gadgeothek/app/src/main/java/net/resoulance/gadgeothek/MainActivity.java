package net.resoulance.gadgeothek;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toLogin = (Button) findViewById(R.id.toLoginButton);
        Button toRegister = (Button) findViewById(R.id.toRegisterButton);
        Button toRegisterAlternative = (Button) findViewById(R.id.toRegsterAlternative);
        Button registerUser = (Button) findViewById(R.id.registrierenButton);

        final EditText nameEditText = (EditText) findViewById(R.id.nameText);
        final EditText emailEditText = (EditText) findViewById(R.id.eMailText);
        final EditText matrikelNumberEditText = (EditText) findViewById(R.id.matrikelNummerText);
        final EditText passwordOneEditText = (EditText) findViewById(R.id.passwortTextEins);
        final EditText passwordTwoEditText = (EditText) findViewById(R.id.passwortTextZwei);

        final LibraryService libraryService = new LibraryService();

        toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setContentView(R.layout.activity_login);
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setContentView(R.layout.activity_register);
            }
        });

        toRegisterAlternative.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setContentView(R.layout.activity_register_alternative);
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String name = nameEditText.toString();
                String matrikelNumber = matrikelNumberEditText.toString();
                String email = emailEditText.toString();
                String passwordOne = passwordOneEditText.toString();
                String passwordTwo = passwordTwoEditText.toString();
                if (name != "" || matrikelNumber != "" || email != "" || passwordOne != ""){
                    // simpler Passwortchecker: keine Längenüberprüfung, Sonderzeichen etc.
                    if (passwordOne != passwordTwo){

                        passwordTwoEditText.setHint("Passwort stimmt nicht überein");
                        passwordTwoEditText.setHintTextColor(Color.RED);
                    } else {
                        libraryService.register(email, passwordOne, name, matrikelNumber, new Callback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                // ToDo
                            }

                            @Override
                            public void onError(String message) {
                                // ToDo
                            }
                        });
                    }

                }
            }
        });
    }
}
