package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents DN performance for the application.
 */
@Schema(description = "Represents DN performance for the application.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class DnPerf   {
  @JsonProperty("appServerInsAddr")
  private AddrFqdn appServerInsAddr = null;

  @JsonProperty("upfInfo")
  private UpfInformation upfInfo = null;

  @JsonProperty("dnai")
  private String dnai = null;

  @JsonProperty("perfData")
  private PerfData perfData = null;

  @JsonProperty("spatialValidCon")
  private NetworkAreaInfo spatialValidCon = null;

  @JsonProperty("temporalValidCon")
  private TimeWindow temporalValidCon = null;

  public DnPerf appServerInsAddr(AddrFqdn appServerInsAddr) {
    this.appServerInsAddr = appServerInsAddr;
    return this;
  }

  /**
   * Get appServerInsAddr
   * @return appServerInsAddr
   **/
  @Schema(description = "")
  
    @Valid
    public AddrFqdn getAppServerInsAddr() {
    return appServerInsAddr;
  }

  public void setAppServerInsAddr(AddrFqdn appServerInsAddr) {
    this.appServerInsAddr = appServerInsAddr;
  }

  public DnPerf upfInfo(UpfInformation upfInfo) {
    this.upfInfo = upfInfo;
    return this;
  }

  /**
   * Get upfInfo
   * @return upfInfo
   **/
  @Schema(description = "")
  
    @Valid
    public UpfInformation getUpfInfo() {
    return upfInfo;
  }

  public void setUpfInfo(UpfInformation upfInfo) {
    this.upfInfo = upfInfo;
  }

  public DnPerf dnai(String dnai) {
    this.dnai = dnai;
    return this;
  }

  /**
   * DNAI (Data network access identifier), see clause 5.6.7 of 3GPP TS 23.501.
   * @return dnai
   **/
  @Schema(description = "DNAI (Data network access identifier), see clause 5.6.7 of 3GPP TS 23.501.")
  
    public String getDnai() {
    return dnai;
  }

  public void setDnai(String dnai) {
    this.dnai = dnai;
  }

  public DnPerf perfData(PerfData perfData) {
    this.perfData = perfData;
    return this;
  }

  /**
   * Get perfData
   * @return perfData
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public PerfData getPerfData() {
    return perfData;
  }

  public void setPerfData(PerfData perfData) {
    this.perfData = perfData;
  }

  public DnPerf spatialValidCon(NetworkAreaInfo spatialValidCon) {
    this.spatialValidCon = spatialValidCon;
    return this;
  }

  /**
   * Get spatialValidCon
   * @return spatialValidCon
   **/
  @Schema(description = "")
  
    @Valid
    public NetworkAreaInfo getSpatialValidCon() {
    return spatialValidCon;
  }

  public void setSpatialValidCon(NetworkAreaInfo spatialValidCon) {
    this.spatialValidCon = spatialValidCon;
  }

  public DnPerf temporalValidCon(TimeWindow temporalValidCon) {
    this.temporalValidCon = temporalValidCon;
    return this;
  }

  /**
   * Get temporalValidCon
   * @return temporalValidCon
   **/
  @Schema(description = "")
  
    @Valid
    public TimeWindow getTemporalValidCon() {
    return temporalValidCon;
  }

  public void setTemporalValidCon(TimeWindow temporalValidCon) {
    this.temporalValidCon = temporalValidCon;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DnPerf dnPerf = (DnPerf) o;
    return Objects.equals(this.appServerInsAddr, dnPerf.appServerInsAddr) &&
        Objects.equals(this.upfInfo, dnPerf.upfInfo) &&
        Objects.equals(this.dnai, dnPerf.dnai) &&
        Objects.equals(this.perfData, dnPerf.perfData) &&
        Objects.equals(this.spatialValidCon, dnPerf.spatialValidCon) &&
        Objects.equals(this.temporalValidCon, dnPerf.temporalValidCon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appServerInsAddr, upfInfo, dnai, perfData, spatialValidCon, temporalValidCon);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DnPerf {\n");
    
    sb.append("    appServerInsAddr: ").append(toIndentedString(appServerInsAddr)).append("\n");
    sb.append("    upfInfo: ").append(toIndentedString(upfInfo)).append("\n");
    sb.append("    dnai: ").append(toIndentedString(dnai)).append("\n");
    sb.append("    perfData: ").append(toIndentedString(perfData)).append("\n");
    sb.append("    spatialValidCon: ").append(toIndentedString(spatialValidCon)).append("\n");
    sb.append("    temporalValidCon: ").append(toIndentedString(temporalValidCon)).append("\n");
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
