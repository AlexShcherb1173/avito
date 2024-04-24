package ru.skypro.homework.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AdsBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-03-16T10:55:08.933404864Z[GMT]")


public class AdsBody   {
  @JsonProperty("properties")
  private CreateOrUpdateAd properties = null;

  @JsonProperty("image")
  private Resource image = null;

  public AdsBody properties(CreateOrUpdateAd properties) {
    this.properties = properties;
    return this;
  }

  /**
   * Get properties
   * @return properties
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public CreateOrUpdateAd getProperties() {
    return properties;
  }

  public void setProperties(CreateOrUpdateAd properties) {
    this.properties = properties;
  }

  public AdsBody image(Resource image) {
    this.image = image;
    return this;
  }

  /**
   * Get image
   * @return image
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public Resource getImage() {
    return image;
  }

  public void setImage(Resource image) {
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
    AdsBody adsBody = (AdsBody) o;
    return Objects.equals(this.properties, adsBody.properties) &&
        Objects.equals(this.image, adsBody.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(properties, image);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdsBody {\n");
    
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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
