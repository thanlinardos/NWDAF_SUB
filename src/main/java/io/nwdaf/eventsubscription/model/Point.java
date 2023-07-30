package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Ellipsoid Point.
 */
@Schema(description = "Ellipsoid Point.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")

@JsonTypeName("Point")
public class Point extends GADShape implements GeographicArea {
  @JsonProperty("point")
  private GeographicalCoordinates point = null;

  public Point point(GeographicalCoordinates point) {
    this.point = point;
    return this;
  }

  /**
   * Get point
   * @return point
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public GeographicalCoordinates getPoint() {
    return point;
  }

  public void setPoint(GeographicalCoordinates point) {
    this.point = point;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Point point = (Point) o;
    return Objects.equals(this.point, point.point) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(point, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Point {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    point: ").append(toIndentedString(point)).append("\n");
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

  @Override
  public String getType() {
    return getClass().getSimpleName();
  }
}
