package com.example.quarantaine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quarantaine.Classes.*;
import dk.quarantaine.commons.helpers.FormatHelper;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Username & Listener
        EditText username = findViewById(R.id.username);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean state = FormatHelper.validateUsername(username.getText().toString());
                ChangeColour(state,username, "username");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Password & Listener
        EditText password = findViewById(R.id.password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean state = FormatHelper.validatePassword(password.getText().toString());
                ChangeColour(state,password,"password");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // PasswordRepeat & Listener
        EditText passwordRepeat = findViewById(R.id.repeatPass);
        passwordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean state = FormatHelper.validateRepeatedPassword(password.getText().toString(),passwordRepeat.getText().toString());
                ChangeColour(state, passwordRepeat, "reppass");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Name
        EditText name = findViewById(R.id.name);

        // Phone Number & Listener
        EditText phone = findViewById(R.id.phoneNumber);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean state = FormatHelper.validatePhoneNumber(phone.getText().toString());
                ChangeColour(state,phone,"phone");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button registerButton = (Button) findViewById(R.id.registrer);

        registerButton.setOnClickListener(v -> Register(
                username.getText().toString(),
                password.getText().toString(),
                name.getText().toString(),
                phone.getText().toString()
        ));
    }



    // Start på registering af bruger, med nødvendige input
    public void Register(String username, String pass, String name, String phone){
        if(!name.equals("") || !username.equals("") || !pass.equals("") || !phone.equals("")) {
            // Sender data videre til næste lag

            boolean reply = UserLogic.RegisterUser(username, pass, name, phone);

            if(reply){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sender til Forside");
                builder.setTitle("Redirect");
                AlertDialog alert = builder.create();

                alert.show();

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast("Registrering fejlet");
            }
        }
        else {
            Toast("Udfyld venligst tomme felter");
        }
    }

    // Skifter farve på tekstfelt baseret på om input er korrekt eller ukorrekt
    private void ChangeColour(boolean value, EditText input, String type){

        if(!value){
            if (type.equals("password")) {
                input.setError("1 stort bogstav"+ "\r\n"+ "1 lille bogstav"+ "\r\n"+ "1 tegn"+ "\r\n" +"1 tal");
            }
            input.setBackgroundColor(Color.parseColor("#ffa0a0"));
        } else {
            input.setBackgroundColor(Color.parseColor("#00ff80"));
        }
        if(input.getText().length() == 0) {
            input.setBackgroundResource(R.drawable.button_border);
        }
    }

    // Printer en toast baseret på String input
    private void Toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}