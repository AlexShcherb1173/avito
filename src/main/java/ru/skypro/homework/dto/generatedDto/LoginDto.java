package ru.skypro.homework.dto.generatedDto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * Login
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-07T11:59:23.949615055Z[GMT]")


public class LoginDto {
  @JsonProperty("password")
  private String password = null;

  @JsonProperty("username")
  private String username = null;

  public LoginDto password(String password) {
    this.password = password;
    return this;
  }

  /**
   * пароль
   * @return password
   **/
  @Schema(description = "пароль")
  
  @Size(min=8,max=16)   public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public LoginDto username(String username) {
    this.username = username;
    return this;
  }

  /**
   * логин
   * @return username
   **/
  @Schema(description = "логин")
  
  @Size(min=4,max=32)   public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginDto login = (LoginDto) o;
    return Objects.equals(this.password, login.password) &&
        Objects.equals(this.username, login.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, username);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Login {\n");
    
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
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
