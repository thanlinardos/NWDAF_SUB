package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - SLICE_LOAD_LEVEL: Indicates that the event subscribed is load level information of Network Slice   - NETWORK_PERFORMANCE: Indicates that the event subscribed is network performance information.   - NF_LOAD: Indicates that the event subscribed is load level and status of one or several Network Functions.   - SERVICE_EXPERIENCE: Indicates that the event subscribed is service experience.   - UE_MOBILITY: Indicates that the event subscribed is UE mobility information.   - UE_COMMUNICATION: Indicates that the event subscribed is UE communication information.   - QOS_SUSTAINABILITY: Indicates that the event subscribed is QoS sustainability.   - ABNORMAL_BEHAVIOUR: Indicates that the event subscribed is abnormal behaviour.   - USER_DATA_CONGESTION: Indicates that the event subscribed is user data congestion information.   - NSI_LOAD_LEVEL: Indicates that the event subscribed is load level information of Network Slice and the optionally associated Network Slice Instance   - DN_PERFORMANCE: Indicates that the event subscribed is DN performance information.   - DISPERSION: Indicates that the event subscribed is dispersion information.   - RED_TRANS_EXP: Indicates that the event subscribed is redundant transmission experience.   - WLAN_PERFORMANCE: Indicates that the event subscribed is WLAN performance.   - SM_CONGESTION: Indicates the Session Management Congestion Control Experience information for specific DNN and/or S-NSSAI. 
*/
@Schema(description = "Represents the type of event.")
@Validated
public class NwdafEvent {
  public enum NwdafEventEnum {
    SLICE_LOAD_LEVEL("SLICE_LOAD_LEVEL"),
    NETWORK_PERFORMANCE("NETWORK_PERFORMANCE"),
    NF_LOAD("NF_LOAD"),
    QOS_SUSTAINABILITY("QOS_SUSTAINABILITY"),
    SERVICE_EXPERIENCE("SERVICE_EXPERIENCE"),
    UE_MOBILITY("UE_MOBILITY"),
    UE_COMM("UE_COMM"),
    ABNORMAL_BEHAVIOUR("ABNORMAL_BEHAVIOUR"),
    USER_DATA_CONGESTION("USER_DATA_CONGESTION"),
    NSI_LOAD_LEVEL("NSI_LOAD_LEVEL"),
    DISPERSION("DISPERSION"),
    RED_TRANS_EXP("RED_TRANS_EXP"),
    WLAN_PERFORMANCE("WLAN_PERFORMANCE"),
    DN_PERFORMANCE("DN_PERFORMANCE"),
    SM_CONGESTION("SM_CONGESTION");
    private String value;

    NwdafEventEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NwdafEventEnum fromValue(String text) {
      for (NwdafEventEnum b : NwdafEventEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("type")
  private NwdafEventEnum event = null;

  public NwdafEvent event(NwdafEventEnum event){
    this.event = event;
    return this;
  }

  /** Get event
  * @return event
  **/
  @Schema(description="event")

  public NwdafEventEnum getEvent(){
    return event;
  }

  public void setEvent(NwdafEventEnum event){
    this.event = event;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NwdafEvent nwdafEventObject = (NwdafEvent) o;
    return Objects.equals(this.event, nwdafEventObject.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NwdafEvent {\n");
    
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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
