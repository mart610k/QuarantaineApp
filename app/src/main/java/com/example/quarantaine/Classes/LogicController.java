package com.example.quarantaine.Classes;

import com.example.quarantaine.DTO.RegisterUserDTO;
import com.google.gson.JsonObject;

public class LogicController {

    static private Boolean reply = null;

    // Laver Registrer bruger objektet og sender videre til Service
    public static Boolean RegisterUser(String username, String password, String name, String phoneNumber) {

        RegisterUserDTO registerUserDTO = new RegisterUserDTO(username, password, name, phoneNumber);

        reply = ServiceController.RegisterUser(registerUserDTO);
        return reply;
    }

}
