package ru.skypro.homework.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skypro.homework.entity.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class UserDto {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonProperty("image")
    private String image;

}
