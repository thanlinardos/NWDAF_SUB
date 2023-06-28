package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - UNAVAILABLE_DATA: Indicates the requested statistics information for the event is rejected since necessary data to perform the service is unavailable. - BOTH_STAT_PRED_NOT_ALLOWED: Indicates the requested analysis information for the event is rejected since the start time is in the past and the end time is in the future, which means the NF service consumer requested both statistics and prediction for the analytics. - UNSATISFIED_REQUESTED_ANALYTICS_TIME: Indicates that the requested event is rejected since the analytics information is not ready when the time indicated by the \&quot;timeAnaNeeded\&quot; attribute (as provided during the creation or modification of subscription) is reached. - OTHER: Indicates the requested analysis information for the event is rejected due to other reasons.  
*/
@Schema(description = "Identifies the failure reason.")
@Validated
public class NwdafFailureCode {
  public enum NwdafFailureCodeEnum {
    UNAVAILABLE_DATA("UNAVAILABLE_DATA"),
    BOTH_STAT_PRED_NOT_ALLOWED("BOTH_STAT_PRED_NOT_ALLOWED"),
    UNSATISFIED_REQUESTED_ANALYTICS_TIME("UNSATISFIED_REQUESTED_ANALYTICS_TIME"),
    OTHER("OTHER");
    private String value;

    NwdafFailureCodeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NwdafFailureCodeEnum fromValue(String text) {
      for (NwdafFailureCodeEnum b : NwdafFailureCodeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("failureCode")
  private NwdafFailureCodeEnum failureCode = null;

  public NwdafFailureCode failureCode(NwdafFailureCodeEnum failureCode){
    this.failureCode = failureCode;
    return this;
  }

  /** Get failureCode
  * @return failureCode
  **/
  @Schema(description="failureCode")

  public NwdafFailureCodeEnum getFailureCode(){
    return failureCode;
  }

  public void setFailureCode(NwdafFailureCodeEnum failureCode){
    this.failureCode = failureCode;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NwdafFailureCode nwdafFailureCodeObject = (NwdafFailureCode) o;
    return Objects.equals(this.failureCode, nwdafFailureCodeObject.failureCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(failureCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NwdafFailureCode {\n");
    
    sb.append("    failureCode: ").append(toIndentedString(failureCode)).append("\n");
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
