package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Traffic information including UL/DL data rate and/or Traffic volume.
 */
@Schema(description = "Traffic information including UL/DL data rate and/or Traffic volume.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class TrafficInformation  implements AnyOfTrafficInformation {
  @JsonProperty("uplinkRate")
  private String uplinkRate = null;

  @JsonProperty("downlinkRate")
  private String downlinkRate = null;

  @JsonProperty("uplinkVolume")
  private Long uplinkVolume = null;

  @JsonProperty("downlinkVolume")
  private Long downlinkVolume = null;

  @JsonProperty("totalVolume")
  private Long totalVolume = null;

  public TrafficInformation uplinkRate(String uplinkRate) {
    this.uplinkRate = uplinkRate;
    return this;
  }

  /**
   * String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". 
   * @return uplinkRate
   **/
  @Schema(description = "String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". ")
  
  @Pattern(regexp="^\\d+(\\.\\d+)? (bps|Kbps|Mbps|Gbps|Tbps)$")   public String getUplinkRate() {
    return uplinkRate;
  }

  public void setUplinkRate(String uplinkRate) {
    this.uplinkRate = uplinkRate;
  }

  public TrafficInformation downlinkRate(String downlinkRate) {
    this.downlinkRate = downlinkRate;
    return this;
  }

  /**
   * String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". 
   * @return downlinkRate
   **/
  @Schema(description = "String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". ")
  
  @Pattern(regexp="^\\d+(\\.\\d+)? (bps|Kbps|Mbps|Gbps|Tbps)$")   public String getDownlinkRate() {
    return downlinkRate;
  }

  public void setDownlinkRate(String downlinkRate) {
    this.downlinkRate = downlinkRate;
  }

  public TrafficInformation uplinkVolume(Long uplinkVolume) {
    this.uplinkVolume = uplinkVolume;
    return this;
  }

  /**
   * Unsigned integer identifying a volume in units of bytes.
   * minimum: 0
   * @return uplinkVolume
   **/
  @Schema(description = "Unsigned integer identifying a volume in units of bytes.")
  
  @Min(0L)  public Long getUplinkVolume() {
    return uplinkVolume;
  }

  public void setUplinkVolume(Long uplinkVolume) {
    this.uplinkVolume = uplinkVolume;
  }

  public TrafficInformation downlinkVolume(Long downlinkVolume) {
    this.downlinkVolume = downlinkVolume;
    return this;
  }

  /**
   * Unsigned integer identifying a volume in units of bytes.
   * minimum: 0
   * @return downlinkVolume
   **/
  @Schema(description = "Unsigned integer identifying a volume in units of bytes.")
  
  @Min(0L)  public Long getDownlinkVolume() {
    return downlinkVolume;
  }

  public void setDownlinkVolume(Long downlinkVolume) {
    this.downlinkVolume = downlinkVolume;
  }

  public TrafficInformation totalVolume(Long totalVolume) {
    this.totalVolume = totalVolume;
    return this;
  }

  /**
   * Unsigned integer identifying a volume in units of bytes.
   * minimum: 0
   * @return totalVolume
   **/
  @Schema(description = "Unsigned integer identifying a volume in units of bytes.")
  
  @Min(0L)  public Long getTotalVolume() {
    return totalVolume;
  }

  public void setTotalVolume(Long totalVolume) {
    this.totalVolume = totalVolume;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrafficInformation trafficInformation = (TrafficInformation) o;
    return Objects.equals(this.uplinkRate, trafficInformation.uplinkRate) &&
        Objects.equals(this.downlinkRate, trafficInformation.downlinkRate) &&
        Objects.equals(this.uplinkVolume, trafficInformation.uplinkVolume) &&
        Objects.equals(this.downlinkVolume, trafficInformation.downlinkVolume) &&
        Objects.equals(this.totalVolume, trafficInformation.totalVolume);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uplinkRate, downlinkRate, uplinkVolume, downlinkVolume, totalVolume);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrafficInformation {\n");
    
    sb.append("    uplinkRate: ").append(toIndentedString(uplinkRate)).append("\n");
    sb.append("    downlinkRate: ").append(toIndentedString(downlinkRate)).append("\n");
    sb.append("    uplinkVolume: ").append(toIndentedString(uplinkVolume)).append("\n");
    sb.append("    downlinkVolume: ").append(toIndentedString(downlinkVolume)).append("\n");
    sb.append("    totalVolume: ").append(toIndentedString(totalVolume)).append("\n");
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
