package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Indicates the radio access used.
*/
@Schema(description = "Indicates the radio access used.")
@Validated
public class RatType {
  public enum RatTypeEnum {
    NR("NR"),
    EUTRA("EUTRA"),
    WLAN("WLAN"),
    VIRTUAL("VIRTUAL"),
    NBIOT("NBIOT"),
    WIRELINE("WIRELINE"),
    WIRELINE_CABLE("WIRELINE_CABLE"),
    WIRELINE_BBF("WIRELINE_BBF"),
    LTE_M("LTE-M"),
    NR_U("NR_U"),
    EUTRA_U("EUTRA_U"),
    TRUSTED_N3GA("TRUSTED_N3GA"),
    TRUSTED_WLAN("TRUSTED_WLAN"),
    UTRA("UTRA"),
    GERA("GERA"),
    NR_LEO("NR_LEO"),
    NR_MEO("NR_MEO"),
    NR_GEO("NR_GEO"),
    NR_OTHER_SAT("NR_OTHER_SAT"),
    NR_REDCAP("NR_REDCAP");
    private String value;

    RatTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RatTypeEnum fromValue(String text) {
      for (RatTypeEnum b : RatTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("ratType")
  private RatTypeEnum ratType = null;

  public RatType ratType(RatTypeEnum ratType){
    this.ratType = ratType;
    return this;
  }

  /** Get ratType
  * @return ratType
  **/
  @Schema(description="ratType")

  public RatTypeEnum getRatType(){
    return ratType;
  }

  public void setRatType(RatTypeEnum ratType){
    this.ratType = ratType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatType ratTypeObject = (RatType) o;
    return Objects.equals(this.ratType, ratTypeObject.ratType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ratType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatType {\n");
    
    sb.append("    ratType: ").append(toIndentedString(ratType)).append("\n");
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
