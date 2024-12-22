package ru.skypro.homework.constant;

import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;

import java.nio.file.Path;

public final class StaticForTests {

    public static final String IMAGE_JPG_PATH_STRING = "./src/test/java/ru/skypro/homework/constant/test.jpg";
    public static final Path IMAGE_JPG_PATH = Path.of(IMAGE_JPG_PATH_STRING);

    public static final String AD_1_TITLE = "Test Ad 1 Title";
    public static final String AD_1_DESCRIPTION = "Test Ad 1 Description";
    public static final Integer AD_1_PRICE = 123;
    public static final CreateOrUpdateAdDto C_U_AD1 = new CreateOrUpdateAdDto(AD_1_TITLE, AD_1_PRICE, AD_1_DESCRIPTION);

    public static final String AD_2_TITLE = "Test Ad 2 Title";
    public static final String AD_2_DESCRIPTION = "Test Ad 2 Description";
    public static final Integer AD_2_PRICE = 234;
    public static final CreateOrUpdateAdDto C_U_AD2 = new CreateOrUpdateAdDto(AD_2_TITLE, AD_2_PRICE, AD_2_DESCRIPTION);

    public static final String AD_3_TITLE = "Test Ad 3 Title";
    public static final String AD_3_DESCRIPTION = "Test Ad 3 Description";
    public static final Integer AD_3_PRICE = 345;
    public static final CreateOrUpdateAdDto C_U_AD3 = new CreateOrUpdateAdDto(AD_3_TITLE, AD_3_PRICE, AD_3_DESCRIPTION);

    public static final Integer USER_ADMIN_ID = 1;
    public static final String USER_ADMIN_EMAIL = "admin@mail.ru";
    public static final String USER_ADMIN_PASSWORD = "QwertyA!";
    public static final String USER_ADMIN_FIRST_NAME = "adminfn";
    public static final String USER_ADMIN_LAST_NAME = "adminln";
    public static final String USER_ADMIN_PHONE = "+7(912)345-67-89";
    public static final String USER_ADMIN_AUTH_TOKEN = "Basic YWRtaW5AbWFpbC5ydTpRd2VydHlBIQ";
    public static final String USER_ADMIN_PASSWORD_HASH = "$2a$10$PHqLP4.5DV8NXkToIWpaKehb2rKIaSaxv74hAmrBifkzWBTqJNkyS";
    public static final Role USER_ADMIN_ROLE = Role.ADMIN;
    public static final RegisterDto USER_ADMIN_REGISTER_DTO = new RegisterDto(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD, USER_ADMIN_FIRST_NAME, USER_ADMIN_LAST_NAME, USER_ADMIN_PHONE, USER_ADMIN_ROLE);
    public static final User USER_ADMIN = new User(USER_ADMIN_ID, USER_ADMIN_EMAIL, USER_ADMIN_FIRST_NAME, USER_ADMIN_LAST_NAME, USER_ADMIN_PHONE, USER_ADMIN_ROLE, USER_ADMIN_PASSWORD,1,"");

    public static final Integer USER_USER1_ID = 2;
    public static final String USER_USER1_EMAIL = "USERONE@mail.ru";
    public static final String USER_USER1_PASSWORD = "Qwerty1!";
    public static final String USER_USER1_FIRST_NAME = "USERONEfn";
    public static final String USER_USER1_LAST_NAME = "USERONEln";
    public static final String USER_USER1_PHONE = "+7(912)345-67-89";
    public static final String USER_USER1_PASSWORD_HASH = "$2a$10$4eG1.GBPawmSx7fuMwQOE.hz7zVjr.AERz7KSCt0PuQg7iHRVpKh2";
    public static final Role USER_USER1_ROLE = Role.USER;
    public static final RegisterDto USER_USER1_REGISTER_DTO = new RegisterDto(USER_USER1_EMAIL, USER_USER1_PASSWORD, USER_USER1_FIRST_NAME, USER_USER1_LAST_NAME, USER_USER1_PHONE, USER_USER1_ROLE);
    public static final User USER_USER1 = new User(USER_USER1_ID, USER_USER1_EMAIL, USER_USER1_FIRST_NAME, USER_USER1_LAST_NAME, USER_USER1_PHONE, USER_USER1_ROLE, USER_USER1_PASSWORD,1,"");

    public static final Integer USER_USER2_ID = 3;
    public static final String USER_USER2_EMAIL = "USERTWO@mail.ru";
    public static final String USER_USER2_PASSWORD = "Qwerty2!";
    public static final String USER_USER2_FIRST_NAME = "USERTWOfn";
    public static final String USER_USER2_LAST_NAME = "USERTWOln";
    public static final String USER_USER2_PHONE = "+7(912)345-67-89";
    public static final Role USER_USER2_ROLE = Role.USER;
    public static final RegisterDto USER_USER2_REGISTER_DTO = new RegisterDto(USER_USER2_EMAIL, USER_USER2_PASSWORD, USER_USER2_FIRST_NAME, USER_USER2_LAST_NAME, USER_USER2_PHONE, USER_USER2_ROLE);
    public static final User USER_USER2 = new User(USER_USER2_ID, USER_USER2_EMAIL, USER_USER2_FIRST_NAME, USER_USER2_LAST_NAME, USER_USER2_PHONE, USER_USER2_ROLE, USER_USER2_PASSWORD,1,"");

    public static final String TEST_COMMENT_TEXT = "Test comment text";

}
