package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - MINUTE: Time unit is per minute. - HOUR: Time unit is per hour. - DAY: Time unit is per day. 
*/
@Schema(description = "Represents the unit for the session active time.")
@Validated
public class TimeUnit {
  public enum TimeUnitEnum {
    MINUTE("MINUTE"),
    HOUR("HOUR"),
    DAY("DAY");
    private String value;

    TimeUnitEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TimeUnitEnum fromValue(String text) {
      for (TimeUnitEnum b : TimeUnitEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("relTimeUnit")
  private TimeUnitEnum relTimeUnit = null;

  public TimeUnit relTimeUnit(TimeUnitEnum relTimeUnit){
    this.relTimeUnit = relTimeUnit;
    return this;
  }

  /** Get relTimeUnit
  * @return relTimeUnit
  **/
  @Schema(description="relTimeUnit")

  public TimeUnitEnum getRelTimeUnit(){
    return relTimeUnit;
  }

  public void setRelTimeUnit(TimeUnitEnum relTimeUnit){
    this.relTimeUnit = relTimeUnit;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeUnit timeUnitObject = (TimeUnit) o;
    return Objects.equals(this.relTimeUnit, timeUnitObject.relTimeUnit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relTimeUnit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimeUnit {\n");
    
    sb.append("    relTimeUnit: ").append(toIndentedString(relTimeUnit)).append("\n");
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
