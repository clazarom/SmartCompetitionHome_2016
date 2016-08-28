package iit.ece.edu.smartcompetitionhouse;

/**
 * Created by Cat on 8/26/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.hardware.*;

import android.widget.Toast;

public class CountSteps implements SensorEventListener {

    private SensorManager sensorManager;
    private float initial_count;
    private boolean started;
    private static float count;
    boolean activityRunning;

    public CountSteps(Context ctx){
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        count = 0;
        //Activity start
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(ctx, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
        //Start counting steps
        started = false;
        initial_count = 0;
    }

    public CountSteps(Context ctx, float initial){
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        //Activity start
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(ctx, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
        //Start counting steps
        started = true;
        initial_count = initial;
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            if (!started) {
                initial_count = event.values[0];
                started = true;
            }
            System.out.println(event.values[0] + "vs "+initial_count);
            count = event.values[0] - initial_count;


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public float getSteps(){
        System.out.println("Counter: "+count);
        return count;
    }

    public float  getInitialSteps(){
        return initial_count;
    }


    public void stopCounting(){
        activityRunning = false;
        count = 0;
        started = false;
        initial_count = 0;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            //Stop listening
            sensorManager.unregisterListener(this);

        }

    }
}
