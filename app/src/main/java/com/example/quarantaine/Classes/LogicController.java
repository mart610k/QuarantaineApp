package com.example.quarantaine.Classes;

import com.example.quarantaine.DTO.RegisterUserDTO;

public class LogicController {

    static private boolean reply = false;

    // Laver Registrer bruger objektet og sender videre til Service
    public static boolean RegisterUser(String username, String password, String name, String phoneNumber) {
        RegisterUserDTO registerUserDTO = null;
        try {
            registerUserDTO = new RegisterUserDTO(username, password, name, phoneNumber);

        }
        catch (Exception e) {

        }

        reply = ServiceController.RegisterUser(registerUserDTO);

        return reply;
    }

}
