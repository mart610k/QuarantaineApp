package com.example.quarantaine.Classes;

import android.util.Log;

import com.example.quarantaine.DTO.APIResponse;
import com.example.quarantaine.service.JSONApiCaller;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import dk.quarantaine.commons.dto.RegisterUserDTO;

public class UserLogic {


    // Laver Registrer bruger objektet og sender videre til Service
    public static boolean RegisterUser(String username, String password, String name, String phoneNumber) {
        boolean reply = false;

        try {
            RegisterUserDTO registerUserDTO = new RegisterUserDTO();

            registerUserDTO.setName(name);
            registerUserDTO.setPassword(password);
            registerUserDTO.setUsername(username);
            registerUserDTO.setPhoneNumber(phoneNumber);

            Log.i("app", registerUserDTO.toJson().toString());

            FutureTask<APIResponse> task = new FutureTask<APIResponse>(new JSONApiCaller("/api/register",registerUserDTO));

            APIResponse result = null;

            new Thread(task).start();
            Log.i("app","Callng api Response");
            result = task.get(5,TimeUnit.SECONDS);

            Log.i("app","Got Response");


            if(result.getResponseCode() == 200){
                reply = true;

            }
            else{
                reply = false;
            }


        }
        catch (Exception e) {

        }
        return reply;
    }

}
