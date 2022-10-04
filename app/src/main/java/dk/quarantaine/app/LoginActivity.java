package dk.quarantaine.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import dk.quarantaine.app.classes.DatabaseHelper;
import dk.quarantaine.app.datamodel.APIResponse;
import dk.quarantaine.app.service.StringAPICaller;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import dk.quarantaine.commons.dto.OauthTokenResponseDTO;
import lombok.extern.jackson.Jacksonized;

public class LoginActivity extends AppCompatActivity {

    private final DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Name
        EditText username = findViewById(R.id.username);

        // Phone Number & Listener
        EditText password = findViewById(R.id.password);

        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();
        Button loginButton = (Button) findViewById(R.id.loginbutton);

        loginButton.setOnClickListener(v -> LoginUser(
                username.getText().toString(),
                password.getText().toString()
        ));
    }


    public void LoginUser(String username, String password){

        boolean loggedIn = false;

        try {
            FutureTask<APIResponse> task = new FutureTask<APIResponse>(new StringAPICaller("/api/token", username, password));

            APIResponse result = null;

            new Thread(task).start();
            Log.i("app", "Callng api Response");
            result = task.get(5, TimeUnit.SECONDS);

            JSONObject object = new JSONObject(result.getResponseBody());

            ObjectMapper mapper = new ObjectMapper();

            OauthTokenResponseDTO tokenDTO = mapper.readValue(object.toString(2), OauthTokenResponseDTO.class);

             loggedIn = databaseHelper.insertOrUpdateAccessToken(username, tokenDTO);

            Log.i("app", "Got Response");
        }
        catch (Exception e){
            Log.i("app", "Some exception");
        }

        if (loggedIn){

            Intent changePage = new Intent(LoginActivity.this,ActiveGPS.class);
            startActivity(changePage);
        }
    }

    //OauthTokenResponseDTO token = databaseHelper.getTokenByUsername("username");
}