package com.example.quarantaine;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    LocationManager manager;

    private static final int HANDLER_DELAY = 1000;
    private ArrayList<String> permissionToRequest;
    private ArrayList<String> rejectedPermissions = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();

    FusedLocationProviderClient locationProviderClient;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetPermissions();

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Toast.makeText(MainActivity.this, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        };
        Button btn = findViewById(R.id.login);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000L,0F, locationListener);

    }

    public void GetPermissions() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);


        permissionToRequest = findUnaskedPermission(permissions);

        if (permissionToRequest.size() > 0) {
            requestPermissions((String[]) permissionToRequest.toArray(new String[permissionToRequest.size()]), ALL_PERMISSIONS_RESULT);

        }
    }

    private ArrayList findUnaskedPermission(ArrayList<String> permissions) {
        ArrayList result = new ArrayList();

        for (String permission : permissions) {
            if(!HasPermission(permission)){
                result.add(permission);
            }
        }

        return result;

    }

    private Boolean HasPermission(String permission){
        return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String permission : permissionToRequest){
                    if(!HasPermission(permission)) {
                        rejectedPermissions.add(permission);
                    }
                }

                if(permissionToRequest.size() > 0) {
                    if(shouldShowRequestPermissionRationale(rejectedPermissions.get(0))){
                        ShowMessageOKCancel("Disse rettighedder er obligatoriske, venligst godkend",
                                new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(rejectedPermissions.toArray(new String[rejectedPermissions.size()]),ALL_PERMISSIONS_RESULT);
                                    }
                                });
                        return;
                    }
                }
                break;
        }
    }



    private void ShowMessageOKCancel(String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Changes to the Register Page
    public void RegisterPageChange(View view){
        Intent changePage = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(changePage);

    }


}
