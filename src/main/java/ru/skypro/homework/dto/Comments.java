package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class Comments {

    @Schema(description = "общее количество комментариев")
    private int type;

    @Schema
    private List<Comment> results;
}
