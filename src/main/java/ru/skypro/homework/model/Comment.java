package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * Comment
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class Comment {

  private @Nullable Integer author;

  private @Nullable String authorImage;

  private @Nullable String authorFirstName;

  private @Nullable Long createdAt;

  private @Nullable Integer pk;

  private @Nullable String text;

  public Comment author(@Nullable Integer author) {
    this.author = author;
    return this;
  }

  /**
   * id автора комментария
   * @return author
   */
  
  @Schema(name = "author", description = "id автора комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("author")
  public @Nullable Integer getAuthor() {
    return author;
  }

  public void setAuthor(@Nullable Integer author) {
    this.author = author;
  }

  public Comment authorImage(@Nullable String authorImage) {
    this.authorImage = authorImage;
    return this;
  }

  /**
   * ссылка на аватар автора комментария
   * @return authorImage
   */
  
  @Schema(name = "authorImage", description = "ссылка на аватар автора комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authorImage")
  public @Nullable String getAuthorImage() {
    return authorImage;
  }

  public void setAuthorImage(@Nullable String authorImage) {
    this.authorImage = authorImage;
  }

  public Comment authorFirstName(@Nullable String authorFirstName) {
    this.authorFirstName = authorFirstName;
    return this;
  }

  /**
   * имя создателя комментария
   * @return authorFirstName
   */
  
  @Schema(name = "authorFirstName", description = "имя создателя комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authorFirstName")
  public @Nullable String getAuthorFirstName() {
    return authorFirstName;
  }

  public void setAuthorFirstName(@Nullable String authorFirstName) {
    this.authorFirstName = authorFirstName;
  }

  public Comment createdAt(@Nullable Long createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
   * @return createdAt
   */
  
  @Schema(name = "createdAt", description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable Long createdAt) {
    this.createdAt = createdAt;
  }

  public Comment pk(@Nullable Integer pk) {
    this.pk = pk;
    return this;
  }

  /**
   * id комментария
   * @return pk
   */
  
  @Schema(name = "pk", description = "id комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pk")
  public @Nullable Integer getPk() {
    return pk;
  }

  public void setPk(@Nullable Integer pk) {
    this.pk = pk;
  }

  public Comment text(@Nullable String text) {
    this.text = text;
    return this;
  }

  /**
   * текст комментария
   * @return text
   */
  
  @Schema(name = "text", description = "текст комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public @Nullable String getText() {
    return text;
  }

  public void setText(@Nullable String text) {
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

