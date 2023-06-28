package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are - DOWNLINK: The corresponding filter applies for traffic to the UE. - UPLINK: The corresponding filter applies for traffic from the UE. - BIDIRECTIONAL: The corresponding filter applies for traffic both to and from the UE. - UNSPECIFIED: The corresponding filter applies for traffic to the UE (downlink), but has no specific direction declared. The service data flow detection shall apply the filter for uplink traffic as if the filter was bidirectional. The PCF shall not use the value UNSPECIFIED in filters created by the network in NW-initiated procedures. The PCF shall only include the value UNSPECIFIED in filters in UE-initiated procedures if the same value is received from the SMF. 
*/
@Schema(description = "Contains the packet filter direction. Only the 'DOWNLINK' or 'UPLINK' value is applicable.")
@Validated
public class FlowDirection {
  public enum FlowDirectionEnum {
    DOWNLINK("DOWNLINK"),
    UPLINK("UPLINK"),
    BIDIRECTIONAL("BIDIRECTIONAL"),
    UNSPECIFIED("UNSPECIFIED");
    private String value;

    FlowDirectionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static FlowDirectionEnum fromValue(String text) {
      for (FlowDirectionEnum b : FlowDirectionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("fDir")
  private FlowDirectionEnum fDir = null;

  public FlowDirection fDir(FlowDirectionEnum fDir){
    this.fDir = fDir;
    return this;
  }

  /** Get fDir
  * @return fDir
  **/
  @Schema(description="fDir")

  public FlowDirectionEnum getFDir(){
    return fDir;
  }

  public void setFDir(FlowDirectionEnum fDir){
    this.fDir = fDir;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlowDirection flowDirectionObject = (FlowDirection) o;
    return Objects.equals(this.fDir, flowDirectionObject.fDir);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fDir);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlowDirection {\n");
    
    sb.append("    fDir: ").append(toIndentedString(fDir)).append("\n");
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
