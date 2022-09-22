package com.example.quarantaine.Classes;

public class FormatHelper {


    public static boolean ValidatePhoneNumber(String numberToValidate){
        return numberToValidate.matches("^[0-9]{8}$");
    }

    public static boolean ValidateUsername(String usernameToValidate) {
        return usernameToValidate.matches("^[^_<>%$.,/:;*-+<> ][0-z][^_<>%$.:;,/*-+<> ]{5,128}$");
    }

    public static boolean ValidatePassword(String passwordToValidate) {
        return passwordToValidate.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%#^*?&])[A-Za-z\\\\d@$!#%^*?&]{8,}$");
    }
}
