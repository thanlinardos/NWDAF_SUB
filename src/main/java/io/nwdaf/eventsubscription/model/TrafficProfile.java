package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - SINGLE_TRANS_UL: Uplink single packet transmission. - SINGLE_TRANS_DL: Downlink single packet transmission. - DUAL_TRANS_UL_FIRST: Dual packet transmission, firstly uplink packet transmission   with subsequent downlink packet transmission. - DUAL_TRANS_DL_FIRST: Dual packet transmission, firstly downlink packet transmission   with subsequent uplink packet transmission.  
*/
@Schema(description = "Represents the traffic profile type.")
@Validated
public class TrafficProfile {
  public enum TrafficProfileEnum {
    SINGLE_TRANS_UL("SINGLE_TRANS_UL"),
    SINGLE_TRANS_DL("SINGLE_TRANS_DL"),
    DUAL_TRANS_UL_FIRST("DUAL_TRANS_UL_FIRST"),
    DUAL_TRANS_DL_FIRST("DUAL_TRANS_DL_FIRST"),
    MULTI_TRANS("MULTI_TRANS");
    private String value;

    TrafficProfileEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TrafficProfileEnum fromValue(String text) {
      for (TrafficProfileEnum b : TrafficProfileEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("trafficProfile")
  private TrafficProfileEnum trafficProfile = null;

  public TrafficProfile trafficProfile(TrafficProfileEnum trafficProfile){
    this.trafficProfile = trafficProfile;
    return this;
  }

  /** Get trafficProfile
  * @return trafficProfile
  **/
  @Schema(description="trafficProfile")

  public TrafficProfileEnum getTrafficProfile(){
    return trafficProfile;
  }

  public void setTrafficProfile(TrafficProfileEnum trafficProfile){
    this.trafficProfile = trafficProfile;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrafficProfile trafficProfileObject = (TrafficProfile) o;
    return Objects.equals(this.trafficProfile, trafficProfileObject.trafficProfile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trafficProfile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrafficProfile {\n");
    
    sb.append("    trafficProfile: ").append(toIndentedString(trafficProfile)).append("\n");
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
