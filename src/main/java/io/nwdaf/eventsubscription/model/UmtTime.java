package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UmtTime
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class UmtTime   {
  @JsonProperty("timeOfDay")
  private String timeOfDay = null;

  @JsonProperty("dayOfWeek")
  private Integer dayOfWeek = null;

  public UmtTime timeOfDay(String timeOfDay) {
    this.timeOfDay = timeOfDay;
    return this;
  }

  /**
   * String with format partial-time or full-time as defined in clause 5.6 of IETF RFC 3339. Examples, 20:15:00, 20:15:00-08:00 (for 8 hours behind UTC).  
   * @return timeOfDay
   **/
  @Schema(required = true, description = "String with format partial-time or full-time as defined in clause 5.6 of IETF RFC 3339. Examples, 20:15:00, 20:15:00-08:00 (for 8 hours behind UTC).  ")
      @NotNull

    public String getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(String timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  public UmtTime dayOfWeek(Integer dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
    return this;
  }

  /**
   * integer between and including 1 and 7 denoting a weekday. 1 shall indicate Monday, and the subsequent weekdays  shall be indicated with the next higher numbers. 7 shall indicate Sunday. 
   * minimum: 1
   * maximum: 7
   * @return dayOfWeek
   **/
  @Schema(required = true, description = "integer between and including 1 and 7 denoting a weekday. 1 shall indicate Monday, and the subsequent weekdays  shall be indicated with the next higher numbers. 7 shall indicate Sunday. ")
      @NotNull

  @Min(1) @Max(7)   public Integer getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(Integer dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UmtTime umtTime = (UmtTime) o;
    return Objects.equals(this.timeOfDay, umtTime.timeOfDay) &&
        Objects.equals(this.dayOfWeek, umtTime.dayOfWeek);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeOfDay, dayOfWeek);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UmtTime {\n");
    
    sb.append("    timeOfDay: ").append(toIndentedString(timeOfDay)).append("\n");
    sb.append("    dayOfWeek: ").append(toIndentedString(dayOfWeek)).append("\n");
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
