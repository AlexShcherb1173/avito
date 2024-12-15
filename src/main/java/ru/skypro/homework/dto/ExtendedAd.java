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
public class ExtendedAd {

  private Integer pk;/**
   * id объявления
   */

  private String authorFirstName;

  private String authorLastName;

  private String description;

  private String email;

  private String image;/**
   * ссылка на картинку объявления
   */

  private String phone;

  private Integer price;

  private String title;

  public ExtendedAd(Integer pk, String authorFirstName, String authorLastName, String description,
                    String email, String image, String phone, Integer price, String title) {
    this.pk = pk;
    this.authorFirstName = authorFirstName;
    this.authorLastName = authorLastName;
    this.description = description;
    this.email = email;
    this.image = image;
    this.phone = phone;
    this.price = price;
    this.title = title;
  }

  public Integer getPk() {
    return pk;
  }

  public String getAuthorFirstName() {
    return authorFirstName;
  }

  public void setAuthorFirstName(String authorFirstName) {
    this.authorFirstName = authorFirstName;
  }

  public String getAuthorLastName() {
    return authorLastName;
  }

  public void setAuthorLastName(String authorLastName) {
    this.authorLastName = authorLastName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtendedAd extendedAd = (ExtendedAd) o;
    return Objects.equals(this.pk, extendedAd.pk) &&
        Objects.equals(this.authorFirstName, extendedAd.authorFirstName) &&
        Objects.equals(this.authorLastName, extendedAd.authorLastName) &&
        Objects.equals(this.description, extendedAd.description) &&
        Objects.equals(this.email, extendedAd.email) &&
        Objects.equals(this.image, extendedAd.image) &&
        Objects.equals(this.phone, extendedAd.phone) &&
        Objects.equals(this.price, extendedAd.price) &&
        Objects.equals(this.title, extendedAd.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pk, authorFirstName, authorLastName, description, email, image, phone, price, title);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtendedAd {\n");
    sb.append("    pk: ").append(toIndentedString(pk)).append("\n");
    sb.append("    authorFirstName: ").append(toIndentedString(authorFirstName)).append("\n");
    sb.append("    authorLastName: ").append(toIndentedString(authorLastName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
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

