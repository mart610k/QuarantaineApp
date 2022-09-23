package com.example.quarantaine.Classes;

import android.app.Activity;

import com.example.quarantaine.Classes.DTO.RegisterUserDTO;

public class LogicController {

    static private boolean reply = false;
    public static boolean RegisterUser(String username, String password, String name, String phoneNumber) {

        RegisterUserDTO registerUserDTO = new RegisterUserDTO(username, password, name, phoneNumber);

        reply = ServiceController.RegisterUser(registerUserDTO);

        return reply;
    }

}
