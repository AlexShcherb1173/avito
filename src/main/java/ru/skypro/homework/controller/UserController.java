package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.service.impl.AvatarServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * <b>Контроллер для управления пользователями в системе. </b> <p>
 * <p>
 * Этот контроллер предоставляет REST API для выполнения операций, связанных с пользователями,
 * включая обновление пароля, получение информации о пользователе, обновление данных пользователя
 * и загрузку/получение аватара. Все методы контроллера защищены аутентификацией.
 * </p>
 * <p>
 * Контроллер использует аннотации Spring для обработки HTTP запросов и взаимодействия с сервисами
 * для выполнения бизнес-логики.
 * </p>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl service;
    private final AvatarServiceImpl avatarService;

    /**
     * <b>Обновляет пароль авторизованного пользователя. </b> <p>
     * </p>
     *
     * <p>
     * Метод принимает новый пароль и аутентификацию пользователя, после чего обновляет пароль
     * в системе. Возвращает статус 200 (OK) при успешном обновлении.
     * </p>
     *
     * @param password объект {@link NewPassword}, содержащий новый пароль пользователя
     * @param authentication объект {@link Authentication}, содержащий информацию о текущем пользователе
     * @return ResponseEntity с HTTP статусом 200 (OK) при успешном обновлении пароля
     */
    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля", description = "updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody NewPassword password, Authentication authentication) {
        service.setPassword(new NewPassword(), authentication);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * <b>Получает информацию об авторизованном пользователе. </b> <p>
     * </p>
     * <p>
     * Метод возвращает объект {@link UserDTO}, содержащий информацию о текущем пользователе,
     * который прошел аутентификацию. Возвращает статус 200 (OK).
     * </p>
     *
     * @param authentication объект {@link Authentication}, содержащий информацию о текущем пользователе
     * @return ResponseEntity с объектом {@link UserDTO} и HTTP статусом 200 (OK)
     */
    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе", description = "getUser")
    public ResponseEntity<UserDTO> getInformationByAuthorizedUser(Authentication authentication) {
        return ResponseEntity.ok(service.getUser(authentication));
    }

    /**
     * <b>Обновляет информацию об авторизованном пользователе. </b> <p>
     * </p>
     * <p>
     * Метод принимает объект {@link UpdateUser}, содержащий новые данные пользователя, и обновляет
     * информацию в системе. Возвращает статус 200 (OK) с обновленной информацией о пользователе.
     * </p>
     *
     * @param update объект {@link UpdateUser}, содержащий новые данные пользователя
     * @param authentication объект {@link Authentication}, содержащий информацию о текущем пользователе
     * @return ResponseEntity с обновленным объектом {@link UpdateUser} и HTTP статусом 200 (OK)
     */
    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе", description = "updateUser")
    public ResponseEntity<UpdateUser> updateInformationFromAuthorizedUser(@RequestBody UpdateUser update, Authentication authentication) {
        return ResponseEntity.ok(service.updateUserInfo(update, authentication));
    }

    /**
     * <b>Обновляет аватар авторизованного пользователя. </b> <p>
     * </p>
     * <p>
     * Метод принимает файл изображения и обновляет аватар текущего пользователя. Возвращает статус 200 (OK)
     * при успешном обновлении аватара.
     * </p>
     *
     * @param image файл изображения, который будет загружен в качестве аватара
     * @param authentication объект {@link Authentication}, содержащий информацию о текущем пользователе
     * @return ResponseEntity с HTTP статусом 200 (OK) при успешном обновлении аватара
     * @throws IOException если произошла ошибка при обработке файла изображения
     */
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя", description = "updateAvatarUser")
    public ResponseEntity<?> updateAvatarFromAuthorizedUser(@RequestPart MultipartFile image, Authentication authentication) throws IOException {
        service.updateUserAvatar(image, authentication);
        return ResponseEntity.ok().build();
    }

    /**
     * <b>Получает аватар пользователя по его идентификатору. </b> <p>
     * </p>
     * <p>
     * Метод возвращает байтовый массив изображения аватара запрашиваемого пользователя.
     * Возвращает статус 200 (OK) при успешном получении изображения.
     * </p>
     *
     * @param id идентификатор пользователя, для которого необходимо получить аватар
     * @return ResponseEntity с байтовым массивом изображения аватара и соответствующим HTTP статусом
     */
    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE
    })
    @Operation(summary = "Получение аватара пользователя", description = "getImage")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") long id) {
        byte[] image = avatarService.getAvatar(id).getData();
        return ResponseEntity.ok(image);
    }
}
