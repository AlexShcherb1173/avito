package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;
/**
 * DTO (Data Transfer Object) для программного интерфейса пользователя.
 * <p>
 * В классе представлены данные для программного интерфейса пользователя, а именно:
 * уникальный идентификатор пользователя, его логин, имя, фамилия, телефон, роль, а также ссылка на аватар пользователя.
 * Также в классе реализованы конструкторы, геттеры, сеттеры, equals, hashCode и toString.
 * </p>
 */
public class UserApiDto {
    @Schema(type = "integer", format = "int32", description = "id пользователя")
    private Integer id;

    @Schema(type = "string", description = "логин пользователя")
    private String email;

    @Schema(type = "string", description = "имя пользователя")
    private String firstName;

    @Schema(type = "string", description = "фамилия пользователя")
    private String lastName;

    @Schema(type = "string", description = "телефон пользователя")
    private String phone;

    @Schema(type = "string", description = "роль пользователя", allowableValues = {"USER", "ADMIN"})
    private Role role;

    @Schema(type = "string", description = "ссылка на аватар пользователя")
    private String image;

    public UserApiDto() {
    }

    public UserApiDto(Integer id, String email, String firstName, String lastName, String phone, Role role, String image) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", phone='" + phone + '\'' + ", role=" + role + ", image='" + image + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserApiDto userApiDto = (UserApiDto) o;
        return Objects.equals(id, userApiDto.id) && Objects.equals(email, userApiDto.email) && Objects.equals(firstName, userApiDto.firstName) && Objects.equals(lastName, userApiDto.lastName) && Objects.equals(phone, userApiDto.phone) && role == userApiDto.role && Objects.equals(image, userApiDto.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, phone, role, image);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
