package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.TrafficInformation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * WLAN performance information per Time Slot during the analytics target period.
 */
@Schema(description = "WLAN performance information per Time Slot during the analytics target period.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class WlanPerTsPerformanceInfo  implements AnyOfWlanPerTsPerformanceInfo {
  @JsonProperty("tsStart")
  private OffsetDateTime tsStart = null;

  @JsonProperty("tsDuration")
  private Integer tsDuration = null;

  @JsonProperty("rssi")
  private Integer rssi = null;

  @JsonProperty("rtt")
  private Integer rtt = null;

  @JsonProperty("trafficInfo")
  private TrafficInformation trafficInfo = null;

  @JsonProperty("numberOfUes")
  private Integer numberOfUes = null;

  @JsonProperty("confidence")
  private Integer confidence = null;

  public WlanPerTsPerformanceInfo tsStart(OffsetDateTime tsStart) {
    this.tsStart = tsStart;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return tsStart
   **/
  @Schema(required = true, description = "string with format 'date-time' as defined in OpenAPI.")
      @NotNull

    @Valid
    public OffsetDateTime getTsStart() {
    return tsStart;
  }

  public void setTsStart(OffsetDateTime tsStart) {
    this.tsStart = tsStart;
  }

  public WlanPerTsPerformanceInfo tsDuration(Integer tsDuration) {
    this.tsDuration = tsDuration;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return tsDuration
   **/
  @Schema(required = true, description = "indicating a time in seconds.")
      @NotNull

    public Integer getTsDuration() {
    return tsDuration;
  }

  public void setTsDuration(Integer tsDuration) {
    this.tsDuration = tsDuration;
  }

  public WlanPerTsPerformanceInfo rssi(Integer rssi) {
    this.rssi = rssi;
    return this;
  }

  /**
   * Get rssi
   * @return rssi
   **/
  @Schema(description = "")
  
    public Integer getRssi() {
    return rssi;
  }

  public void setRssi(Integer rssi) {
    this.rssi = rssi;
  }

  public WlanPerTsPerformanceInfo rtt(Integer rtt) {
    this.rtt = rtt;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return rtt
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getRtt() {
    return rtt;
  }

  public void setRtt(Integer rtt) {
    this.rtt = rtt;
  }

  public WlanPerTsPerformanceInfo trafficInfo(TrafficInformation trafficInfo) {
    this.trafficInfo = trafficInfo;
    return this;
  }

  /**
   * Get trafficInfo
   * @return trafficInfo
   **/
  @Schema(description = "")
  
    @Valid
    public TrafficInformation getTrafficInfo() {
    return trafficInfo;
  }

  public void setTrafficInfo(TrafficInformation trafficInfo) {
    this.trafficInfo = trafficInfo;
  }

  public WlanPerTsPerformanceInfo numberOfUes(Integer numberOfUes) {
    this.numberOfUes = numberOfUes;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return numberOfUes
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getNumberOfUes() {
    return numberOfUes;
  }

  public void setNumberOfUes(Integer numberOfUes) {
    this.numberOfUes = numberOfUes;
  }

  public WlanPerTsPerformanceInfo confidence(Integer confidence) {
    this.confidence = confidence;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return confidence
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getConfidence() {
    return confidence;
  }

  public void setConfidence(Integer confidence) {
    this.confidence = confidence;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WlanPerTsPerformanceInfo wlanPerTsPerformanceInfo = (WlanPerTsPerformanceInfo) o;
    return Objects.equals(this.tsStart, wlanPerTsPerformanceInfo.tsStart) &&
        Objects.equals(this.tsDuration, wlanPerTsPerformanceInfo.tsDuration) &&
        Objects.equals(this.rssi, wlanPerTsPerformanceInfo.rssi) &&
        Objects.equals(this.rtt, wlanPerTsPerformanceInfo.rtt) &&
        Objects.equals(this.trafficInfo, wlanPerTsPerformanceInfo.trafficInfo) &&
        Objects.equals(this.numberOfUes, wlanPerTsPerformanceInfo.numberOfUes) &&
        Objects.equals(this.confidence, wlanPerTsPerformanceInfo.confidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tsStart, tsDuration, rssi, rtt, trafficInfo, numberOfUes, confidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WlanPerTsPerformanceInfo {\n");
    
    sb.append("    tsStart: ").append(toIndentedString(tsStart)).append("\n");
    sb.append("    tsDuration: ").append(toIndentedString(tsDuration)).append("\n");
    sb.append("    rssi: ").append(toIndentedString(rssi)).append("\n");
    sb.append("    rtt: ").append(toIndentedString(rtt)).append("\n");
    sb.append("    trafficInfo: ").append(toIndentedString(trafficInfo)).append("\n");
    sb.append("    numberOfUes: ").append(toIndentedString(numberOfUes)).append("\n");
    sb.append("    confidence: ").append(toIndentedString(confidence)).append("\n");
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
