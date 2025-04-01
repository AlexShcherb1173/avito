package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "User")
@Data
@Getter
@Setter
public class UserDTO {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

    public UserDTO() {
    }

    public UserDTO(String mail, String john, String doe, String number) {

    }
}
