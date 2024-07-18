package ru.skypro.homework.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateOrUpdateCommentDto {

    @NotNull
    private String text;
}
