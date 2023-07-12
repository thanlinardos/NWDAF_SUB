package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Dispersion collection per UE location or per slice.
 */
@Schema(description = "Dispersion collection per UE location or per slice.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class DispersionCollection   {
  @JsonProperty("ueLoc")
  private UserLocation ueLoc = null;

  @JsonProperty("snssai")
  private Snssai snssai = null;

  @JsonProperty("supis")
  @Valid
  private List<String> supis = null;

  @JsonProperty("gpsis")
  @Valid
  private List<String> gpsis = null;

  @JsonProperty("appVolumes")
  @Valid
  private List<ApplicationVolume> appVolumes = null;

  @JsonProperty("disperAmount")
  private Integer disperAmount = null;

  @JsonProperty("disperClass")
  private DispersionClass disperClass = null;

  @JsonProperty("usageRank")
  private Integer usageRank = null;

  @JsonProperty("percentileRank")
  private Integer percentileRank = null;

  @JsonProperty("ueRatio")
  private Integer ueRatio = null;

  @JsonProperty("confidence")
  private Integer confidence = null;

  public DispersionCollection ueLoc(UserLocation ueLoc) {
    this.ueLoc = ueLoc;
    return this;
  }

  /**
   * Get ueLoc
   * @return ueLoc
   **/
  @Schema(description = "")
  
    @Valid
    public UserLocation getUeLoc() {
    return ueLoc;
  }

  public void setUeLoc(UserLocation ueLoc) {
    this.ueLoc = ueLoc;
  }

  public DispersionCollection snssai(Snssai snssai) {
    this.snssai = snssai;
    return this;
  }

  /**
   * Get snssai
   * @return snssai
   **/
  @Schema(description = "")
  
    @Valid
    public Snssai getSnssai() {
    return snssai;
  }

  public void setSnssai(Snssai snssai) {
    this.snssai = snssai;
  }

  public DispersionCollection supis(List<String> supis) {
    this.supis = supis;
    return this;
  }

  public DispersionCollection addSupisItem(String supisItem) {
    if (this.supis == null) {
      this.supis = new ArrayList<String>();
    }
    this.supis.add(supisItem);
    return this;
  }

  /**
   * Get supis
   * @return supis
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getSupis() {
    return supis;
  }

  public void setSupis(List<String> supis) {
    this.supis = supis;
  }

  public DispersionCollection gpsis(List<String> gpsis) {
    this.gpsis = gpsis;
    return this;
  }

  public DispersionCollection addGpsisItem(String gpsisItem) {
    if (this.gpsis == null) {
      this.gpsis = new ArrayList<String>();
    }
    this.gpsis.add(gpsisItem);
    return this;
  }

  /**
   * Get gpsis
   * @return gpsis
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getGpsis() {
    return gpsis;
  }

  public void setGpsis(List<String> gpsis) {
    this.gpsis = gpsis;
  }

  public DispersionCollection appVolumes(List<ApplicationVolume> appVolumes) {
    this.appVolumes = appVolumes;
    return this;
  }

  public DispersionCollection addAppVolumesItem(ApplicationVolume appVolumesItem) {
    if (this.appVolumes == null) {
      this.appVolumes = new ArrayList<ApplicationVolume>();
    }
    this.appVolumes.add(appVolumesItem);
    return this;
  }

  /**
   * Get appVolumes
   * @return appVolumes
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<ApplicationVolume> getAppVolumes() {
    return appVolumes;
  }

  public void setAppVolumes(List<ApplicationVolume> appVolumes) {
    this.appVolumes = appVolumes;
  }

  public DispersionCollection disperAmount(Integer disperAmount) {
    this.disperAmount = disperAmount;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return disperAmount
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getDisperAmount() {
    return disperAmount;
  }

  public void setDisperAmount(Integer disperAmount) {
    this.disperAmount = disperAmount;
  }

  public DispersionCollection disperClass(DispersionClass disperClass) {
    this.disperClass = disperClass;
    return this;
  }

  /**
   * Get disperClass
   * @return disperClass
   **/
  @Schema(description = "")
  
    @Valid
    public DispersionClass getDisperClass() {
    return disperClass;
  }

  public void setDisperClass(DispersionClass disperClass) {
    this.disperClass = disperClass;
  }

  public DispersionCollection usageRank(Integer usageRank) {
    this.usageRank = usageRank;
    return this;
  }

  /**
   * Integer where the allowed values correspond to 1, 2, 3 only.
   * minimum: 1
   * maximum: 3
   * @return usageRank
   **/
  @Schema(description = "Integer where the allowed values correspond to 1, 2, 3 only.")
  
  @Min(1) @Max(3)   public Integer getUsageRank() {
    return usageRank;
  }

  public void setUsageRank(Integer usageRank) {
    this.usageRank = usageRank;
  }

  public DispersionCollection percentileRank(Integer percentileRank) {
    this.percentileRank = percentileRank;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return percentileRank
   **/
  @Schema(description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
  
  @Min(1) @Max(100)   public Integer getPercentileRank() {
    return percentileRank;
  }

  public void setPercentileRank(Integer percentileRank) {
    this.percentileRank = percentileRank;
  }

  public DispersionCollection ueRatio(Integer ueRatio) {
    this.ueRatio = ueRatio;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return ueRatio
   **/
  @Schema(description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
  
  @Min(1) @Max(100)   public Integer getUeRatio() {
    return ueRatio;
  }

  public void setUeRatio(Integer ueRatio) {
    this.ueRatio = ueRatio;
  }

  public DispersionCollection confidence(Integer confidence) {
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
    DispersionCollection dispersionCollection = (DispersionCollection) o;
    return Objects.equals(this.ueLoc, dispersionCollection.ueLoc) &&
        Objects.equals(this.snssai, dispersionCollection.snssai) &&
        Objects.equals(this.supis, dispersionCollection.supis) &&
        Objects.equals(this.gpsis, dispersionCollection.gpsis) &&
        Objects.equals(this.appVolumes, dispersionCollection.appVolumes) &&
        Objects.equals(this.disperAmount, dispersionCollection.disperAmount) &&
        Objects.equals(this.disperClass, dispersionCollection.disperClass) &&
        Objects.equals(this.usageRank, dispersionCollection.usageRank) &&
        Objects.equals(this.percentileRank, dispersionCollection.percentileRank) &&
        Objects.equals(this.ueRatio, dispersionCollection.ueRatio) &&
        Objects.equals(this.confidence, dispersionCollection.confidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ueLoc, snssai, supis, gpsis, appVolumes, disperAmount, disperClass, usageRank, percentileRank, ueRatio, confidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DispersionCollection {\n");
    
    sb.append("    ueLoc: ").append(toIndentedString(ueLoc)).append("\n");
    sb.append("    snssai: ").append(toIndentedString(snssai)).append("\n");
    sb.append("    supis: ").append(toIndentedString(supis)).append("\n");
    sb.append("    gpsis: ").append(toIndentedString(gpsis)).append("\n");
    sb.append("    appVolumes: ").append(toIndentedString(appVolumes)).append("\n");
    sb.append("    disperAmount: ").append(toIndentedString(disperAmount)).append("\n");
    sb.append("    disperClass: ").append(toIndentedString(disperClass)).append("\n");
    sb.append("    usageRank: ").append(toIndentedString(usageRank)).append("\n");
    sb.append("    percentileRank: ").append(toIndentedString(percentileRank)).append("\n");
    sb.append("    ueRatio: ").append(toIndentedString(ueRatio)).append("\n");
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
