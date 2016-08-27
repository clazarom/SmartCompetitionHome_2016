package iit.ece.edu.smartcompetitionhouse;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by Cat on 8/26/2016.
 */
public class PermissionsManager {
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

    private Context pContext;
    private ActivityCompat permissionActivity;

    public PermissionsManager(Context ctx, ActivityCompat act){
        pContext = ctx;
        permissionActivity = act;

    }

    public  void requestPermissions() {
        // ****** REQUESTS *****
        //Location
        //permissionActivity.requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        //Check permissions
        if (!canAccessLocation() || !canAccessMemory()) {
            //Do nothing
            //permissionActivity.requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            Toast.makeText(pContext, "Request Permissions", Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(pContext, "Permission alright!", Toast.LENGTH_LONG).show();

            //Start tracking service
            System.out.println("Location permission ");
            //Start tracking
            if (ContextCompat.checkSelfPermission(pContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Start tracking ");
               /* mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, mLocationListener);
            } else {
                //requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                System.out.println("Manager ");
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, mLocationListener);*/

            }
            //Request write read:
       /* String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, WRITE_REQUEST_CODE);
        String[] permissions2 = {Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions2, READ_REQUEST_CODE);*/

        }
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
        return false; //(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }
}
