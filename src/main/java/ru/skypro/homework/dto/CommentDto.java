package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Comment data")
public class CommentDto {
    @Schema(description = "Comment ID")
    private Integer id;

    @Schema(description = "Text")
    private String text;

    @Schema(description = "Author ID")
    private Integer author;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getAuthor() { return author; }
    public void setAuthor(Integer author) { this.author = author; }
}
