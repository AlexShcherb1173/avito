package ru.avito.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateCommentRequest {

    @NotBlank(message = "Comment text must not be blank")
    @Size(min = 8, max = 64, message = "Comment length must be between 8 and 64 characters")
    private String text;
}