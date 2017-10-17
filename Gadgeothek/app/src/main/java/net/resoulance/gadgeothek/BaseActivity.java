package net.resoulance.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;

abstract class BaseActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.option_settings:
                Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
                startActivity(intent);

                return true;
            case R.id.option_logout:
                if (LibraryService.hasToken()) {
                    LibraryService.logout(new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean input) {



                            Toast toast = Toast.makeText(getApplicationContext(), "Erfolgreich ausgeloggt", Toast.LENGTH_SHORT);
                            toast.show();

                            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onError(String message) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Logout Fehler", Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });

                    return true;
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: Kein Token", Toast.LENGTH_SHORT);
                    toast.show();
                }
            case R.id.option_Tester:

                //Werte aus der Settings lesen und dann über den Toaster zeigen


                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String testString = sharedPreferences.getString("loginpref_password", "bla");


                Toast toast = Toast.makeText(getApplicationContext(), testString, Toast.LENGTH_SHORT);
                toast.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(null);


    }
}
