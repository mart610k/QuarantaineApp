package com.example.quarantaine;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.quarantaine.Classes.DatabaseHelper;
import com.example.quarantaine.Classes.LocationModel;

import java.util.ArrayList;
import java.util.Calendar;

public class ActiveGPS extends AppCompatActivity {
    LocationManager manager;

    private LocationModel locationModel;

    private final static long LOCATION_GET_DATA_DELAY = 1000L * 60L * 2L;
    private final DatabaseHelper databaseHelper = new DatabaseHelper(ActiveGPS.this);

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_gps);


        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                // Try and see if you can create the location model, Error toast in case it doesn't work
                try {
                    locationModel = new LocationModel(-1, location.getLatitude(), location.getLongitude(), Calendar.getInstance().getTime());
                } catch (Exception e) {
                    Toast.makeText(ActiveGPS.this, "Error Inserting Location Data", Toast.LENGTH_SHORT).show();
                    locationModel = new LocationModel(-1, 0, 0, Calendar.getInstance().getTime());
                }

                // Calls the addLocation function of our datahelper & returns a bool
                Boolean locationAdded = databaseHelper.addLocation(locationModel);

                // toasting whether it was successful or not
                Toast.makeText(ActiveGPS.this, "Location added successful: " + locationAdded, Toast.LENGTH_SHORT).show();

                databaseHelper.close();
            }
        };

        // Uses the location manager to get GPS lokation every 2 minutes
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_GET_DATA_DELAY, 0F, locationListener);

    }
}