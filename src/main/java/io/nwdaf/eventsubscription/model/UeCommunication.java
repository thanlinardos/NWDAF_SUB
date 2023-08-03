package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents UE communication information.
 */
@Schema(description = "Represents UE communication information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class UeCommunication   {
  @JsonProperty("commDur")
  private Integer commDur = null;

  @JsonProperty("commDurVariance")
  private Float commDurVariance = null;

  @JsonProperty("perioTime")
  private Integer perioTime = null;

  @JsonProperty("perioTimeVariance")
  private Float perioTimeVariance = null;

  @JsonProperty("ts")
  private OffsetDateTime ts = null;

  @JsonProperty("tsVariance")
  private Float tsVariance = null;

  @JsonProperty("recurringTime")
  private ScheduledCommunicationTime recurringTime = null;

  @JsonProperty("trafChar")
  private TrafficCharacterization trafChar = null;

  @JsonProperty("ratio")
  private Integer ratio = null;

  @JsonProperty("perioCommInd")
  private Boolean perioCommInd = null;

  @JsonProperty("confidence")
  private Integer confidence = null;

  @JsonProperty("anaOfAppList")
  private AppListForUeComm anaOfAppList = null;

  @JsonProperty("sessInactTimer")
  private SessInactTimerForUeComm sessInactTimer = null;

  public UeCommunication commDur(Integer commDur) {
    this.commDur = commDur;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return commDur
   **/
  @Schema(description = "indicating a time in seconds.")
  
    public Integer getCommDur() {
    return commDur;
  }

  public void setCommDur(Integer commDur) {
    this.commDur = commDur;
  }

  public UeCommunication commDurVariance(Float commDurVariance) {
    this.commDurVariance = commDurVariance;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return commDurVariance
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getCommDurVariance() {
    return commDurVariance;
  }

  public void setCommDurVariance(Float commDurVariance) {
    this.commDurVariance = commDurVariance;
  }

  public UeCommunication perioTime(Integer perioTime) {
    this.perioTime = perioTime;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return perioTime
   **/
  @Schema(description = "indicating a time in seconds.")
  
    public Integer getPerioTime() {
    return perioTime;
  }

  public void setPerioTime(Integer perioTime) {
    this.perioTime = perioTime;
  }

  public UeCommunication perioTimeVariance(Float perioTimeVariance) {
    this.perioTimeVariance = perioTimeVariance;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return perioTimeVariance
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getPerioTimeVariance() {
    return perioTimeVariance;
  }

  public void setPerioTimeVariance(Float perioTimeVariance) {
    this.perioTimeVariance = perioTimeVariance;
  }

  public UeCommunication ts(OffsetDateTime ts) {
    this.ts = ts;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return ts
   **/
  @Schema(description = "string with format 'date-time' as defined in OpenAPI.")
  
    @Valid
    public OffsetDateTime getTs() {
    return ts;
  }

  public void setTs(OffsetDateTime ts) {
    this.ts = ts;
  }

  public UeCommunication tsVariance(Float tsVariance) {
    this.tsVariance = tsVariance;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return tsVariance
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getTsVariance() {
    return tsVariance;
  }

  public void setTsVariance(Float tsVariance) {
    this.tsVariance = tsVariance;
  }

  public UeCommunication recurringTime(ScheduledCommunicationTime recurringTime) {
    this.recurringTime = recurringTime;
    return this;
  }

  /**
   * Get recurringTime
   * @return recurringTime
   **/
  @Schema(description = "")
  
    @Valid
    public ScheduledCommunicationTime getRecurringTime() {
    return recurringTime;
  }

  public void setRecurringTime(ScheduledCommunicationTime recurringTime) {
    this.recurringTime = recurringTime;
  }

  public UeCommunication trafChar(TrafficCharacterization trafChar) {
    this.trafChar = trafChar;
    return this;
  }

  /**
   * Get trafChar
   * @return trafChar
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public TrafficCharacterization getTrafChar() {
    return trafChar;
  }

  public void setTrafChar(TrafficCharacterization trafChar) {
    this.trafChar = trafChar;
  }

  public UeCommunication ratio(Integer ratio) {
    this.ratio = ratio;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return ratio
   **/
  @Schema(description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
  
  @Min(1) @Max(100)   public Integer getRatio() {
    return ratio;
  }

  public void setRatio(Integer ratio) {
    this.ratio = ratio;
  }

  public UeCommunication perioCommInd(Boolean perioCommInd) {
    this.perioCommInd = perioCommInd;
    return this;
  }

  /**
   * Get perioCommInd
   * @return perioCommInd
   **/
  @Schema(description = "")
  
    public Boolean isPerioCommInd() {
    return perioCommInd;
  }

  public void setPerioCommInd(Boolean perioCommInd) {
    this.perioCommInd = perioCommInd;
  }

  public UeCommunication confidence(Integer confidence) {
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

  public UeCommunication anaOfAppList(AppListForUeComm anaOfAppList) {
    this.anaOfAppList = anaOfAppList;
    return this;
  }

  /**
   * Get anaOfAppList
   * @return anaOfAppList
   **/
  @Schema(description = "")
  
    @Valid
    public AppListForUeComm getAnaOfAppList() {
    return anaOfAppList;
  }

  public void setAnaOfAppList(AppListForUeComm anaOfAppList) {
    this.anaOfAppList = anaOfAppList;
  }

  public UeCommunication sessInactTimer(SessInactTimerForUeComm sessInactTimer) {
    this.sessInactTimer = sessInactTimer;
    return this;
  }

  /**
   * Get sessInactTimer
   * @return sessInactTimer
   **/
  @Schema(description = "")
  
    @Valid
    public SessInactTimerForUeComm getSessInactTimer() {
    return sessInactTimer;
  }

  public void setSessInactTimer(SessInactTimerForUeComm sessInactTimer) {
    this.sessInactTimer = sessInactTimer;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UeCommunication ueCommunication = (UeCommunication) o;
    return Objects.equals(this.commDur, ueCommunication.commDur) &&
        Objects.equals(this.commDurVariance, ueCommunication.commDurVariance) &&
        Objects.equals(this.perioTime, ueCommunication.perioTime) &&
        Objects.equals(this.perioTimeVariance, ueCommunication.perioTimeVariance) &&
        Objects.equals(this.ts, ueCommunication.ts) &&
        Objects.equals(this.tsVariance, ueCommunication.tsVariance) &&
        Objects.equals(this.recurringTime, ueCommunication.recurringTime) &&
        Objects.equals(this.trafChar, ueCommunication.trafChar) &&
        Objects.equals(this.ratio, ueCommunication.ratio) &&
        Objects.equals(this.perioCommInd, ueCommunication.perioCommInd) &&
        Objects.equals(this.confidence, ueCommunication.confidence) &&
        Objects.equals(this.anaOfAppList, ueCommunication.anaOfAppList) &&
        Objects.equals(this.sessInactTimer, ueCommunication.sessInactTimer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commDur, commDurVariance, perioTime, perioTimeVariance, ts, tsVariance, recurringTime, trafChar, ratio, perioCommInd, confidence, anaOfAppList, sessInactTimer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UeCommunication {\n");
    
    sb.append("    commDur: ").append(toIndentedString(commDur)).append("\n");
    sb.append("    commDurVariance: ").append(toIndentedString(commDurVariance)).append("\n");
    sb.append("    perioTime: ").append(toIndentedString(perioTime)).append("\n");
    sb.append("    perioTimeVariance: ").append(toIndentedString(perioTimeVariance)).append("\n");
    sb.append("    ts: ").append(toIndentedString(ts)).append("\n");
    sb.append("    tsVariance: ").append(toIndentedString(tsVariance)).append("\n");
    sb.append("    recurringTime: ").append(toIndentedString(recurringTime)).append("\n");
    sb.append("    trafChar: ").append(toIndentedString(trafChar)).append("\n");
    sb.append("    ratio: ").append(toIndentedString(ratio)).append("\n");
    sb.append("    perioCommInd: ").append(toIndentedString(perioCommInd)).append("\n");
    sb.append("    confidence: ").append(toIndentedString(confidence)).append("\n");
    sb.append("    anaOfAppList: ").append(toIndentedString(anaOfAppList)).append("\n");
    sb.append("    sessInactTimer: ").append(toIndentedString(sessInactTimer)).append("\n");
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
