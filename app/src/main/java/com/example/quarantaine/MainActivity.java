package com.example.quarantaine;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetPermissions();
    }

    private ArrayList<String> permissionToRequest;
    private ArrayList<String> rejectedPermissions = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;

    public void GetPermissions() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(ACCESS_BACKGROUND_LOCATION);



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
