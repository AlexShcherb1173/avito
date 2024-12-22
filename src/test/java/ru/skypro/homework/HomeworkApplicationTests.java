package ru.skypro.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.controller.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeworkApplicationTests {

    @Autowired
    private AdController adController;
    @Autowired
    private AdImageController adImageController;
    @Autowired
    private CommentController commentController;
    @Autowired
    private LoginController loginController;
    @Autowired
    private RegisterController registerController;
    @Autowired
    private UserAvatarController userAvatarController;
    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(adController);
        Assertions.assertNotNull(adImageController);
        Assertions.assertNotNull(commentController);
        Assertions.assertNotNull(loginController);
        Assertions.assertNotNull(registerController);
        Assertions.assertNotNull(userAvatarController);
        Assertions.assertNotNull(userController);
    }

}
