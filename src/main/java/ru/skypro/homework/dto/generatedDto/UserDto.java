package ru.skypro.homework.dto.generatedDto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import ru.skypro.homework.dto.Role;

/**
 * User
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-07T11:59:23.949615055Z[GMT]")


public class UserDto {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("role")
  private Role role = null;

  @JsonProperty("image")
  private String image = null;

  public UserDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * id пользователя
   * @return id
   **/
  @Schema(description = "id пользователя")
  
    public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public UserDto email(String email) {
    this.email = email;
    return this;
  }

  /**
   * логин пользователя
   * @return email
   **/
  @Schema(description = "логин пользователя")
  
    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * имя пользователя
   * @return firstName
   **/
  @Schema(description = "имя пользователя")
  
    public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public UserDto lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * фамилия пользователя
   * @return lastName
   **/
  @Schema(description = "фамилия пользователя")
  
    public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UserDto phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * телефон пользователя
   * @return phone
   **/
  @Schema(description = "телефон пользователя")
  
    public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public UserDto role(Role role) {
    this.role = role;
    return this;
  }

  /**
   * роль пользователя
   * @return role
   **/
  @Schema(description = "роль пользователя")
  
    public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public UserDto image(String image) {
    this.image = image;
    return this;
  }

  /**
   * ссылка на аватар пользователя
   * @return image
   **/
  @Schema(description = "ссылка на аватар пользователя")
  
    public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDto user = (UserDto) o;
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
