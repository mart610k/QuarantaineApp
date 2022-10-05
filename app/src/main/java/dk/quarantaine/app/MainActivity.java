package dk.quarantaine.app;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import dk.quarantaine.app.classes.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    // Properties
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
        getPermissions();

        String user = databaseHelper.getLoggedInUser();

        if(user != null){
            sendToActiveGPS();
            return;
        }
    }

    /**
     * Gets the permissions requirements and requests for them
     */
    public void getPermissions() {
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(ACCESS_FINE_LOCATION);

        permissionToRequest = requestMissingPermissions(permissions);

        if (permissionToRequest.size() > 0) {
            requestPermissions((String[]) permissionToRequest.toArray(new String[permissionToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    /**
     * Requests the user to approve the missing and required permessions
     * @param permissions permissions to request
     * @return list over approved permissions
     */
    private ArrayList requestMissingPermissions(ArrayList<String> permissions) {
        ArrayList result = new ArrayList();

        for (String permission : permissions) {
            if(!hasPermission(permission)){
                result.add(permission);
            }
        }

        return result;

    }

    /**
     * Checks if the App have the permission
     * @param permission the permission to check
     * @return whether or not the app have the permission
     */
    private Boolean hasPermission(String permission){
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
                    if(!hasPermission(permission)) {
                        rejectedPermissions.add(permission);
                    }
                }

                // If the permission to request is not empty, And asks for permission again if rejected permissions isn't empty
                if(permissionToRequest.size() > 0) {
                    if(shouldShowRequestPermissionRationale(rejectedPermissions.get(0))){
                        showMessageOKCancel("Disse rettighedder er obligatoriske, venligst godkend",
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
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Changes to the register page
    public void sendToRegisterPage(View view){
        Intent changePage = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(changePage);
    }
    
    // Changes to the login page
    public void sendToLoginPage(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // Changes to the register page
    public void sendToActiveGPS(){
        Intent changePage = new Intent(MainActivity.this,ActiveGPSActivity.class);
        startActivity(changePage);
        this.finish();
    }
}
