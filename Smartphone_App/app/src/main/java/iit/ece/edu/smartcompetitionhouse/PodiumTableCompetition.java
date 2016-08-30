package iit.ece.edu.smartcompetitionhouse;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PodiumTableCompetition extends AppCompatActivity {


    //Distance values
    Map<String, String> lastMonthValues;
    private final String[] roommates_keys = {"Adnan", "Cat", "Chris", "Sergio"};

    //Table values
    private TextView[] roommates;
    private TextView[] distances;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podium_table_competition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Initialize GUI
        roommates = new TextView[4];
        roommates[0]= (TextView) findViewById(R.id.firstResident);
        roommates[1] = (TextView) findViewById(R.id.secondResident);
        roommates[2] = (TextView) findViewById(R.id.thirdResident);
        roommates[3] = (TextView) findViewById(R.id.fourthdResident);

        distances = new TextView[4];
        distances[0]= (TextView) findViewById(R.id.firstDistance);
        distances[1] = (TextView) findViewById(R.id.secondDistance);
        distances[2] = (TextView) findViewById(R.id.thirdDistance);
        distances[3] = (TextView) findViewById(R.id.fourthDistance);

        //Initialize server
        lastMonthValues = new HashMap<String, String>();
        requestLastCompetitionValues();
    }

    private void requestLastCompetitionValues(){
        //Prepare the message request for the server:
        List<Map<String, String>> jsonRequestValues =  new ArrayList<Map<String, String>>();
        HashMap<String, String> tables_list = new HashMap<String, String>();
        tables_list.put("roommate1", "Adnan");
        tables_list.put("roommate2", "Cat");
        tables_list.put("roommate3", "Chris");
        tables_list.put("roommate4", "Sergio");
        tables_list.put("type", "read_row");
        jsonRequestValues.add(tables_list);
        String jsonString = CompetitionTrackActivity.mServer.convertToJSON(jsonRequestValues);
        CompetitionTrackActivity.mServer.sendToCentralNode(jsonString, CompetitionTrackActivity.mServer.WRITE_URL, aHTTPCompClient);
    }

    private void  updateTableValues(){
        //Sort Map values
        double first = 0;
        double second = 0;
        double third = 0;
        double fourth =0;

        String first_room = "";
        String second_room = "";
        String third_room = "";
        String fourth_room = "";
        Iterator<String> keys = lastMonthValues.keySet().iterator();
        String [] sortedRooms= new String[4];
        try {
            while (keys.hasNext()) {
                String key = (String) keys.next();

                double val = Double.parseDouble(lastMonthValues.get(key));
               // System.out.println("Douuble distance: "+val);
                if (val >= first) {
                    //First will be this one
                    //But! move all the others one step down
                    for (int i = 3; i >= 1; i--)
                        sortedRooms[i] = sortedRooms[i - 1];
                    sortedRooms[0] = key;
                    first = val;
                    //System.out.println("First: " + key);

                } else if (val >= second) {
                    //Second will be this one
                    for (int i = 3; i >= 2; i--)
                        sortedRooms[i] = sortedRooms[i - 1];
                    sortedRooms[1] = key;
                    second = val;
                    //System.out.println("Second: "+key);


                } else if (val >= third) {
                    sortedRooms[3] = sortedRooms[2];
                    sortedRooms[2] = key;
                    third = val;
                    System.out.println("Third: "+key);


                } else if (val >= fourth) {
                    sortedRooms[3] = key;
                    fourth = val;
                    //System.out.println("Fourth: "+key);
                }

            }
            //Show in the GUI
            for (int i =0; i < roommates.length; i ++) {
                //Get values and put in table
                roommates[i].setText(sortedRooms[i]);
                distances[i].setText(lastMonthValues.get(sortedRooms[i]));

            }
        }catch (Exception e){
            System.out.println("Getting keys exc "+e);
        }




    }

    /****************************************************
     * HTTP ASYNC CLIENT
     */
    public final AsyncHttpResponseHandler aHTTPCompClient = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            String cont = ServerConnector.convertToString(bytes);
            onSuccess(cont);

        }

        @Override
        public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
            String cont = ServerConnector.convertToString(bytes);


            System.out.println("Get comp Failed! server:" + statusCode);

            if (statusCode == 404) {
                System.out.println("Page not found");

            } else if (statusCode == 500) {
                System.out.println("Server failure");

            } else {
            }

        }

        //Handle succesful response
        public void onSuccess(String response) {
            System.out.println("Get comp Server response: "+response);

            try {
                //Convert to a JSON Array and get the arguments
                JSONArray arr = new JSONArray(response);
                //List<String> args = new ArrayList();
                //Analyze each JSON object
                JSONObject jsonObj = (JSONObject)arr.get(0);
                Iterator<?> keys = jsonObj.keys();
                while( keys.hasNext() ) {
                    String key = (String) keys.next();
                    lastMonthValues.put(key, jsonObj.getString(key));
                }
                //Update gui values:
                updateTableValues();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Handle failing response
        public void onFailure(int statusCode, Throwable error, String content) {


        }


    };

}
