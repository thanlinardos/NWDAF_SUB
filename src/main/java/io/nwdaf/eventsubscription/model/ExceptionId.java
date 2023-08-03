package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - UNEXPECTED_UE_LOCATION: Unexpected UE location - UNEXPECTED_LONG_LIVE_FLOW: Unexpected long-live rate flows - UNEXPECTED_LARGE_RATE_FLOW: Unexpected large rate flows - UNEXPECTED_WAKEUP: Unexpected wakeup - SUSPICION_OF_DDOS_ATTACK: Suspicion of DDoS attack - WRONG_DESTINATION_ADDRESS: Wrong destination address - TOO_FREQUENT_SERVICE_ACCESS: Too frequent Service Access - UNEXPECTED_RADIO_LINK_FAILURES: Unexpected radio link failures - PING_PONG_ACROSS_CELLS: Ping-ponging across neighbouring cells 
*/
@Schema(description = "Describes the Exception Id.")
@Validated
public class ExceptionId {
  public enum ExceptionIdEnum {
    NUM_OF_SAMPLES("NUM_OF_SAMPLES"),
    DATA_WINDOW("DATA_WINDOW"),
    DATA_STAT_PROPS("DATA_STAT_PROPS"),
    STRATEGY("STRATEGY"),
    ACCURACY("ACCURACY");
    private String value;

    ExceptionIdEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ExceptionIdEnum fromValue(String text) {
      for (ExceptionIdEnum b : ExceptionIdEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("excepId")
  private ExceptionIdEnum excepId = null;

  public ExceptionId excepId(ExceptionIdEnum excepId){
    this.excepId = excepId;
    return this;
  }

  /** Get excepId
  * @return excepId
  **/
  @Schema(description="excepId")

  public ExceptionIdEnum getExcepId(){
    return excepId;
  }

  public void setExcepId(ExceptionIdEnum excepId){
    this.excepId = excepId;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExceptionId exceptionIdObject = (ExceptionId) o;
    return Objects.equals(this.excepId, exceptionIdObject.excepId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(excepId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExceptionId {\n");
    
    sb.append("    excepId: ").append(toIndentedString(excepId)).append("\n");
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
