/* DTO Register - для регистрации. Напиши сюда если что то поменяется*/

package ru.skypro.homework.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import ru.skypro.homework.entity.Role;

import javax.validation.constraints.Size;

@Data
@Getter
public class RegisterDto {
    // Dto для регистрации пользователя

    @Size(min = 4, max = 32)
    @JsonProperty("username")
    private String username;

    @Size(min = 8, max = 16)
    @JsonProperty("password")
    private String password;

    @Size(min = 2, max = 16)
    @JsonProperty("firstName")
    private String firstName;

    @Size(min = 2, max = 16)
    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("role")
    private Role role;

    
}
