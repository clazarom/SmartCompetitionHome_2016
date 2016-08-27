package iit.ece.edu.smartcompetitionhouse;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by Cat on 8/25/2016.
 */
public class CompetitionTrackActivity extends AppCompatActivity {
    //Permissions
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE


    };
    private static final int INITIAL_REQUEST=1337;
    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;



    //Distance
    static TextView currentDistance;
    static TextView todayDistance;
    static TextView monthDistance;
    LocationManager mLocationManager;
    Location previousLocation;
    private final static int coef = 10*1000;

    //Minimum time for location update
    final static int LOCATION_REFRESH_TIME = 10*1000; //10 seconds
    ///Minimum distance for location update
    final static int LOCATION_REFRESH_DISTANCE = 50 ; //50 meters
    final static int MIN_DISTANCE_THRESHOLD = 100;

    private static float sessionDistanceValue;
    private static float dayDistanceValue;
    private static float monthDistanceValue;

    //Step counter
    private  CountSteps stepCounter;
    private static float initial_steps;
    private static boolean startedCounter = false;
    private static final double STEP_LENGTH = 0.8; //meters
    Context ctx;


    //Columns for database
    private static  DatabaseManager myDB;

    public static final String[] columnsTable = new String[]{"time_stamp", "current_distance", "today_distance", "monthly_distance"};
    public static final int _TIME_STAMP = 0;
    public static final int _TODAY = 2;
    public static final int _MONTH = 3;
    public static final String user_name = "user";

    //Session info
    private String currentMonth;
    private String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("On create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competition_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ctx = this;

        //Get textview
        currentDistance = (TextView) findViewById(R.id.currentDistance);
        todayDistance = (TextView) findViewById(R.id.todayDistance);
        monthDistance = (TextView) findViewById(R.id.monthDistance);


        // ****** REQUESTS *****
        //Location
        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        //Check permissions
        if (!canAccessLocation() || !canAccessMemory()) {
            //Do nothing
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            Toast.makeText(ctx, "Request Permissions", Toast.LENGTH_LONG).show();


        }else {
            //Start tracking service
            if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                System.out.println("Start tracking ");
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, mLocationListener);
            }else{
                //requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                System.out.println("Manager ");
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, mLocationListener);

            }
        //Request write read:
       /* String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, WRITE_REQUEST_CODE);
        String[] permissions2 = {Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions2, READ_REQUEST_CODE);*/



        //Recover data from database

        //TODO Get user name

        //Create database object
        myDB = new DatabaseManager(this);



        }

        //DEBUG: Try updating
        //updateSessionDistance((float)0.11);

        //Step counter
        if (!startedCounter) {
            initial_steps = 0;
            startedCounter = true;
            Toast.makeText(ctx, "New step counter: "+stepCounter, Toast.LENGTH_LONG).show();
            stepCounter = new CountSteps(this);

            //Database restart
            restartFromTable(this);
            //Distance
            sessionDistanceValue = 0;

            //Update gui
            updateTextViews();



        }
        else{

            System.out.println("Initial steps: "+initial_steps);
            stepCounter = new CountSteps(this, initial_steps);
            updateTextViews();


        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("On start");
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("On resume");
        initial_steps = stepCounter.getInitialSteps();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //DO NOTHING
        System.out.println("On pause");
        initial_steps =  stepCounter.getInitialSteps();

    }

    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("On stop");
        initial_steps = stepCounter.getInitialSteps();


    }

    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("On Restart");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //Stop counting steps
        stepCounter.stopCounting();


    }

    /**
     * Include new distance to the app
     * @param dist - in km
     */
    public static void updateSessionDistance(float dist){
        //Distance in meters
        sessionDistanceValue = dist;
        System.out.println("Session distance: "+sessionDistanceValue);
        //Update in GUI
        updateTextViews();


        //Store in database
        ArrayList<String> values = new ArrayList<>();
        //Timestamp
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = format.format(new Date());
        values.add("'"+currentDateandTime+"'");
        //Distances:
        values.add(sessionDistanceValue+"");
        values.add((sessionDistanceValue+dayDistanceValue)+"");
        values.add((sessionDistanceValue+monthDistanceValue)+"");

        myDB.updateDatabaseTable(user_name, values, true);


    }

    /***
     * Permissions
     */
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessMemory() {
        return(hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                && hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }
    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    /***************************************
     * Location parameters
     */
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            System.out.println("New location");

            //your code here
            if (previousLocation ==null) {
                previousLocation = location;
                //Obtain distance from step counter, not GPS
                float steps = stepCounter.getSteps();
                //Toast.makeText(ctx, "Location steps: " + steps, Toast.LENGTH_LONG).show();
                CompetitionTrackActivity.updateSessionDistance(stepsToKm(steps));
            }else{
                //Results: initial bearing and final bearing
               /*float[] results = new float[2];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                        previousLocation.getLatitude(), previousLocation.getLongitude(), results);*/
                float distance = previousLocation.distanceTo(location);
                CompetitionTrackActivity.updateSessionDistance(distance);
                previousLocation = location;
                //Obtain distance from step counter, not GPS
                float steps = stepCounter.getSteps();

                if (distance > MIN_DISTANCE_THRESHOLD) {
                    Toast.makeText(ctx, "Location steps: " + steps, Toast.LENGTH_LONG).show();
                    if (steps >0)
                        CompetitionTrackActivity.updateSessionDistance(stepsToKm(steps));
                }else {
                    //Restart counter
                    // Steps but no movement??? Cheating....
                    initial_steps = initial_steps +steps;
                    stepCounter = new CountSteps(ctx, initial_steps);

                }


            }
        }

        private float stepsToKm(float steps){
            float distance = (float)(steps*STEP_LENGTH)/1000;
            return distance;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void restartFromTable(Context ctx){
        //Create a table for Empatica
        myDB.createTable(user_name, columnsTable[0], new ArrayList<>(Arrays.asList(columnsTable)));
        //Verify last row values
        Map<String, String> row =  myDB.readLastRowFromDatabase(ctx, user_name, columnsTable);

        //Get timestamp:
        String time = row.get(columnsTable[_TIME_STAMP]);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = format.format(new Date());


        if (time != null) {
            try {
                Date date = format.parse(time);
                Date currentDate = format.parse(currentDateAndTime);
                //Recover month -Compare with current motnh
                currentMonth = (String) android.text.format.DateFormat.format("MMM", currentDate);
                String last_month = (String) android.text.format.DateFormat.format("MMM", date);
                if (currentMonth.equals(last_month)) {
                    monthDistanceValue = (float)Double.parseDouble(row.get(columnsTable[_MONTH]));
                    System.out.println("Month total: "+ monthDistanceValue);
                    //Recover day - Compare with current day
                    String last_day = (String) android.text.format.DateFormat.format("dd", date);
                    currentDay = (String) android.text.format.DateFormat.format("dd", currentDate);
                    if (currentDay.equals(last_day)) {
                        dayDistanceValue = (float) Double.parseDouble(row.get(columnsTable[_TODAY]));
                        System.out.println("Day total: " + dayDistanceValue);
                    }

                    else
                        dayDistanceValue = 0;
                } else {
                    monthDistanceValue = 0;
                    dayDistanceValue = 0;

                }


            } catch (Exception e) {
                System.out.println("ErroR converting to date " + e);

            }
        }
        updateTextViews();


    }

    private static void updateTextViews(){
        //Truncaate to ##.##
        Double trun = new BigDecimal(sessionDistanceValue)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        currentDistance.setText(""+trun);

        //Truncaate to ##.##
        trun = new BigDecimal(dayDistanceValue + sessionDistanceValue)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        todayDistance.setText("" + trun);

        //Truncaate to ##.##
        trun = new BigDecimal(monthDistanceValue + sessionDistanceValue)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        monthDistance.setText(""+trun);

    }

}
