//package ru.skypro.homework.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import ru.skypro.homework.dto.NewPassword;
//import ru.skypro.homework.dto.UpdateUser;
//import ru.skypro.homework.dto.User;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class UserService {
//
//    private Map<String, User> userDatabase = new HashMap<>(); // Хранение пользователей в памяти
//    private String currentUserId = "user1"; // Пример текущего пользователя
//
//    public boolean updatePassword(String currentPassword, String newPassword) {
//        User currentUser = getCurrentUser();
//        // Проверка текущего пароля (в реальном приложении следует использовать хэширование паролей)
//        if (currentUser.getPassword().equals(currentPassword)) {
//            currentUser.setPassword(newPassword);
//            return true;
//        }
//        return false; // Возврат false, если текущий пароль неверен
//    }
//
//    public User getCurrentUser() {
//        // Получение текущего пользователя по ID (в реальном приложении это может быть получено из контекста безопасности)
//        return userDatabase.get(currentUserId);
//    }
//
//    public void updateUser(UpdateUser updateUserDto) {
//        User currentUser = getCurrentUser();
//        // Обновление информации о пользователе
//        if (updateUserDto.getName() != null) {
//            currentUser.setName(updateUserDto.getName());
//        }
//        if (updateUserDto.getEmail() != null) {
//            currentUser.setEmail(updateUserDto.getEmail());
//        }
//        // Дополнительные поля могут быть добавлены здесь
//    }
//
//    public void updateUserImage(MultipartFile image) {
//        User currentUser = getCurrentUser();
//        // Логика сохранения изображения (например, сохранение на диск или в облачное хранилище)
//        // Здесь можно добавить код для обработки файла и обновления аватара пользователя
//        currentUser.setImageUrl("url_to_updated_image"); // Пример обновления URL изображения
//    }
//}