package ru.skypro.homework.dto;

import lombok.*;

@Data
public class ExtendedAd extends Ad {
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String phone;
    
}
