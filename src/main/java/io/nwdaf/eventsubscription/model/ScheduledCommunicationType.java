package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: -DOWNLINK_ONLY: Downlink only -UPLINK_ONLY: Uplink only -BIDIRECTIONA: Bi-directional 
*/
@Schema(description = "Represents the type of scheduled communication.")
@Validated
public class ScheduledCommunicationType {
  public enum ScheduledCommunicationTypeEnum {
    DOWNLINK_ONLY("DOWNLINK_ONLY"),
    UPLINK_ONLY("UPLINK_ONLY"),
    BIDIRECTIONA("BIDIRECTIONA");
    private String value;

    ScheduledCommunicationTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ScheduledCommunicationTypeEnum fromValue(String text) {
      for (ScheduledCommunicationTypeEnum b : ScheduledCommunicationTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("scheduledCommunicationType")
  private ScheduledCommunicationTypeEnum scheduledCommunicationType = null;

  public ScheduledCommunicationType scheduledCommunicationType(ScheduledCommunicationTypeEnum scheduledCommunicationType){
    this.scheduledCommunicationType = scheduledCommunicationType;
    return this;
  }

  /** Get scheduledCommunicationType
  * @return scheduledCommunicationType
  **/
  @Schema(description="scheduledCommunicationType")

  public ScheduledCommunicationTypeEnum getAnaMeta(){
    return scheduledCommunicationType;
  }

  public void setAnaMeta(ScheduledCommunicationTypeEnum scheduledCommunicationType){
    this.scheduledCommunicationType = scheduledCommunicationType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduledCommunicationType scheduledCommunicationTypeObject = (ScheduledCommunicationType) o;
    return Objects.equals(this.scheduledCommunicationType, scheduledCommunicationTypeObject.scheduledCommunicationType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduledCommunicationType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduledCommunicationType {\n");
    
    sb.append("    scheduledCommunicationType: ").append(toIndentedString(scheduledCommunicationType)).append("\n");
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
