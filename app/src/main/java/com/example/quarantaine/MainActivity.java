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
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.quarantaine.DTO.APIResponse;
import com.example.quarantaine.service.JSONApiCaller;
import com.example.quarantaine.service.StringAPICaller;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quarantaine.Classes.DatabaseHelper;
import com.example.quarantaine.Classes.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {
    private ArrayList<String> permissionToRequest;
    private ArrayList<String> rejectedPermissions = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();



    private final static int ALL_PERMISSIONS_RESULT = 101;
    private final DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetPermissions();

        Button btn = findViewById(R.id.login);



    }

    // Gets the needed permissions of the app
    public void GetPermissions() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionToRequest = findUnaskedPermission(permissions);

        if (permissionToRequest.size() > 0) {
            requestPermissions((String[]) permissionToRequest.toArray(new String[permissionToRequest.size()]), ALL_PERMISSIONS_RESULT);


        try {

            FutureTask<APIResponse> task = new FutureTask<APIResponse>(new StringAPICaller("/api/token", "username", "Passw0rd!"));

            APIResponse result = null;

            new Thread(task).start();
            Log.i("app", "Callng api Response");
            result = task.get(5, TimeUnit.SECONDS);

            Log.i("app", "Got Response");
        }
        catch (Exception e){
            Log.i("app", "Some exception");
        }
    }

    // Finds out which of these permissions are unasked
    private ArrayList findUnaskedPermission(ArrayList<String> permissions) {
        ArrayList result = new ArrayList();

        for (String permission : permissions) {
            if(!HasPermission(permission)){
                result.add(permission);
            }
        }

        return result;

    }

    // Checks if the permission is granted and returns true or false
    private Boolean HasPermission(String permission){
        return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    // callback on the permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                // If the permission was denied, add to rejected permission list
                for (String permission : permissionToRequest){
                    if(!HasPermission(permission)) {
                        rejectedPermissions.add(permission);
                    }
                }

                // If the permission to request is not empty, And asks for permission again if rejected permissions isn't empty
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

    // Shows a message in an alert dialog
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
