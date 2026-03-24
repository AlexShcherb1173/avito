package ru.avito.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateOrUpdateCommentRequest {

    @NotBlank(message = "Comment text must not be blank")
    private String text;
}