package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateOrUpdateComment")
@Data
public class CreateOrUpdateCommentDTO {
    @NotNull
    @Size(min = 8, max = 64, message = "Длина комментария должна быть от 8 до 64 символов")
    private String text;
}
