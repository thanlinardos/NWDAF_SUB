package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* The enumeration QosResourceType indicates whether a QoS Flow is non-GBR, delay critical GBR, or non-delay critical GBR (see clauses 5.7.3.4 and 5.7.3.5 of 3GPP TS 23.501). It shall comply with the provisions defined in table 5.5.3.6-1.  
*/
@Schema(description = "The enumeration QosResourceType indicates whether a QoS Flow is non-GBR, delay critical GBR, or non-delay critical GBR.")
@Validated
public class QosResourceType {
  public enum QosResourceTypeEnum {
    NON_GBR("NON_GBR"),
    NON_CRITICAL_GBR("NON_CRITICAL_GBR"),
    CRITICAL_GBR("CRITICAL_GBR");
    private String value;

    QosResourceTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static QosResourceTypeEnum fromValue(String text) {
      for (QosResourceTypeEnum b : QosResourceTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("resType")
  private QosResourceTypeEnum resType = null;

  public QosResourceType resType(QosResourceTypeEnum resType){
    this.resType = resType;
    return this;
  }

  /** Get resType
  * @return resType
  **/
  @Schema(description="resType")

  public QosResourceTypeEnum getResType(){
    return resType;
  }

  public void setResType(QosResourceTypeEnum resType){
    this.resType = resType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QosResourceType qosResourceTypeObject = (QosResourceType) o;
    return Objects.equals(this.resType, qosResourceTypeObject.resType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QosResourceType {\n");
    
    sb.append("    resType: ").append(toIndentedString(resType)).append("\n");
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
