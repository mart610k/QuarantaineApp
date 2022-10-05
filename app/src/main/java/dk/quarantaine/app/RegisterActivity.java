package dk.quarantaine.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.quarantaine.app.classes.*;
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
                changeColour(state,username, "username");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Password & Listener
        EditText password = findViewById(R.id.password);
        password.setError("1 stort bogstav"+ "\r\n"+ "1 lille bogstav"+ "\r\n"+ "1 tegn"+ "\r\n" +"1 tal" + "\r\n"+ "mindst 8 tegn");
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean state = FormatHelper.validatePassword(password.getText().toString());
                changeColour(state,password,"password");
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
                changeColour(state, passwordRepeat, "reppass");
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
                changeColour(state,phone,"phone");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button registerButton = (Button) findViewById(R.id.registrer);

        registerButton.setOnClickListener(v -> registerUser(
                username.getText().toString(),
                password.getText().toString(),
                name.getText().toString(),
                phone.getText().toString()
        ));
    }



    /**
     * Registers the user by calling user logic
     * @param username the name that the user will log in with
     * @param password the password the user will log in with
     * @param name the display name
     * @param phone the user's phone number
     */
    public void registerUser(String username, String password, String name, String phone){
        if(!name.equals("") || !username.equals("") || !password.equals("") || !phone.equals("")) {
            // Sender data videre til næste lag

            boolean reply = UserLogic.registerUser(username, password, name, phone);

            if(reply){
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

    /**
     * Changes the color on a text element based on a boolean
     * @param value controls if the color should be red or green
     * @param input the field to change color on.
     * @param type field type specific actions
     */
    private void changeColour(boolean value, EditText input, String type){

        if(!value){
            if (type.equals("password")) {
                input.setError("1 stort bogstav"+ "\r\n"+ "1 lille bogstav"+ "\r\n"+ "1 tegn"+ "\r\n" +"1 tal" + "\r\n"+ "mindst 8 tegn");
            }
            input.setBackgroundColor(Color.parseColor("#ffa0a0"));
        } else {
            input.setBackgroundColor(Color.parseColor("#00ff80"));
        }
        if(input.getText().length() == 0) {
            input.setBackgroundResource(R.drawable.button_border);
        }
    }

    /**
     * Helper method to show a Toast on the screen with text
     * @param text the text to present
     */
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}