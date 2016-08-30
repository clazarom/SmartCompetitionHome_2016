package iit.ece.edu.smartcompetitionhouse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "Pass_message";
    private String userName;
    private Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Logged in user: ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Smart house button
        //Buttons
        Button b1 = (Button) findViewById(R.id.smart_house_button);
        b1.setOnClickListener(smartHouseListener);

        //Competition button
        Button b2 = (Button) findViewById(R.id.competition_button);
        b2.setOnClickListener(competitionListener);

        ctx = this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Start fragment:

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /***************************************
     * ON CLICK LISTENERS
     */

    View.OnClickListener smartHouseListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //

            String msg = "Attempting to connect…";

        }
    };

    View.OnClickListener competitionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //
            String msg = "Attempting to connect…";
            Intent intent = new Intent(ctx, CompetitionTrackActivity.class);
            //EditText editText = (EditText) findViewById(R.id.edit_message);
            if (userName != null) {
                String message = userName.toString();
                intent.putExtra(EXTRA_MESSAGE, message);
            }
            startActivity(intent);


        }
    };

}
