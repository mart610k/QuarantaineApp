package dk.quarantaine.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import dk.quarantaine.app.classes.DatabaseHelper;
import dk.quarantaine.app.datamodel.LocationModel;

import java.util.Calendar;

public class ActiveGPSActivity extends AppCompatActivity {
    LocationManager manager;

    private LocationModel locationModel;

    private final static long LOCATION_GET_DATA_DELAY = 1000L*10L ;
    private final DatabaseHelper databaseHelper = new DatabaseHelper(ActiveGPSActivity.this);
    private int datacounter = 0;

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_gps);
        TextView user = findViewById(R.id.user);
        user.setText("Hej " + databaseHelper.getLoggedInUser());

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                // Try and see if you can create the location model, Error toast in case it doesn't work
                try {
                    locationModel = new LocationModel(-1, location.getLatitude(), location.getLongitude(), Calendar.getInstance().getTime());
                } catch (Exception e) {
                    Toast.makeText(ActiveGPSActivity.this, "Error Inserting Location Data", Toast.LENGTH_SHORT).show();
                    locationModel = new LocationModel(-1, 0, 0, Calendar.getInstance().getTime());
                }

                // Calls the addLocation function of our datahelper & returns a bool
                Boolean locationAdded = databaseHelper.addLocation(locationModel);


                // toasting whether it was successful or not
                Toast.makeText(ActiveGPSActivity.this, "Location added successful: " + locationAdded, Toast.LENGTH_SHORT).show();
                databaseHelper.close();

                datacounter++;

                // Shows current count of data
                Toast.makeText(ActiveGPSActivity.this, "Current amount of Data: " +datacounter+ ". Will delete data at 6 inputs", Toast.LENGTH_SHORT).show();

                // If data has been inserted a 6th time, delete data
                if(datacounter > 5) {
                    deleteData();
                    datacounter = 0;
                }
            }
        };

        // Uses the location manager to get GPS lokation every 2 minutes
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_GET_DATA_DELAY, 0F, locationListener);

    }

    // Stops user from going back by the arrow button
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button not implemented", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method that deletes Location Data
     */
    public void deleteData(){
        boolean result = databaseHelper.deleteLocations();
        if(result)
            Toast.makeText(ActiveGPSActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
    }
}