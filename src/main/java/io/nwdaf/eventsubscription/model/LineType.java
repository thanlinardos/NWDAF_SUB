package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - DSL: Identifies a DSL line - PON: Identifies a PON line 
*/
@Schema(description="The type of the wireline (DSL or PON).")
@Validated
public class LineType {
  public enum LineTypeEnum {
    DSL("DSL"),
    PON("PON");
    private String value;

    LineTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static LineTypeEnum fromValue(String text) {
      for (LineTypeEnum b : LineTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("lType")
  private LineTypeEnum lType = null;

  public LineType lType(LineTypeEnum lType){
    this.lType = lType;
    return this;
  }

  /** Get lType
  * @return lType
  **/
  @Schema(description="lType")

  public LineTypeEnum getLType(){
    return lType;
  }

  public void setLType(LineTypeEnum lType){
    this.lType = lType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LineType lineTypeObject = (LineType) o;
    return Objects.equals(this.lType, lineTypeObject.lType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LineType {\n");
    
    sb.append("    lType: ").append(toIndentedString(lType)).append("\n");
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
