package ru.skypro.homework.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Comment
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-03-16T10:55:08.933404864Z[GMT]")


public class Comment   {
  @JsonProperty("author")
  private Integer author = null;

  @JsonProperty("authorImage")
  private String authorImage = null;

  @JsonProperty("authorFirstName")
  private String authorFirstName = null;

  @JsonProperty("createdAt")
  private Long createdAt = null;

  @JsonProperty("pk")
  private Integer pk = null;

  @JsonProperty("text")
  private String text = null;

  public Comment author(Integer author) {
    this.author = author;
    return this;
  }

  /**
   * id автора комментария
   * @return author
   **/
  @Schema(description = "id автора комментария")
  
    public Integer getAuthor() {
    return author;
  }

  public void setAuthor(Integer author) {
    this.author = author;
  }

  public Comment authorImage(String authorImage) {
    this.authorImage = authorImage;
    return this;
  }

  /**
   * ссылка на аватар автора комментария
   * @return authorImage
   **/
  @Schema(description = "ссылка на аватар автора комментария")
  
    public String getAuthorImage() {
    return authorImage;
  }

  public void setAuthorImage(String authorImage) {
    this.authorImage = authorImage;
  }

  public Comment authorFirstName(String authorFirstName) {
    this.authorFirstName = authorFirstName;
    return this;
  }

  /**
   * имя создателя комментария
   * @return authorFirstName
   **/
  @Schema(description = "имя создателя комментария")
  
    public String getAuthorFirstName() {
    return authorFirstName;
  }

  public void setAuthorFirstName(String authorFirstName) {
    this.authorFirstName = authorFirstName;
  }

  public Comment createdAt(Long createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
   * @return createdAt
   **/
  @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
  
    public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public Comment pk(Integer pk) {
    this.pk = pk;
    return this;
  }

  /**
   * id комментария
   * @return pk
   **/
  @Schema(description = "id комментария")
  
    public Integer getPk() {
    return pk;
  }

  public void setPk(Integer pk) {
    this.pk = pk;
  }

  public Comment text(String text) {
    this.text = text;
    return this;
  }

  /**
   * текст комментария
   * @return text
   **/
  @Schema(description = "текст комментария")
  
    public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }


  @Override
  public boolean equals(java.lang.Object o) {
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
