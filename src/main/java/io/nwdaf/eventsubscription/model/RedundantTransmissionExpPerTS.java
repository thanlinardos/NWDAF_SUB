package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.ObservedRedundantTransExp;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * The redundant transmission experience per Time Slot.
 */
@Schema(description = "The redundant transmission experience per Time Slot.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class RedundantTransmissionExpPerTS   {
  @JsonProperty("tsStart")
  private OffsetDateTime tsStart = null;

  @JsonProperty("tsDuration")
  private Integer tsDuration = null;

  @JsonProperty("obsvRedTransExp")
  private ObservedRedundantTransExp obsvRedTransExp = null;

  @JsonProperty("redTransStatus")
  private Boolean redTransStatus = null;

  @JsonProperty("ueRatio")
  private Integer ueRatio = null;

  @JsonProperty("confidence")
  private Integer confidence = null;

  public RedundantTransmissionExpPerTS tsStart(OffsetDateTime tsStart) {
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

  public RedundantTransmissionExpPerTS tsDuration(Integer tsDuration) {
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

  public RedundantTransmissionExpPerTS obsvRedTransExp(ObservedRedundantTransExp obsvRedTransExp) {
    this.obsvRedTransExp = obsvRedTransExp;
    return this;
  }

  /**
   * Get obsvRedTransExp
   * @return obsvRedTransExp
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public ObservedRedundantTransExp getObsvRedTransExp() {
    return obsvRedTransExp;
  }

  public void setObsvRedTransExp(ObservedRedundantTransExp obsvRedTransExp) {
    this.obsvRedTransExp = obsvRedTransExp;
  }

  public RedundantTransmissionExpPerTS redTransStatus(Boolean redTransStatus) {
    this.redTransStatus = redTransStatus;
    return this;
  }

  /**
   * Redundant Transmission Status. Set to \"true\" if redundant transmission was activated, otherwise set to \"false\". Default value is \"false\" if omitted. 
   * @return redTransStatus
   **/
  @Schema(description = "Redundant Transmission Status. Set to \"true\" if redundant transmission was activated, otherwise set to \"false\". Default value is \"false\" if omitted. ")
  
    public Boolean isRedTransStatus() {
    return redTransStatus;
  }

  public void setRedTransStatus(Boolean redTransStatus) {
    this.redTransStatus = redTransStatus;
  }

  public RedundantTransmissionExpPerTS ueRatio(Integer ueRatio) {
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

  public RedundantTransmissionExpPerTS confidence(Integer confidence) {
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
    RedundantTransmissionExpPerTS redundantTransmissionExpPerTS = (RedundantTransmissionExpPerTS) o;
    return Objects.equals(this.tsStart, redundantTransmissionExpPerTS.tsStart) &&
        Objects.equals(this.tsDuration, redundantTransmissionExpPerTS.tsDuration) &&
        Objects.equals(this.obsvRedTransExp, redundantTransmissionExpPerTS.obsvRedTransExp) &&
        Objects.equals(this.redTransStatus, redundantTransmissionExpPerTS.redTransStatus) &&
        Objects.equals(this.ueRatio, redundantTransmissionExpPerTS.ueRatio) &&
        Objects.equals(this.confidence, redundantTransmissionExpPerTS.confidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tsStart, tsDuration, obsvRedTransExp, redTransStatus, ueRatio, confidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RedundantTransmissionExpPerTS {\n");
    
    sb.append("    tsStart: ").append(toIndentedString(tsStart)).append("\n");
    sb.append("    tsDuration: ").append(toIndentedString(tsDuration)).append("\n");
    sb.append("    obsvRedTransExp: ").append(toIndentedString(obsvRedTransExp)).append("\n");
    sb.append("    redTransStatus: ").append(toIndentedString(redTransStatus)).append("\n");
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
