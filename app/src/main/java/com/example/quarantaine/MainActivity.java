package com.example.quarantaine;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quarantaine.Classes.DatabaseHelper;

import java.util.ArrayList;

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActiveGPS.class);
                startActivity(intent);
            }
        });

    }

    // Gets the needed permissions of the app
    public void GetPermissions() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionToRequest = findUnaskedPermission(permissions);

        if (permissionToRequest.size() > 0) {
            requestPermissions((String[]) permissionToRequest.toArray(new String[permissionToRequest.size()]), ALL_PERMISSIONS_RESULT);

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
