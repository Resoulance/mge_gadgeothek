package net.resoulance.gadgeothek;

import android.content.Intent;
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


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_login);
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_register);
            }
        });

        toRegisterAlternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterAlternativeActivity.class);
                startActivity(intent);
                /*setContentView(R.layout.activity_register_alternative);*/
            }
        });


    }
}
