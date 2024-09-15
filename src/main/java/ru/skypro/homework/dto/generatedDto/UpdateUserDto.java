package ru.skypro.homework.dto.generatedDto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * UpdateUser
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-07T11:59:23.949615055Z[GMT]")


public class UpdateUserDto {
  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("phone")
  private String phone = null;

  public UpdateUserDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * имя пользователя
   * @return firstName
   **/
  @Schema(description = "имя пользователя")
  
  @Size(min=3,max=10)   public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public UpdateUserDto lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * фамилия пользователя
   * @return lastName
   **/
  @Schema(description = "фамилия пользователя")
  
  @Size(min=3,max=10)   public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UpdateUserDto phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * телефон пользователя
   * @return phone
   **/
  @Schema(description = "телефон пользователя")
  
  @Pattern(regexp="\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")   public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateUserDto updateUser = (UpdateUserDto) o;
    return Objects.equals(this.firstName, updateUser.firstName) &&
        Objects.equals(this.lastName, updateUser.lastName) &&
        Objects.equals(this.phone, updateUser.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, phone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateUser {\n");
    
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
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
