package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents bandwidth requirements.
 */
@Schema(description = "Represents bandwidth requirements.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class BwRequirement   {
  @JsonProperty("appId")
  private String appId = null;

  @JsonProperty("marBwDl")
  private String marBwDl = null;

  @JsonProperty("marBwUl")
  private String marBwUl = null;

  @JsonProperty("mirBwDl")
  private String mirBwDl = null;

  @JsonProperty("mirBwUl")
  private String mirBwUl = null;

  public BwRequirement appId(String appId) {
    this.appId = appId;
    return this;
  }

  /**
   * String providing an application identifier.
   * @return appId
   **/
  @Schema(required = true, description = "String providing an application identifier.")
      @NotNull

    public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public BwRequirement marBwDl(String marBwDl) {
    this.marBwDl = marBwDl;
    return this;
  }

  /**
   * String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". 
   * @return marBwDl
   **/
  @Schema(description = "String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". ")
  
  @Pattern(regexp="^\\d+(\\.\\d+)? (bps|Kbps|Mbps|Gbps|Tbps)$")   public String getMarBwDl() {
    return marBwDl;
  }

  public void setMarBwDl(String marBwDl) {
    this.marBwDl = marBwDl;
  }

  public BwRequirement marBwUl(String marBwUl) {
    this.marBwUl = marBwUl;
    return this;
  }

  /**
   * String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". 
   * @return marBwUl
   **/
  @Schema(description = "String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". ")
  
  @Pattern(regexp="^\\d+(\\.\\d+)? (bps|Kbps|Mbps|Gbps|Tbps)$")   public String getMarBwUl() {
    return marBwUl;
  }

  public void setMarBwUl(String marBwUl) {
    this.marBwUl = marBwUl;
  }

  public BwRequirement mirBwDl(String mirBwDl) {
    this.mirBwDl = mirBwDl;
    return this;
  }

  /**
   * String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". 
   * @return mirBwDl
   **/
  @Schema(description = "String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". ")
  
  @Pattern(regexp="^\\d+(\\.\\d+)? (bps|Kbps|Mbps|Gbps|Tbps)$")   public String getMirBwDl() {
    return mirBwDl;
  }

  public void setMirBwDl(String mirBwDl) {
    this.mirBwDl = mirBwDl;
  }

  public BwRequirement mirBwUl(String mirBwUl) {
    this.mirBwUl = mirBwUl;
    return this;
  }

  /**
   * String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". 
   * @return mirBwUl
   **/
  @Schema(description = "String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". ")
  
  @Pattern(regexp="^\\d+(\\.\\d+)? (bps|Kbps|Mbps|Gbps|Tbps)$")   public String getMirBwUl() {
    return mirBwUl;
  }

  public void setMirBwUl(String mirBwUl) {
    this.mirBwUl = mirBwUl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BwRequirement bwRequirement = (BwRequirement) o;
    return Objects.equals(this.appId, bwRequirement.appId) &&
        Objects.equals(this.marBwDl, bwRequirement.marBwDl) &&
        Objects.equals(this.marBwUl, bwRequirement.marBwUl) &&
        Objects.equals(this.mirBwDl, bwRequirement.mirBwDl) &&
        Objects.equals(this.mirBwUl, bwRequirement.mirBwUl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, marBwDl, marBwUl, mirBwDl, mirBwUl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BwRequirement {\n");
    
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    marBwDl: ").append(toIndentedString(marBwDl)).append("\n");
    sb.append("    marBwUl: ").append(toIndentedString(marBwUl)).append("\n");
    sb.append("    mirBwDl: ").append(toIndentedString(mirBwDl)).append("\n");
    sb.append("    mirBwUl: ").append(toIndentedString(mirBwUl)).append("\n");
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
