package ru.avito.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void shouldContainRoles() {
        assertEquals(Role.USER, Role.valueOf("USER"));
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
    }

    @Test
    void shouldHaveTwoValues() {
        assertEquals(2, Role.values().length);
    }

    @Test
    void shouldConvertToString() {
        assertEquals("USER", Role.USER.name());
        assertEquals("ADMIN", Role.ADMIN.name());
    }
}