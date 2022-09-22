package com.example.quarantaine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.example.quarantaine.Classes.*;

public class FormatHelperTests {

    FormatHelper formatHelper = new FormatHelper();

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "85659935", "18477532"})
    public void IsPhoneNumberValid(String phoneNumberToValidate) {
        assertTrue(formatHelper.ValidatePhoneNumber(phoneNumberToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"125", "1588456", "184775321","kashdjkas"})
    public void IsPhoneNumberInvalid(String phoneNumberToValidate) {
        assertFalse(formatHelper.ValidatePhoneNumber(phoneNumberToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"MartinGroundhogDay2857", "Laura20January", "20BagerJensen45"})
    public void IsUsernameValid(String usernameToValidate) {
        assertTrue(formatHelper.ValidateUsername(usernameToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"125", "j:_oadu29", "67584903+wåsæxc,vmnbjtui5940+eåwøs-x.,c mjn","LagkageMandend47_3"})
    public void IsUsernameInvalid(String usernameToValidate) {
        assertFalse(formatHelper.ValidatePhoneNumber(usernameToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"MartinGroundhogDay2857!", "#Laura20January", "20BagerJensen%45"})
    public void IsPasswordValid(String PasswordToValidate) {
        assertTrue(formatHelper.ValidateUsername(PasswordToValidate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"125", "j:_oadu29", "67584903+wåsæxc,vmnbjtui5940+eåwøs-x.,c mjn","LagkageMandend47_3"})
    public void IsPasswordInvalid(String PasswordToValidate) {
        assertFalse(formatHelper.ValidatePhoneNumber(PasswordToValidate));
    }
}
