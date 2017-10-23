package net.resoulance.gadgeothek;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.LibraryService;

import java.util.ArrayList;

abstract class BaseActivity extends AppCompatActivity {

    boolean devOn = false;
    private String selectedServer;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);

        if (!LibraryService.isLoggedIn()) {
            menu.findItem(R.id.option_logout).setVisible(false);
        }

        if (!devOn) {
            menu.findItem(R.id.option_Tester).setVisible(false);
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        switch (item.getItemId()) {
            case R.id.option_settings:
                Intent intent = new Intent(BaseActivity.this, PrefActivity.class);
                startActivity(intent);

                return true;
            case R.id.option_logout:

                if (LibraryService.isLoggedIn()) {
                    LibraryService.logout(new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean input) {

                            sharedPreferences.edit().putBoolean("loginpref_logout", false);
                            sharedPreferences.edit().commit();
                            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }


                        @Override
                        public void onError(String message) {
                            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });

                    return true;
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: Kein Token", Toast.LENGTH_SHORT);
                    toast.show();
                    return false;
                }
            case R.id.option_Tester:
                String user = sharedPreferences.getString("loginpref_email", "Setting nicht gefunden");
                String password = sharedPreferences.getString("loginpref_password", "Setting nicht gefunden");
                String server = sharedPreferences.getString("loginpref_serveraddress", "Setting nicht gefunden");
                String output = "eMail: " + user + "\nPasswort: " + password + "\nServer: " + server;
                Toast toast = Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT);
                toast.show();

                return true;
            case R.id.option_switchserver:
                showServerDialogue();
                return true;
            case R.id.option_autologin:
                item.setChecked(!item.isChecked());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showServerDialogue() {

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String activeServer = sharedPreferences.getString("loginpref_serveraddress", "");
        int activeServerPos = 0;

        ArrayList<String> serverNames = new ArrayList<>();
        final ArrayList<String> serverAdresses = new ArrayList<>();
        serverNames.add("MGE Server 1");
        serverAdresses.add("http://mge1.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 2");
        serverAdresses.add("http://mge2.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 3");
        serverAdresses.add("http://mge3.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 4");
        serverAdresses.add("http://mge4.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 5");
        serverAdresses.add("http://mge5.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 6");
        serverAdresses.add("http://mge6.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 7");
        serverAdresses.add("http://mge7.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 8");
        serverAdresses.add("http://mge8.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 9");
        serverAdresses.add("http://mge9.dev.ifs.hsr.ch/public");
        serverNames.add("MGE Server 10");
        serverAdresses.add("http://mge10.dev.ifs.hsr.ch/public");

        CharSequence[] servers = serverNames.toArray(new CharSequence[serverNames.size()]);

        boolean foundActiveServer = false;
        for (String server :
                serverAdresses) {
            if(!server.equals(activeServer)) {
                activeServerPos++;
                foundActiveServer = false;
            } else {
                foundActiveServer = true;
                break;
            }
        }
        if (!foundActiveServer) {
            activeServerPos = -1;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wähle den Server").setSingleChoiceItems(servers, activeServerPos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                selectedServer = serverAdresses.get(item);
            }
        });

        String positiveText = "Change";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                sharedPreferences.edit().putString("loginpref_serveraddress", selectedServer)
                        .putBoolean("loginpref_logout", false).commit();
                if(LibraryService.isLoggedIn()) {
                    LibraryService.logout(new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean input) {
                            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
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
        });
        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {}
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(null);


    }
}
