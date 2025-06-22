package ru.skypro.homework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ExtendedAdDTO extends AdResponseDTO {
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String phone;
}
