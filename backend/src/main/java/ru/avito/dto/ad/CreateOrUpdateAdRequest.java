package ru.avito.dto.ad;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateAdRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(min = 8, max = 32, message = "Title length must be between 8 and 32 characters")
    private String title;

    @NotNull(message = "Price must not be null")
    @Min(value = 1, message = "Price must be greater than 0")
    private Integer price;

    @NotBlank(message = "Description must not be blank")
    @Size(min = 8, max = 64, message = "Description length must be between 8 and 64 characters")
    private String description;
}