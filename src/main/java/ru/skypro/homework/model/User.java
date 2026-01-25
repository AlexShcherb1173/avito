package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * User
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class User {

  private @Nullable Integer id;

  private @Nullable String email;

  private @Nullable String firstName;

  private @Nullable String lastName;

  private @Nullable String phone;

  /**
   * роль пользователя
   */
  public enum RoleEnum {
    USER("USER"),
    
    ADMIN("ADMIN");

    private final String value;

    RoleEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RoleEnum role;

  private @Nullable String image;

  public User id(@Nullable Integer id) {
    this.id = id;
    return this;
  }

  /**
   * id пользователя
   * @return id
   */
  
  @Schema(name = "id", description = "id пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Integer getId() {
    return id;
  }

  public void setId(@Nullable Integer id) {
    this.id = id;
  }

  public User email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * логин пользователя
   * @return email
   */
  
  @Schema(name = "email", description = "логин пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public User firstName(@Nullable String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * имя пользователя
   * @return firstName
   */
  
  @Schema(name = "firstName", description = "имя пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("firstName")
  public @Nullable String getFirstName() {
    return firstName;
  }

  public void setFirstName(@Nullable String firstName) {
    this.firstName = firstName;
  }

  public User lastName(@Nullable String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * фамилия пользователя
   * @return lastName
   */
  
  @Schema(name = "lastName", description = "фамилия пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastName")
  public @Nullable String getLastName() {
    return lastName;
  }

  public void setLastName(@Nullable String lastName) {
    this.lastName = lastName;
  }

  public User phone(@Nullable String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * телефон пользователя
   * @return phone
   */
  
  @Schema(name = "phone", description = "телефон пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phone")
  public @Nullable String getPhone() {
    return phone;
  }

  public void setPhone(@Nullable String phone) {
    this.phone = phone;
  }

  public User role(@Nullable RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * роль пользователя
   * @return role
   */
  
  @Schema(name = "role", description = "роль пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable RoleEnum getRole() {
    return role;
  }

  public void setRole(@Nullable RoleEnum role) {
    this.role = role;
  }

  public User image(@Nullable String image) {
    this.image = image;
    return this;
  }

  /**
   * ссылка на аватар пользователя
   * @return image
   */
  
  @Schema(name = "image", description = "ссылка на аватар пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("image")
  public @Nullable String getImage() {
    return image;
  }

  public void setImage(@Nullable String image) {
    this.image = image;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.firstName, user.firstName) &&
        Objects.equals(this.lastName, user.lastName) &&
        Objects.equals(this.phone, user.phone) &&
        Objects.equals(this.role, user.role) &&
        Objects.equals(this.image, user.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, firstName, lastName, phone, role, image);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
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

