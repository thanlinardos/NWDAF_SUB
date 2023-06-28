package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - MOBILITY: Mobility related abnormal behaviour analytics is expected by the consumer. - COMMUN: Communication related abnormal behaviour analytics is expected by the consumer. - MOBILITY_AND_COMMUN: Both mobility and communication related abnormal behaviour analytics is expected by the consumer. 
*/
@Schema(description = "Represents expected UE analytics type.")
@Validated
public class ExpectedAnalyticsType {
  public enum ExpectedAnalyticsTypeEnum {
    MOBILITY("MOBILITY"),
    COMMUN("COMMUN"),
    MOBILITY_AND_COMMUN("MOBILITY_AND_COMMUN");
    private String value;

    ExpectedAnalyticsTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ExpectedAnalyticsTypeEnum fromValue(String text) {
      for (ExpectedAnalyticsTypeEnum b : ExpectedAnalyticsTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("exptAnaType")
  private ExpectedAnalyticsTypeEnum exptAnaType = null;

  public ExpectedAnalyticsType exptAnaType(ExpectedAnalyticsTypeEnum exptAnaType){
    this.exptAnaType = exptAnaType;
    return this;
  }

  /** Get exptAnaType
  * @return exptAnaType
  **/
  @Schema(description="exptAnaType")

  public ExpectedAnalyticsTypeEnum getExptAnaType(){
    return exptAnaType;
  }

  public void setExptAnaType(ExpectedAnalyticsTypeEnum exptAnaType){
    this.exptAnaType = exptAnaType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpectedAnalyticsType expectedAnalyticsTypeObject = (ExpectedAnalyticsType) o;
    return Objects.equals(this.exptAnaType, expectedAnalyticsTypeObject.exptAnaType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(exptAnaType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpectedAnalyticsType {\n");
    
    sb.append("    exptAnaType: ").append(toIndentedString(exptAnaType)).append("\n");
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
