package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Advertisement data")
public class AdDto {
    @Schema(description = "Ad ID")
    private Integer id;

    @Schema(description = "Title")
    private String title;

    @Schema(description = "Price")
    private Integer price;

    @Schema(description = "Description")
    private String description;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
