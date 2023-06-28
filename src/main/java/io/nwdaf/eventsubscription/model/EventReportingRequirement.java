package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.Accuracy;
import io.nwdaf.eventsubscription.model.AnalyticsMetadata;
import io.nwdaf.eventsubscription.model.AnalyticsMetadataIndication;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents the type of reporting that the subscription requires.
 */
@Schema(description = "Represents the type of reporting that the subscription requires.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class EventReportingRequirement   {
  @JsonProperty("accuracy")
  private Accuracy accuracy = null;

  @JsonProperty("accPerSubset")
  @Valid
  private List<Accuracy> accPerSubset = null;
  
  @JsonProperty("startTs")
  @DateTimeFormat
  private OffsetDateTime startTs = null;

  @JsonProperty("endTs")
  @DateTimeFormat
  private OffsetDateTime endTs = null;

  @JsonProperty("offsetPeriod")
  private Integer offsetPeriod = null;

  @JsonProperty("sampRatio")
  private Integer sampRatio = null;

  @JsonProperty("maxObjectNbr")
  private Integer maxObjectNbr = null;

  @JsonProperty("maxSupiNbr")
  private Integer maxSupiNbr = null;

  @JsonProperty("timeAnaNeeded")
  @DateTimeFormat
  private OffsetDateTime timeAnaNeeded = null;

  @JsonProperty("anaMeta")
  @Valid
  private List<AnalyticsMetadata> anaMeta = null;

  @JsonProperty("anaMetaInd")
  private AnalyticsMetadataIndication anaMetaInd = null;

  public EventReportingRequirement accuracy(Accuracy accuracy) {
    this.accuracy = accuracy;
    return this;
  }

  /**
   * Get accuracy
   * @return accuracy
   **/
  @Schema(description = "")
  
    @Valid
    public Accuracy getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(Accuracy accuracy) {
    this.accuracy = accuracy;
  }

  public EventReportingRequirement accPerSubset(List<Accuracy> accPerSubset) {
    this.accPerSubset = accPerSubset;
    return this;
  }

  public EventReportingRequirement addAccPerSubsetItem(Accuracy accPerSubsetItem) {
    if (this.accPerSubset == null) {
      this.accPerSubset = new ArrayList<Accuracy>();
    }
    this.accPerSubset.add(accPerSubsetItem);
    return this;
  }

  /**
   * Each element indicates the preferred accuracy level per analytics subset. It may be present if the \"listOfAnaSubsets\" attribute is present in the subscription request when the subscription event is NF_LOAD, UE_COMM, DISPERSION, NETWORK_PERFORMANCE, WLAN_PERFORMANCE, DN_PERFORMANCE or SERVICE_EXPERIENCE. 
   * @return accPerSubset
   **/
  @Schema(description = "Each element indicates the preferred accuracy level per analytics subset. It may be present if the \"listOfAnaSubsets\" attribute is present in the subscription request when the subscription event is NF_LOAD, UE_COMM, DISPERSION, NETWORK_PERFORMANCE, WLAN_PERFORMANCE, DN_PERFORMANCE or SERVICE_EXPERIENCE. ")
      @Valid
  @Size(min=1)   public List<Accuracy> getAccPerSubset() {
    return accPerSubset;
  }

  public void setAccPerSubset(List<Accuracy> accPerSubset) {
    this.accPerSubset = accPerSubset;
  }

  public EventReportingRequirement startTs(OffsetDateTime startTs) {
    this.startTs = startTs;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return startTs
   **/
  @Schema(description = "string with format 'date-time' as defined in OpenAPI.")
  
    @Valid
    public OffsetDateTime getStartTs() {
    return startTs;
  }

  public void setStartTs(OffsetDateTime startTs) {
    this.startTs = startTs;
  }

  public EventReportingRequirement endTs(OffsetDateTime endTs) {
    this.endTs = endTs;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return endTs
   **/
  @Schema(description = "string with format 'date-time' as defined in OpenAPI.")
  
    @Valid
    public OffsetDateTime getEndTs() {
    return endTs;
  }

  public void setEndTs(OffsetDateTime endTs) {
    this.endTs = endTs;
  }

  public EventReportingRequirement offsetPeriod(Integer offsetPeriod) {
    this.offsetPeriod = offsetPeriod;
    return this;
  }

  /**
   * Offset period in units of seconds to the reporting time, if the value is negative means  statistics in the past offset period, otherwise a positive value means prediction in the  future offset period. May be present if the \"repPeriod\" attribute is included within the  \"evtReq\" attribute. 
   * @return offsetPeriod
   **/
  @Schema(description = "Offset period in units of seconds to the reporting time, if the value is negative means  statistics in the past offset period, otherwise a positive value means prediction in the  future offset period. May be present if the \"repPeriod\" attribute is included within the  \"evtReq\" attribute. ")
  
    public Integer getOffsetPeriod() {
    return offsetPeriod;
  }

  public void setOffsetPeriod(Integer offsetPeriod) {
    this.offsetPeriod = offsetPeriod;
  }

  public EventReportingRequirement sampRatio(Integer sampRatio) {
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

  public EventReportingRequirement maxObjectNbr(Integer maxObjectNbr) {
    this.maxObjectNbr = maxObjectNbr;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return maxObjectNbr
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getMaxObjectNbr() {
    return maxObjectNbr;
  }

  public void setMaxObjectNbr(Integer maxObjectNbr) {
    this.maxObjectNbr = maxObjectNbr;
  }

  public EventReportingRequirement maxSupiNbr(Integer maxSupiNbr) {
    this.maxSupiNbr = maxSupiNbr;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return maxSupiNbr
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getMaxSupiNbr() {
    return maxSupiNbr;
  }

  public void setMaxSupiNbr(Integer maxSupiNbr) {
    this.maxSupiNbr = maxSupiNbr;
  }

  public EventReportingRequirement timeAnaNeeded(OffsetDateTime timeAnaNeeded) {
    this.timeAnaNeeded = timeAnaNeeded;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return timeAnaNeeded
   **/
  @Schema(description = "string with format 'date-time' as defined in OpenAPI.")
  
    @Valid
    public OffsetDateTime getTimeAnaNeeded() {
    return timeAnaNeeded;
  }

  public void setTimeAnaNeeded(OffsetDateTime timeAnaNeeded) {
    this.timeAnaNeeded = timeAnaNeeded;
  }

  public EventReportingRequirement anaMeta(List<AnalyticsMetadata> anaMeta) {
    this.anaMeta = anaMeta;
    return this;
  }

  public EventReportingRequirement addAnaMetaItem(AnalyticsMetadata anaMetaItem) {
    if (this.anaMeta == null) {
      this.anaMeta = new ArrayList<AnalyticsMetadata>();
    }
    this.anaMeta.add(anaMetaItem);
    return this;
  }

  /**
   * Get anaMeta
   * @return anaMeta
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<AnalyticsMetadata> getAnaMeta() {
    return anaMeta;
  }

  public void setAnaMeta(List<AnalyticsMetadata> anaMeta) {
    this.anaMeta = anaMeta;
  }

  public EventReportingRequirement anaMetaInd(AnalyticsMetadataIndication anaMetaInd) {
    this.anaMetaInd = anaMetaInd;
    return this;
  }

  /**
   * Get anaMetaInd
   * @return anaMetaInd
   **/
  @Schema(description = "")
  
    @Valid
    public AnalyticsMetadataIndication getAnaMetaInd() {
    return anaMetaInd;
  }

  public void setAnaMetaInd(AnalyticsMetadataIndication anaMetaInd) {
    this.anaMetaInd = anaMetaInd;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventReportingRequirement eventReportingRequirement = (EventReportingRequirement) o;
    return Objects.equals(this.accuracy, eventReportingRequirement.accuracy) &&
        Objects.equals(this.accPerSubset, eventReportingRequirement.accPerSubset) &&
        Objects.equals(this.startTs, eventReportingRequirement.startTs) &&
        Objects.equals(this.endTs, eventReportingRequirement.endTs) &&
        Objects.equals(this.offsetPeriod, eventReportingRequirement.offsetPeriod) &&
        Objects.equals(this.sampRatio, eventReportingRequirement.sampRatio) &&
        Objects.equals(this.maxObjectNbr, eventReportingRequirement.maxObjectNbr) &&
        Objects.equals(this.maxSupiNbr, eventReportingRequirement.maxSupiNbr) &&
        Objects.equals(this.timeAnaNeeded, eventReportingRequirement.timeAnaNeeded) &&
        Objects.equals(this.anaMeta, eventReportingRequirement.anaMeta) &&
        Objects.equals(this.anaMetaInd, eventReportingRequirement.anaMetaInd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accuracy, accPerSubset, startTs, endTs, offsetPeriod, sampRatio, maxObjectNbr, maxSupiNbr, timeAnaNeeded, anaMeta, anaMetaInd);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventReportingRequirement {\n");
    
    sb.append("    accuracy: ").append(toIndentedString(accuracy)).append("\n");
    sb.append("    accPerSubset: ").append(toIndentedString(accPerSubset)).append("\n");
    sb.append("    startTs: ").append(toIndentedString(startTs)).append("\n");
    sb.append("    endTs: ").append(toIndentedString(endTs)).append("\n");
    sb.append("    offsetPeriod: ").append(toIndentedString(offsetPeriod)).append("\n");
    sb.append("    sampRatio: ").append(toIndentedString(sampRatio)).append("\n");
    sb.append("    maxObjectNbr: ").append(toIndentedString(maxObjectNbr)).append("\n");
    sb.append("    maxSupiNbr: ").append(toIndentedString(maxSupiNbr)).append("\n");
    sb.append("    timeAnaNeeded: ").append(toIndentedString(timeAnaNeeded)).append("\n");
    sb.append("    anaMeta: ").append(toIndentedString(anaMeta)).append("\n");
    sb.append("    anaMetaInd: ").append(toIndentedString(anaMetaInd)).append("\n");
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
