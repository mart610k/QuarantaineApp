package com.example.quarantaine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.quarantaine.Classes.FormatHelper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Array;

public class FormatHelperTests {

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "85659935", "18477532"})
    public void IsPhoneNumberValid(String phoneNumberToValidate) {
        assertTrue(FormatHelper.ValidatePhoneNumber(phoneNumberToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"125", "1588456", "184775321","kashdjkas"})
    public void IsPhoneNumberInvalid(String phoneNumberToValidate) {
        assertFalse(FormatHelper.ValidatePhoneNumber(phoneNumberToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"MartinGroundhogDay2857", "Laura20January", "20BagerJensen45"})
    public void IsUsernameValid(String usernameToValidate) {
        assertTrue(FormatHelper.ValidateUsername(usernameToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"125", "j:_oadu29", "67584903+wåsæxc,vmnbjtui5940+eåwøs-x.,c mjn","LagkageMandend47_3"})
    public void IsUsernameInvalid(String usernameToValidate) {
        assertFalse(FormatHelper.ValidateUsername(usernameToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Abekongen12!", "#Laura20January", "20BagerJensen=45"})
    public void IsPasswordValid(String passwordToValidate) {
        assertTrue(FormatHelper.ValidatePassword(passwordToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"125", "j:_oadu29", "67584903+wåsæxc,vmnbjtui5940+eåwøs-x.,c mjn","LagkageMandend47_3"})
    public void IsPasswordInvalid(String passwordToValidate) {
        assertFalse(FormatHelper.ValidatePassword(passwordToValidate));
    }

    @ParameterizedTest
    @CsvSource(value = {"Abekongen12!:Abekongen12!", "20BagerJensen=45:20BagerJensen=45"}, delimiter = ':')
    public void IsRepeatedPasswordValid(String password,String passwordToValidate) {
        if(FormatHelper.ValidatePassword(passwordToValidate)){
            assertTrue(FormatHelper.ValidateRepeatedPassword(password,passwordToValidate));
        }
    }
}
