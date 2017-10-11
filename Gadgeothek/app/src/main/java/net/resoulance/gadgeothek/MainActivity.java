package net.resoulance.gadgeothek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button toRegisterAlternative = (Button) findViewById(R.id.toRegster);
        Button toLoginAlternative = (Button) findViewById(R.id.toLogin);
        Button toSettings = (Button) findViewById(R.id.toSettingsButton);

        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        toRegisterAlternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterAlternativeActivity.class);
                startActivity(intent);

            }
        });

        toLoginAlternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setContentView(R.layout.activity_login);


                Intent intent = new Intent(MainActivity.this, RegisterAlternativeActivity.class);
                startActivity(intent);


            }
        });


    }
}
