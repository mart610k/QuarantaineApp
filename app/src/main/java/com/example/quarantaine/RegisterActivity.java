package com.example.quarantaine;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quarantaine.Classes.*;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // input validation


        // Username & Listener
        EditText username = findViewById(R.id.username);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean state = FormatHelper.ValidateUsername(username.getText().toString());
                ChangeColour(state,username);
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
                Toast(password.getText().toString());
                boolean state = FormatHelper.ValidatePassword(password.getText().toString());
                ChangeColour(state,password);
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
                boolean state = FormatHelper.ValidateRepeatedPassword(password.getText().toString(),passwordRepeat.getText().toString());
                ChangeColour(state, passwordRepeat);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Phone Number & Listener
        EditText phone = findViewById(R.id.phoneNumber);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean state = FormatHelper.ValidatePhoneNumber(phone.getText().toString());
                ChangeColour(state,phone);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    private void ChangeColour(boolean value, EditText input){

        if(!value){
            input.setBackgroundColor(Color.parseColor("#f80202"));
        } else {
            input.setBackgroundColor(Color.parseColor("#00ff80"));
        }
        if(input.getText().length() == 0) {
            input.setBackgroundResource(R.drawable.button_border);
        }
    }

    public void Toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}