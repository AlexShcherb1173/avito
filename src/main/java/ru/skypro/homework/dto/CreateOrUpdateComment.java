package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateOrUpdateComment {
    @JsonProperty("text")
    private String text;	// текст комментария
}

