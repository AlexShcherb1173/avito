package ru.skypro.homework.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NewPasswordTest {

    @Test
    void testNewPasswordGettersAndSetters() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("currentPass");
        newPassword.setNewPassword("newPass123");

        assertEquals("currentPass", newPassword.getCurrentPassword());
        assertEquals("newPass123", newPassword.getNewPassword());
    }

    @Test
    void testNewPasswordEqualsAndHashCode() {
        NewPassword password1 = new NewPassword();
        password1.setCurrentPassword("current");
        password1.setNewPassword("new");

        NewPassword password2 = new NewPassword();
        password2.setCurrentPassword("current");
        password2.setNewPassword("new");

        assertEquals(password1, password2);
        assertEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    void testNewPasswordToString() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("current");
        newPassword.setNewPassword("new");

        String toString = newPassword.toString();
        assertTrue(toString.contains("current"));
        assertTrue(toString.contains("new"));
    }
}