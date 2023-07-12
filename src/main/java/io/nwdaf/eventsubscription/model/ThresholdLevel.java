package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Represents a threshold level.
 */
@Schema(description = "Represents a threshold level.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ThresholdLevel   {
  @JsonProperty("congLevel")
  private Integer congLevel = null;

  @JsonProperty("nfLoadLevel")
  private Integer nfLoadLevel = null;

  @JsonProperty("nfCpuUsage")
  private Integer nfCpuUsage = null;

  @JsonProperty("nfMemoryUsage")
  private Integer nfMemoryUsage = null;

  @JsonProperty("nfStorageUsage")
  private Integer nfStorageUsage = null;

  @JsonProperty("avgTrafficRate")
  private String avgTrafficRate = null;

  @JsonProperty("maxTrafficRate")
  private String maxTrafficRate = null;

  @JsonProperty("avgPacketDelay")
  private Integer avgPacketDelay = null;

  @JsonProperty("maxPacketDelay")
  private Integer maxPacketDelay = null;

  @JsonProperty("avgPacketLossRate")
  private Integer avgPacketLossRate = null;

  @JsonProperty("svcExpLevel")
  private Float svcExpLevel = null;

  public ThresholdLevel congLevel(Integer congLevel) {
    this.congLevel = congLevel;
    return this;
  }

  /**
   * Get congLevel
   * @return congLevel
   **/
  @Schema(description = "")
  
    public Integer getCongLevel() {
    return congLevel;
  }

  public void setCongLevel(Integer congLevel) {
    this.congLevel = congLevel;
  }

  public ThresholdLevel nfLoadLevel(Integer nfLoadLevel) {
    this.nfLoadLevel = nfLoadLevel;
    return this;
  }

  /**
   * Get nfLoadLevel
   * @return nfLoadLevel
   **/
  @Schema(description = "")
  
    public Integer getNfLoadLevel() {
    return nfLoadLevel;
  }

  public void setNfLoadLevel(Integer nfLoadLevel) {
    this.nfLoadLevel = nfLoadLevel;
  }

  public ThresholdLevel nfCpuUsage(Integer nfCpuUsage) {
    this.nfCpuUsage = nfCpuUsage;
    return this;
  }

  /**
   * Get nfCpuUsage
   * @return nfCpuUsage
   **/
  @Schema(description = "")
  
    public Integer getNfCpuUsage() {
    return nfCpuUsage;
  }

  public void setNfCpuUsage(Integer nfCpuUsage) {
    this.nfCpuUsage = nfCpuUsage;
  }

  public ThresholdLevel nfMemoryUsage(Integer nfMemoryUsage) {
    this.nfMemoryUsage = nfMemoryUsage;
    return this;
  }

  /**
   * Get nfMemoryUsage
   * @return nfMemoryUsage
   **/
  @Schema(description = "")
  
    public Integer getNfMemoryUsage() {
    return nfMemoryUsage;
  }

  public void setNfMemoryUsage(Integer nfMemoryUsage) {
    this.nfMemoryUsage = nfMemoryUsage;
  }

  public ThresholdLevel nfStorageUsage(Integer nfStorageUsage) {
    this.nfStorageUsage = nfStorageUsage;
    return this;
  }

  /**
   * Get nfStorageUsage
   * @return nfStorageUsage
   **/
  @Schema(description = "")
  
    public Integer getNfStorageUsage() {
    return nfStorageUsage;
  }

  public void setNfStorageUsage(Integer nfStorageUsage) {
    this.nfStorageUsage = nfStorageUsage;
  }

  public ThresholdLevel avgTrafficRate(String avgTrafficRate) {
    this.avgTrafficRate = avgTrafficRate;
    return this;
  }

  /**
   * String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". 
   * @return avgTrafficRate
   **/
  @Schema(description = "String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". ")
  
  @Pattern(regexp="^\\d+(\\.\\d+)? (bps|Kbps|Mbps|Gbps|Tbps)$")   public String getAvgTrafficRate() {
    return avgTrafficRate;
  }

  public void setAvgTrafficRate(String avgTrafficRate) {
    this.avgTrafficRate = avgTrafficRate;
  }

  public ThresholdLevel maxTrafficRate(String maxTrafficRate) {
    this.maxTrafficRate = maxTrafficRate;
    return this;
  }

  /**
   * String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". 
   * @return maxTrafficRate
   **/
  @Schema(description = "String representing a bit rate; the prefixes follow the standard symbols from The International System of Units, and represent x1000 multipliers, with the exception that prefix \"K\" is used to represent the standard symbol \"k\". ")
  
  @Pattern(regexp="^\\d+(\\.\\d+)? (bps|Kbps|Mbps|Gbps|Tbps)$")   public String getMaxTrafficRate() {
    return maxTrafficRate;
  }

  public void setMaxTrafficRate(String maxTrafficRate) {
    this.maxTrafficRate = maxTrafficRate;
  }

  public ThresholdLevel avgPacketDelay(Integer avgPacketDelay) {
    this.avgPacketDelay = avgPacketDelay;
    return this;
  }

  /**
   * Unsigned integer indicating Packet Delay Budget (see clauses 5.7.3.4 and 5.7.4 of 3GPP TS 23.501), expressed in milliseconds. 
   * minimum: 1
   * @return avgPacketDelay
   **/
  @Schema(description = "Unsigned integer indicating Packet Delay Budget (see clauses 5.7.3.4 and 5.7.4 of 3GPP TS 23.501), expressed in milliseconds. ")
  
  @Min(1)  public Integer getAvgPacketDelay() {
    return avgPacketDelay;
  }

  public void setAvgPacketDelay(Integer avgPacketDelay) {
    this.avgPacketDelay = avgPacketDelay;
  }

  public ThresholdLevel maxPacketDelay(Integer maxPacketDelay) {
    this.maxPacketDelay = maxPacketDelay;
    return this;
  }

  /**
   * Unsigned integer indicating Packet Delay Budget (see clauses 5.7.3.4 and 5.7.4 of 3GPP TS 23.501), expressed in milliseconds. 
   * minimum: 1
   * @return maxPacketDelay
   **/
  @Schema(description = "Unsigned integer indicating Packet Delay Budget (see clauses 5.7.3.4 and 5.7.4 of 3GPP TS 23.501), expressed in milliseconds. ")
  
  @Min(1)  public Integer getMaxPacketDelay() {
    return maxPacketDelay;
  }

  public void setMaxPacketDelay(Integer maxPacketDelay) {
    this.maxPacketDelay = maxPacketDelay;
  }

  public ThresholdLevel avgPacketLossRate(Integer avgPacketLossRate) {
    this.avgPacketLossRate = avgPacketLossRate;
    return this;
  }

  /**
   * Unsigned integer indicating Packet Loss Rate (see clauses 5.7.2.8 and 5.7.4 of 3GPP TS 23.501), expressed in tenth of percent. 
   * minimum: 0
   * maximum: 1000
   * @return avgPacketLossRate
   **/
  @Schema(description = "Unsigned integer indicating Packet Loss Rate (see clauses 5.7.2.8 and 5.7.4 of 3GPP TS 23.501), expressed in tenth of percent. ")
  
  @Min(0) @Max(1000)   public Integer getAvgPacketLossRate() {
    return avgPacketLossRate;
  }

  public void setAvgPacketLossRate(Integer avgPacketLossRate) {
    this.avgPacketLossRate = avgPacketLossRate;
  }

  public ThresholdLevel svcExpLevel(Float svcExpLevel) {
    this.svcExpLevel = svcExpLevel;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return svcExpLevel
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getSvcExpLevel() {
    return svcExpLevel;
  }

  public void setSvcExpLevel(Float svcExpLevel) {
    this.svcExpLevel = svcExpLevel;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ThresholdLevel thresholdLevel = (ThresholdLevel) o;
    return Objects.equals(this.congLevel, thresholdLevel.congLevel) &&
        Objects.equals(this.nfLoadLevel, thresholdLevel.nfLoadLevel) &&
        Objects.equals(this.nfCpuUsage, thresholdLevel.nfCpuUsage) &&
        Objects.equals(this.nfMemoryUsage, thresholdLevel.nfMemoryUsage) &&
        Objects.equals(this.nfStorageUsage, thresholdLevel.nfStorageUsage) &&
        Objects.equals(this.avgTrafficRate, thresholdLevel.avgTrafficRate) &&
        Objects.equals(this.maxTrafficRate, thresholdLevel.maxTrafficRate) &&
        Objects.equals(this.avgPacketDelay, thresholdLevel.avgPacketDelay) &&
        Objects.equals(this.maxPacketDelay, thresholdLevel.maxPacketDelay) &&
        Objects.equals(this.avgPacketLossRate, thresholdLevel.avgPacketLossRate) &&
        Objects.equals(this.svcExpLevel, thresholdLevel.svcExpLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(congLevel, nfLoadLevel, nfCpuUsage, nfMemoryUsage, nfStorageUsage, avgTrafficRate, maxTrafficRate, avgPacketDelay, maxPacketDelay, avgPacketLossRate, svcExpLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ThresholdLevel {\n");
    
    sb.append("    congLevel: ").append(toIndentedString(congLevel)).append("\n");
    sb.append("    nfLoadLevel: ").append(toIndentedString(nfLoadLevel)).append("\n");
    sb.append("    nfCpuUsage: ").append(toIndentedString(nfCpuUsage)).append("\n");
    sb.append("    nfMemoryUsage: ").append(toIndentedString(nfMemoryUsage)).append("\n");
    sb.append("    nfStorageUsage: ").append(toIndentedString(nfStorageUsage)).append("\n");
    sb.append("    avgTrafficRate: ").append(toIndentedString(avgTrafficRate)).append("\n");
    sb.append("    maxTrafficRate: ").append(toIndentedString(maxTrafficRate)).append("\n");
    sb.append("    avgPacketDelay: ").append(toIndentedString(avgPacketDelay)).append("\n");
    sb.append("    maxPacketDelay: ").append(toIndentedString(maxPacketDelay)).append("\n");
    sb.append("    avgPacketLossRate: ").append(toIndentedString(avgPacketLossRate)).append("\n");
    sb.append("    svcExpLevel: ").append(toIndentedString(svcExpLevel)).append("\n");
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
