package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Polygon.
 */
@Schema(description = "Polygon.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")

@JsonTypeName("Polygon")
public class Polygon extends GADShape implements GeographicArea {
  @JsonProperty("pointList")
  private PointList pointList = null;

  public Polygon pointList(PointList pointList) {
    this.pointList = pointList;
    return this;
  }

  /**
   * Get pointList
   * @return pointList
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public PointList getPointList() {
    return pointList;
  }

  public void setPointList(PointList pointList) {
    this.pointList = pointList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Polygon polygon = (Polygon) o;
    return Objects.equals(this.pointList, polygon.pointList) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pointList, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Polygon {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    pointList: ").append(toIndentedString(pointList)).append("\n");
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
