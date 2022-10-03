package com.example.quarantaine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.quarantaine.DTO.APIResponse;
import com.example.quarantaine.service.JSONApiCaller;
import com.example.quarantaine.service.StringAPICaller;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


    // Changes to the Register Page
    public void RegisterPageChange(View view){
        Intent changePage = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(changePage);
    }

    private void Toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}