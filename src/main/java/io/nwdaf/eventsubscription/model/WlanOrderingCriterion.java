package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - TIME_SLOT_START: Indicates the order of time slot start. - NUMBER_OF_UES: Indicates the order of number of UEs. - RSSI: Indicates the order of RSSI. - RTT: Indicates the order of RTT. - TRAFFIC_INFO: Indicates the order of Traffic information. 
*/
@Schema(description = "Ordering criterion for the list of WLAN performance information.")
@Validated
public class WlanOrderingCriterion {
  public enum WlanOrderingCriterionEnum {
    TIME_SLOT_START("TIME_SLOT_START"),
    NUMBER_OF_UES("NUMBER_OF_UES"),
    RSSI("RSSI"),
    RTT("RTT"),
    TRAFFIC_INFO("TRAFFIC_INFO");
    private String value;

    WlanOrderingCriterionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static WlanOrderingCriterionEnum fromValue(String text) {
      for (WlanOrderingCriterionEnum b : WlanOrderingCriterionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("wlanOrderCriter")
  private WlanOrderingCriterionEnum wlanOrderCriter = null;

  public WlanOrderingCriterion wlanOrderCriter(WlanOrderingCriterionEnum wlanOrderCriter){
    this.wlanOrderCriter = wlanOrderCriter;
    return this;
  }

  /** Get wlanOrderCriter
  * @return wlanOrderCriter
  **/
  @Schema(description="wlanOrderCriter")

  public WlanOrderingCriterionEnum getWlanOrderCriter(){
    return wlanOrderCriter;
  }

  public void setWlanOrderCriter(WlanOrderingCriterionEnum wlanOrderCriter){
    this.wlanOrderCriter = wlanOrderCriter;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WlanOrderingCriterion wlanOrderCriterObject = (WlanOrderingCriterion) o;
    return Objects.equals(this.wlanOrderCriter, wlanOrderCriterObject.wlanOrderCriter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wlanOrderCriter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WlanOrderingCriterion {\n");
    
    sb.append("    wlanOrderCriter: ").append(toIndentedString(wlanOrderCriter)).append("\n");
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
