package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Represents the N4 Session inactivity timer.
 */
@Schema(description = "Represents the N4 Session inactivity timer.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class SessInactTimerForUeComm   {
  @JsonProperty("n4SessId")
  private Integer n4SessId = null;

  @JsonProperty("sessInactiveTimer")
  private Integer sessInactiveTimer = null;

  public SessInactTimerForUeComm n4SessId(Integer n4SessId) {
    this.n4SessId = n4SessId;
    return this;
  }

  /**
   * Unsigned integer identifying a PDU session, within the range 0 to 255, as specified in  clause 11.2.3.1b, bits 1 to 8, of 3GPP TS 24.007. If the PDU Session ID is allocated by the  Core Network for UEs not supporting N1 mode, reserved range 64 to 95 is used. PDU Session ID  within the reserved range is only visible in the Core Network.  
   * minimum: 0
   * maximum: 255
   * @return n4SessId
   **/
  @Schema(required = true, description = "Unsigned integer identifying a PDU session, within the range 0 to 255, as specified in  clause 11.2.3.1b, bits 1 to 8, of 3GPP TS 24.007. If the PDU Session ID is allocated by the  Core Network for UEs not supporting N1 mode, reserved range 64 to 95 is used. PDU Session ID  within the reserved range is only visible in the Core Network.  ")
      @NotNull

  @Min(0) @Max(255)   public Integer getN4SessId() {
    return n4SessId;
  }

  public void setN4SessId(Integer n4SessId) {
    this.n4SessId = n4SessId;
  }

  public SessInactTimerForUeComm sessInactiveTimer(Integer sessInactiveTimer) {
    this.sessInactiveTimer = sessInactiveTimer;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return sessInactiveTimer
   **/
  @Schema(required = true, description = "indicating a time in seconds.")
      @NotNull

    public Integer getSessInactiveTimer() {
    return sessInactiveTimer;
  }

  public void setSessInactiveTimer(Integer sessInactiveTimer) {
    this.sessInactiveTimer = sessInactiveTimer;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessInactTimerForUeComm sessInactTimerForUeComm = (SessInactTimerForUeComm) o;
    return Objects.equals(this.n4SessId, sessInactTimerForUeComm.n4SessId) &&
        Objects.equals(this.sessInactiveTimer, sessInactTimerForUeComm.sessInactiveTimer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(n4SessId, sessInactiveTimer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessInactTimerForUeComm {\n");
    
    sb.append("    n4SessId: ").append(toIndentedString(n4SessId)).append("\n");
    sb.append("    sessInactiveTimer: ").append(toIndentedString(sessInactiveTimer)).append("\n");
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
