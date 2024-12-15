package ru.skypro.homework.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-16T00:47:56.416674900+05:00[Asia/Yekaterinburg]", comments = "Generator version: 7.8.0")
public class Comment {

  private Integer author;/**
   * id автора комментария
   */

  private String authorImage;/**
   *ссылка на аватар автора комментария
   */

  private String authorFirstName;

  private Long createdAt;/**
   *дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
   */

  private Integer pk;/**
   *id комментария
   */

  private String text;

  public Comment(Integer author, String authorImage, String authorFirstName, Long createdAt, Integer pk, String text) {
    this.author = author;
    this.authorImage = authorImage;
    this.authorFirstName = authorFirstName;
    this.createdAt = createdAt;
    this.pk = pk;
    this.text = text;
  }

  public Integer getAuthor() {
    return author;
  }

  public void setAuthor(Integer author) {
    this.author = author;
  }

  public String getAuthorImage() {
    return authorImage;
  }

  public void setAuthorImage(String authorImage) {
    this.authorImage = authorImage;
  }

  public String getAuthorFirstName() {
    return authorFirstName;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public Integer getPk() {
    return pk;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(this.author, comment.author) &&
        Objects.equals(this.authorImage, comment.authorImage) &&
        Objects.equals(this.authorFirstName, comment.authorFirstName) &&
        Objects.equals(this.createdAt, comment.createdAt) &&
        Objects.equals(this.pk, comment.pk) &&
        Objects.equals(this.text, comment.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, authorImage, authorFirstName, createdAt, pk, text);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comment {\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    authorImage: ").append(toIndentedString(authorImage)).append("\n");
    sb.append("    authorFirstName: ").append(toIndentedString(authorFirstName)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    pk: ").append(toIndentedString(pk)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

