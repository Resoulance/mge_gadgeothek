package net.resoulance.gadgeothek;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by sasch on 16.10.2017.
 */

abstract class BaseActivity extends AppCompatActivity {
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
                // TODO: Code hier
                Context context1 = getApplicationContext();
                CharSequence text1 = "Settings clicked!";
                int duration1 = Toast.LENGTH_SHORT;

                Toast toast1 = Toast.makeText(context1, text1, duration1);
                toast1.show();
                return true;
            case R.id.option_logout:
                // TODO Code hier
                Context context2 = getApplicationContext();
                CharSequence text2 = "Logout clicked!";
                int duration2 = Toast.LENGTH_SHORT;

                Toast toast2 = Toast.makeText(context2, text2, duration2);
                toast2.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(null);

    }
}
