package ru.skypro.homework.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skypro.homework.dto.Role;

import java.time.format.SignStyle;
@Data
public class UserDTO {
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
    private Role role;
    @JsonProperty("image")
    private String image;

}
