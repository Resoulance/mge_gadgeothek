package net.resoulance.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toLogin = (Button) findViewById(R.id.toLoginButton);
        Button toRegister = (Button) findViewById(R.id.toRegisterButton);
        Button toRegisterAlternative = (Button) findViewById(R.id.toRegsterAlternative);
        Button toLoginAlternative = (Button) findViewById(R.id.toLoginAlternative);
        Button toastTester = (Button) findViewById(R.id.toasttesterButton);


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_login);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                /*setContentView(R.layout.activity_login);*/
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_register);
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                /*setContentView(R.layout.activity_register);*/
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

        toLoginAlternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                setContentView(R.layout.activity_login_alternative);

                Intent intent = new Intent(MainActivity.this, RegisterAlternativeActivity.class);
                startActivity(intent);
                /*setContentView(R.layout.activity_login_alternative);*/
           
            }
        });

        toastTester.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });


    }
}
