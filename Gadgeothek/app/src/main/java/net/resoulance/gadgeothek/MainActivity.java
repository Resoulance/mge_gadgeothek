package net.resoulance.gadgeothek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.resoulance.gadgeothek.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toLogin = (Button) findViewById(R.id.toLoginButton);
        Button toRegister = (Button) findViewById(R.id.toRegisterButton);
        Button toRegisterAlternative = (Button) findViewById(R.id.toRegsterAlternative);

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
    }
}
