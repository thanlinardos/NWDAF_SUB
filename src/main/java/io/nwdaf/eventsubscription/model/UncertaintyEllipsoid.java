package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Ellipsoid with uncertainty
 */
@Schema(description = "Ellipsoid with uncertainty")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class UncertaintyEllipsoid   {
  @JsonProperty("semiMajor")
  private Float semiMajor = null;

  @JsonProperty("semiMinor")
  private Float semiMinor = null;

  @JsonProperty("vertical")
  private Float vertical = null;

  @JsonProperty("orientationMajor")
  private Integer orientationMajor = null;

  public UncertaintyEllipsoid semiMajor(Float semiMajor) {
    this.semiMajor = semiMajor;
    return this;
  }

  /**
   * Indicates value of uncertainty.
   * minimum: 0
   * @return semiMajor
   **/
  @Schema(required = true, description = "Indicates value of uncertainty.")
      @NotNull

  @DecimalMin("0")  public Float getSemiMajor() {
    return semiMajor;
  }

  public void setSemiMajor(Float semiMajor) {
    this.semiMajor = semiMajor;
  }

  public UncertaintyEllipsoid semiMinor(Float semiMinor) {
    this.semiMinor = semiMinor;
    return this;
  }

  /**
   * Indicates value of uncertainty.
   * minimum: 0
   * @return semiMinor
   **/
  @Schema(required = true, description = "Indicates value of uncertainty.")
      @NotNull

  @DecimalMin("0")  public Float getSemiMinor() {
    return semiMinor;
  }

  public void setSemiMinor(Float semiMinor) {
    this.semiMinor = semiMinor;
  }

  public UncertaintyEllipsoid vertical(Float vertical) {
    this.vertical = vertical;
    return this;
  }

  /**
   * Indicates value of uncertainty.
   * minimum: 0
   * @return vertical
   **/
  @Schema(required = true, description = "Indicates value of uncertainty.")
      @NotNull

  @DecimalMin("0")  public Float getVertical() {
    return vertical;
  }

  public void setVertical(Float vertical) {
    this.vertical = vertical;
  }

  public UncertaintyEllipsoid orientationMajor(Integer orientationMajor) {
    this.orientationMajor = orientationMajor;
    return this;
  }

  /**
   * Indicates value of orientation angle.
   * minimum: 0
   * maximum: 180
   * @return orientationMajor
   **/
  @Schema(required = true, description = "Indicates value of orientation angle.")
      @NotNull

  @Min(0) @Max(180)   public Integer getOrientationMajor() {
    return orientationMajor;
  }

  public void setOrientationMajor(Integer orientationMajor) {
    this.orientationMajor = orientationMajor;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UncertaintyEllipsoid uncertaintyEllipsoid = (UncertaintyEllipsoid) o;
    return Objects.equals(this.semiMajor, uncertaintyEllipsoid.semiMajor) &&
        Objects.equals(this.semiMinor, uncertaintyEllipsoid.semiMinor) &&
        Objects.equals(this.vertical, uncertaintyEllipsoid.vertical) &&
        Objects.equals(this.orientationMajor, uncertaintyEllipsoid.orientationMajor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(semiMajor, semiMinor, vertical, orientationMajor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UncertaintyEllipsoid {\n");
    
    sb.append("    semiMajor: ").append(toIndentedString(semiMajor)).append("\n");
    sb.append("    semiMinor: ").append(toIndentedString(semiMinor)).append("\n");
    sb.append("    vertical: ").append(toIndentedString(vertical)).append("\n");
    sb.append("    orientationMajor: ").append(toIndentedString(orientationMajor)).append("\n");
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
