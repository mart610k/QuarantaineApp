package com.example.quarantaine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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