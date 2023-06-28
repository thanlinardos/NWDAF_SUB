package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - STATIONARY: Identifies the UE is stationary - MOBILE: Identifies the UE is mobile 
*/
@Schema(description = "Indicates whether the UE is stationary or mobile.")
@Validated
public class StationaryIndication {
  public enum StationaryIndicationEnum {
    STATIONARY("STATIONARY"),
    MOBILE("MOBILE");
    private String value;

    StationaryIndicationEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StationaryIndicationEnum fromValue(String text) {
      for (StationaryIndicationEnum b : StationaryIndicationEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("stationaryIndication")
  private StationaryIndicationEnum stationaryIndication = null;

  public StationaryIndication stationaryIndication(StationaryIndicationEnum stationaryIndication){
    this.stationaryIndication = stationaryIndication;
    return this;
  }

  /** Get stationaryIndication
  * @return stationaryIndication
  **/
  @Schema(description="stationaryIndication")

  public StationaryIndicationEnum getStationaryIndication(){
    return stationaryIndication;
  }

  public void setStationaryIndication(StationaryIndicationEnum stationaryIndication){
    this.stationaryIndication = stationaryIndication;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StationaryIndication stationaryIndicationObject = (StationaryIndication) o;
    return Objects.equals(this.stationaryIndication, stationaryIndicationObject.stationaryIndication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stationaryIndication);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StationaryIndication {\n");
    
    sb.append("    stationaryIndication: ").append(toIndentedString(stationaryIndication)).append("\n");
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
