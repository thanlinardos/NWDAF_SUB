package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - USER_PLANE: The congestion analytics type is User Plane.  - CONTROL_PLANE: The congestion analytics type is Control Plane. - USER_AND_CONTROL_PLANE: The congestion analytics type is User Plane and Control Plane. 
*/
@Schema(description="Identification congestion analytics type.")
@Validated
public class CongestionType {
  public enum CongestionTypeEnum {
    USER_PLANE("USER_PLANE"),
    CONTROL_PLANE("CONTROL_PLANE"),
    USER_AND_CONTROL_PLANE("USER_AND_CONTROL_PLANE");
    private String value;

    CongestionTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CongestionTypeEnum fromValue(String text) {
      for (CongestionTypeEnum b : CongestionTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("congType")
  private CongestionTypeEnum congType = null;

  public CongestionType congType(CongestionTypeEnum congType){
    this.congType = congType;
    return this;
  }

  /** Get congType
  * @return congType
  **/
  @Schema(description="congType")

  public CongestionTypeEnum getCongType(){
    return congType;
  }

  public void setCongType(CongestionTypeEnum congType){
    this.congType = congType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CongestionType congestionTypeObject = (CongestionType) o;
    return Objects.equals(this.congType, congestionTypeObject.congType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(congType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CongestionType {\n");
    
    sb.append("    congType: ").append(toIndentedString(congType)).append("\n");
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
