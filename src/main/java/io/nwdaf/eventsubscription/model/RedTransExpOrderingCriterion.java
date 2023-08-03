package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - TIME_SLOT_START: Indicates the order of time slot start. - RED_TRANS_EXP: Indicates the order of Redundant Transmission Experience. 
*/
@Schema(description = "Ordering criterion for the list of Redundant Transmission Experience.")
@Validated
public class RedTransExpOrderingCriterion {
  public enum RedTransExpOrderingCriterionEnum {
    TIME_SLOT_START("TIME_SLOT_START"),
    RED_TRANS_EXP("RED_TRANS_EXP");
    private String value;

    RedTransExpOrderingCriterionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RedTransExpOrderingCriterionEnum fromValue(String text) {
      for (RedTransExpOrderingCriterionEnum b : RedTransExpOrderingCriterionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("redTOrderCriter")
  private RedTransExpOrderingCriterionEnum redTOrderCriter = null;

  public RedTransExpOrderingCriterion redTOrderCriter(RedTransExpOrderingCriterionEnum redTOrderCriter){
    this.redTOrderCriter = redTOrderCriter;
    return this;
  }

  /** Get redTOrderCriter
  * @return redTOrderCriter
  **/
  @Schema(description="redTOrderCriter")

  public RedTransExpOrderingCriterionEnum getRedTOrderCriter(){
    return redTOrderCriter;
  }

  public void setRedTOrderCriter(RedTransExpOrderingCriterionEnum redTOrderCriter){
    this.redTOrderCriter = redTOrderCriter;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RedTransExpOrderingCriterion redTransExpOrderingCriterionObject = (RedTransExpOrderingCriterion) o;
    return Objects.equals(this.redTOrderCriter, redTransExpOrderingCriterionObject.redTOrderCriter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(redTOrderCriter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RedTransExpOrderingCriterion {\n");
    
    sb.append("    redTOrderCriter: ").append(toIndentedString(redTOrderCriter)).append("\n");
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
