package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents a time window identified by a start time and a stop time.
 */
@Schema(description = "Represents a time window identified by a start time and a stop time.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class TimeWindow   {
  @JsonProperty("startTime")
  private OffsetDateTime startTime = null;

  @JsonProperty("stopTime")
  private OffsetDateTime stopTime = null;

  public TimeWindow startTime(OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * string with format \"date-time\" as defined in OpenAPI.
   * @return startTime
   **/
  @Schema(required = true, description = "string with format \"date-time\" as defined in OpenAPI.")
      @NotNull

    @Valid
    public OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public TimeWindow stopTime(OffsetDateTime stopTime) {
    this.stopTime = stopTime;
    return this;
  }

  /**
   * string with format \"date-time\" as defined in OpenAPI.
   * @return stopTime
   **/
  @Schema(required = true, description = "string with format \"date-time\" as defined in OpenAPI.")
      @NotNull

    @Valid
    public OffsetDateTime getStopTime() {
    return stopTime;
  }

  public void setStopTime(OffsetDateTime stopTime) {
    this.stopTime = stopTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeWindow timeWindow = (TimeWindow) o;
    return Objects.equals(this.startTime, timeWindow.startTime) &&
        Objects.equals(this.stopTime, timeWindow.stopTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTime, stopTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimeWindow {\n");
    
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    stopTime: ").append(toIndentedString(stopTime)).append("\n");
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
