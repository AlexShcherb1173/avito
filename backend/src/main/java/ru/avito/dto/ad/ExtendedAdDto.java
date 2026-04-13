package ru.avito.dto.ad;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ExtendedAdDto {
    private Integer pk;
    private String title;
    private String description;
    private Integer price;
    private String image;
    private Integer author;
    private String authorFirstName;
    private String authorLastName;
    private String email;
    private String phone; }