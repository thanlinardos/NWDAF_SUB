package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents the type of reporting that the subscription requires.
 */
@Schema(description = "Represents the type of reporting that the subscription requires.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ReportingInformation   {
  @JsonProperty("immRep")
  private Boolean immRep = null;

  @JsonProperty("notifMethod")
  private NotificationMethod notifMethod = null;

  @JsonProperty("maxReportNbr")
  private Integer maxReportNbr = null;

  @JsonProperty("monDur")
  private OffsetDateTime monDur = null;

  @JsonProperty("repPeriod")
  private Integer repPeriod = null;

  @JsonProperty("sampRatio")
  private Integer sampRatio = null;

  @JsonProperty("partitionCriteria")
  @Valid
  private List<PartitioningCriteria> partitionCriteria = null;

  @JsonProperty("grpRepTime")
  private Integer grpRepTime = null;

  @JsonProperty("notifFlag")
  private NotificationFlag notifFlag = null;

  public ReportingInformation immRep(Boolean immRep) {
    this.immRep = immRep;
    return this;
  }

  /**
   * Get immRep
   * @return immRep
   **/
  @Schema(description = "")
  
    public Boolean isImmRep() {
    return immRep;
  }

  public void setImmRep(Boolean immRep) {
    this.immRep = immRep;
  }

  public ReportingInformation notifMethod(NotificationMethod notifMethod) {
    this.notifMethod = notifMethod;
    return this;
  }

  /**
   * Get notifMethod
   * @return notifMethod
   **/
  @Schema(description = "")
  
    @Valid
    public NotificationMethod getNotifMethod() {
    return notifMethod;
  }

  public void setNotifMethod(NotificationMethod notifMethod) {
    this.notifMethod = notifMethod;
  }

  public ReportingInformation maxReportNbr(Integer maxReportNbr) {
    this.maxReportNbr = maxReportNbr;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return maxReportNbr
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getMaxReportNbr() {
    return maxReportNbr;
  }

  public void setMaxReportNbr(Integer maxReportNbr) {
    this.maxReportNbr = maxReportNbr;
  }

  public ReportingInformation monDur(OffsetDateTime monDur) {
    this.monDur = monDur;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return monDur
   **/
  @Schema(description = "string with format 'date-time' as defined in OpenAPI.")
  
    @Valid
    public OffsetDateTime getMonDur() {
    return monDur;
  }

  public void setMonDur(OffsetDateTime monDur) {
    this.monDur = monDur;
  }

  public ReportingInformation repPeriod(Integer repPeriod) {
    this.repPeriod = repPeriod;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return repPeriod
   **/
  @Schema(description = "indicating a time in seconds.")
  
    public Integer getRepPeriod() {
    return repPeriod;
  }

  public void setRepPeriod(Integer repPeriod) {
    this.repPeriod = repPeriod;
  }

  public ReportingInformation sampRatio(Integer sampRatio) {
    this.sampRatio = sampRatio;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return sampRatio
   **/
  @Schema(description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
  
  @Min(1) @Max(100)   public Integer getSampRatio() {
    return sampRatio;
  }

  public void setSampRatio(Integer sampRatio) {
    this.sampRatio = sampRatio;
  }

  public ReportingInformation partitionCriteria(List<PartitioningCriteria> partitionCriteria) {
    this.partitionCriteria = partitionCriteria;
    return this;
  }

  public ReportingInformation addPartitionCriteriaItem(PartitioningCriteria partitionCriteriaItem) {
    if (this.partitionCriteria == null) {
      this.partitionCriteria = new ArrayList<PartitioningCriteria>();
    }
    this.partitionCriteria.add(partitionCriteriaItem);
    return this;
  }

  /**
   * Criteria for partitioning the UEs before applying the sampling ratio.
   * @return partitionCriteria
   **/
  @Schema(description = "Criteria for partitioning the UEs before applying the sampling ratio.")
      @Valid
  @Size(min=1)   public List<PartitioningCriteria> getPartitionCriteria() {
    return partitionCriteria;
  }

  public void setPartitionCriteria(List<PartitioningCriteria> partitionCriteria) {
    this.partitionCriteria = partitionCriteria;
  }

  public ReportingInformation grpRepTime(Integer grpRepTime) {
    this.grpRepTime = grpRepTime;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return grpRepTime
   **/
  @Schema(description = "indicating a time in seconds.")
  
    public Integer getGrpRepTime() {
    return grpRepTime;
  }

  public void setGrpRepTime(Integer grpRepTime) {
    this.grpRepTime = grpRepTime;
  }

  public ReportingInformation notifFlag(NotificationFlag notifFlag) {
    this.notifFlag = notifFlag;
    return this;
  }

  /**
   * Get notifFlag
   * @return notifFlag
   **/
  @Schema(description = "")
  
    @Valid
    public NotificationFlag getNotifFlag() {
    return notifFlag;
  }

  public void setNotifFlag(NotificationFlag notifFlag) {
    this.notifFlag = notifFlag;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportingInformation reportingInformation = (ReportingInformation) o;
    return Objects.equals(this.immRep, reportingInformation.immRep) &&
        Objects.equals(this.notifMethod, reportingInformation.notifMethod) &&
        Objects.equals(this.maxReportNbr, reportingInformation.maxReportNbr) &&
        Objects.equals(this.monDur, reportingInformation.monDur) &&
        Objects.equals(this.repPeriod, reportingInformation.repPeriod) &&
        Objects.equals(this.sampRatio, reportingInformation.sampRatio) &&
        Objects.equals(this.partitionCriteria, reportingInformation.partitionCriteria) &&
        Objects.equals(this.grpRepTime, reportingInformation.grpRepTime) &&
        Objects.equals(this.notifFlag, reportingInformation.notifFlag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(immRep, notifMethod, maxReportNbr, monDur, repPeriod, sampRatio, partitionCriteria, grpRepTime, notifFlag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportingInformation {\n");
    
    sb.append("    immRep: ").append(toIndentedString(immRep)).append("\n");
    sb.append("    notifMethod: ").append(toIndentedString(notifMethod)).append("\n");
    sb.append("    maxReportNbr: ").append(toIndentedString(maxReportNbr)).append("\n");
    sb.append("    monDur: ").append(toIndentedString(monDur)).append("\n");
    sb.append("    repPeriod: ").append(toIndentedString(repPeriod)).append("\n");
    sb.append("    sampRatio: ").append(toIndentedString(sampRatio)).append("\n");
    sb.append("    partitionCriteria: ").append(toIndentedString(partitionCriteria)).append("\n");
    sb.append("    grpRepTime: ").append(toIndentedString(grpRepTime)).append("\n");
    sb.append("    notifFlag: ").append(toIndentedString(notifFlag)).append("\n");
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
