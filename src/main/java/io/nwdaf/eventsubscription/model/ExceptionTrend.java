package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - UP: Up trend of the exception level. - DOWN: Down trend of the exception level. - UNKNOW: Unknown trend of the exception level. - STABLE: Stable trend of the exception level. 
*/
@Schema(description = "Describes the Exception Trend.")
@Validated
public class ExceptionTrend {
  public enum ExceptionTrendEnum {
    UP("UP"),
    DOWN("DOWN"),
    UNKNOW("UNKNOW"),
    STABLE("STABLE");
    private String value;

    ExceptionTrendEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ExceptionTrendEnum fromValue(String text) {
      for (ExceptionTrendEnum b : ExceptionTrendEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("excepTrend")
  private ExceptionTrendEnum excepTrend = null;

  public ExceptionTrend excepTrend(ExceptionTrendEnum excepTrend){
    this.excepTrend = excepTrend;
    return this;
  }

  /** Get excepTrend
  * @return excepTrend
  **/
  @Schema(description="excepTrend")

  public ExceptionTrendEnum getExcepTrend(){
    return excepTrend;
  }

  public void setExcepTrend(ExceptionTrendEnum excepTrend){
    this.excepTrend = excepTrend;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExceptionTrend exceptionTrendObject = (ExceptionTrend) o;
    return Objects.equals(this.excepTrend, exceptionTrendObject.excepTrend);
  }

  @Override
  public int hashCode() {
    return Objects.hash(excepTrend);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExceptionTrend {\n");
    
    sb.append("    excepTrend: ").append(toIndentedString(excepTrend)).append("\n");
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
