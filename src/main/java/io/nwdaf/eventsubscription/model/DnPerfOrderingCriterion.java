package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are:   - AVERAGE_TRAFFIC_RATE: Indicates the average traffic rate.   - MAXIMUM_TRAFFIC_RATE: Indicates the maximum traffic rate.   - AVERAGE_PACKET_DELAY: Indicates the average packet delay.   - MAXIMUM_PACKET_DELAY: Indicates the maximum packet delay.   - AVERAGE_PACKET_LOSS_RATE: Indicates the average packet loss rate. 
*/
@Schema(description = "Ordering criterion for the list of DN performance analytics.")
@Validated
public class DnPerfOrderingCriterion {
  public enum DnPerfOrderingCriterionEnum {
    AVERAGE_TRAFFIC_RATE("AVERAGE_TRAFFIC_RATE"),
    MAXIMUM_TRAFFIC_RATE("MAXIMUM_TRAFFIC_RATE"),
    AVERAGE_PACKET_DELAY("AVERAGE_PACKET_DELAY"),
    MAXIMUM_PACKET_DELAY("MAXIMUM_PACKET_DELAY"),
    AVERAGE_PACKET_LOSS_RATE("AVERAGE_PACKET_LOSS_RATE");
    private String value;

    DnPerfOrderingCriterionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DnPerfOrderingCriterionEnum fromValue(String text) {
      for (DnPerfOrderingCriterionEnum b : DnPerfOrderingCriterionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("dnPerfOrderCriter")
  private DnPerfOrderingCriterionEnum dnPerfOrderCriter = null;

  public DnPerfOrderingCriterion dnPerfOrderCriter(DnPerfOrderingCriterionEnum dnPerfOrderCriter){
    this.dnPerfOrderCriter = dnPerfOrderCriter;
    return this;
  }

  /** Get dnPerfOrderCriter
  * @return dnPerfOrderCriter
  **/
  @Schema(description="dnPerfOrderCriter")

  public DnPerfOrderingCriterionEnum getDnPerfOrderCriter(){
    return dnPerfOrderCriter;
  }

  public void setDnPerfOrderCriter(DnPerfOrderingCriterionEnum dnPerfOrderCriter){
    this.dnPerfOrderCriter = dnPerfOrderCriter;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DnPerfOrderingCriterion dnPerfOrderingCriterionObject = (DnPerfOrderingCriterion) o;
    return Objects.equals(this.dnPerfOrderCriter, dnPerfOrderingCriterionObject.dnPerfOrderCriter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dnPerfOrderCriter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DnPerfOrderingCriterion {\n");
    
    sb.append("    dnPerfOrderCriter: ").append(toIndentedString(dnPerfOrderCriter)).append("\n");
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
