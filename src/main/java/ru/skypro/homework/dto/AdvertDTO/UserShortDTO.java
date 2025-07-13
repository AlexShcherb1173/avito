package ru.skypro.homework.dto.AdvertDTO;
/**
 * DTO для краткого представления пользователя (в составе других DTO)
 */
public class UserShortDTO {
    private Long id; // ID пользователя
    private String username; // Имя пользователя
    private String avatar; // URL аватарки

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
