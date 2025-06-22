package ru.skypro.homework;

import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTest {
    @Test
    public void testGetAndSet() {
        Register register = new Register();
        register.setUsername("login");
        register.setPassword("password");
        register.setFirstName("User1");
        register.setLastName("Family");
        register.setPhone("89996665544");
        register.setRole(Role.USER);

        assertEquals("login", register.getUsername());
        assertEquals("password", register.getPassword());
        assertEquals("User1", register.getFirstName());
        assertEquals("Family", register.getLastName());
        assertEquals("89996665544", register.getPhone());
        assertEquals(Role.USER, register.getRole());
    }
}
