package ru.skypro.homework.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateUserDto {
    @JsonProperty("firstName")
    @Size(min = 3, max = 10)
    private String firstName;
    @JsonProperty("lastName")
    @Size(min = 3, max = 10)
    private String lastName;
    @JsonProperty("phone")
    private String phone;
}
