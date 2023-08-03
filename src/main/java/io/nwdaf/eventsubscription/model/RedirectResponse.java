package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * The response shall include a Location header field containing a different URI  (pointing to a different URI of an other service instance), or the same URI if a request  is redirected to the same target resource via a different SCP. 
 */
@Schema(description = "The response shall include a Location header field containing a different URI  (pointing to a different URI of an other service instance), or the same URI if a request  is redirected to the same target resource via a different SCP. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class RedirectResponse   {
  @JsonProperty("cause")
  private String cause = null;

  @JsonProperty("targetScp")
  private String targetScp = null;

  @JsonProperty("targetSepp")
  private String targetSepp = null;

  public RedirectResponse cause(String cause) {
    this.cause = cause;
    return this;
  }

  /**
   * Get cause
   * @return cause
   **/
  @Schema(description = "")
  
    public String getCause() {
    return cause;
  }

  public void setCause(String cause) {
    this.cause = cause;
  }

  public RedirectResponse targetScp(String targetScp) {
    this.targetScp = targetScp;
    return this;
  }

  /**
   * String providing an URI formatted according to RFC 3986.
   * @return targetScp
   **/
  @Schema(description = "String providing an URI formatted according to RFC 3986.")
  
    public String getTargetScp() {
    return targetScp;
  }

  public void setTargetScp(String targetScp) {
    this.targetScp = targetScp;
  }

  public RedirectResponse targetSepp(String targetSepp) {
    this.targetSepp = targetSepp;
    return this;
  }

  /**
   * String providing an URI formatted according to RFC 3986.
   * @return targetSepp
   **/
  @Schema(description = "String providing an URI formatted according to RFC 3986.")
  
    public String getTargetSepp() {
    return targetSepp;
  }

  public void setTargetSepp(String targetSepp) {
    this.targetSepp = targetSepp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RedirectResponse redirectResponse = (RedirectResponse) o;
    return Objects.equals(this.cause, redirectResponse.cause) &&
        Objects.equals(this.targetScp, redirectResponse.targetScp) &&
        Objects.equals(this.targetSepp, redirectResponse.targetSepp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cause, targetScp, targetSepp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RedirectResponse {\n");
    
    sb.append("    cause: ").append(toIndentedString(cause)).append("\n");
    sb.append("    targetScp: ").append(toIndentedString(targetScp)).append("\n");
    sb.append("    targetSepp: ").append(toIndentedString(targetSepp)).append("\n");
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
