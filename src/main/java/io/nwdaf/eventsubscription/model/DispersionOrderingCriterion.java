package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - TIME_SLOT_START: Indicates the order of time slot start. - DISPERSION: Indicates the order of data/transaction dispersion. - CLASSIFICATION: Indicates the order of data/transaction classification. - RANKING: Indicates the order of data/transaction ranking. - PERCENTILE_RANKING: Indicates the order of data/transaction percentile ranking. 
*/
@Schema(description = "Ordering criterion for the list of Dispersion.")
@Validated
public class DispersionOrderingCriterion {
  public enum DispersionOrderingCriterionEnum {
    TIME_SLOT_START("TIME_SLOT_START"),
    DISPERSION("DISPERSION"),
    CLASSIFICATION("CLASSIFICATION"),
    RANKING("RANKING"),
    PERCENTILE_RANKING("PERCENTILE_RANKING");
    private String value;

    DispersionOrderingCriterionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DispersionOrderingCriterionEnum fromValue(String text) {
      for (DispersionOrderingCriterionEnum b : DispersionOrderingCriterionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("dispOrderCriter")
  private DispersionOrderingCriterionEnum dispOrderCriter = null;

  public DispersionOrderingCriterion dispOrderCriter(DispersionOrderingCriterionEnum dispOrderCriter){
    this.dispOrderCriter = dispOrderCriter;
    return this;
  }

  /** Get dispOrderCriter
  * @return dispOrderCriter
  **/
  @Schema(description="dispOrderCriter")

  public DispersionOrderingCriterionEnum getDispOrderCriter(){
    return dispOrderCriter;
  }

  public void setDispOrderCriter(DispersionOrderingCriterionEnum dispOrderCriter){
    this.dispOrderCriter = dispOrderCriter;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DispersionOrderingCriterion dispersionOrderingCriterionObject = (DispersionOrderingCriterion) o;
    return Objects.equals(this.dispOrderCriter, dispersionOrderingCriterionObject.dispOrderCriter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dispOrderCriter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DispersionOrderingCriterion {\n");
    
    sb.append("    dispOrderCriter: ").append(toIndentedString(dispOrderCriter)).append("\n");
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
