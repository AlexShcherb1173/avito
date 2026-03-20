package ru.avito.dto.ad;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateOrUpdateAdRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotNull(message = "Price must not be null")
    private Integer price;

    @NotBlank(message = "Description must not be blank")
    private String description;
}