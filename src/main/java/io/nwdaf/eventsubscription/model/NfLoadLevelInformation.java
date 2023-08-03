package io.nwdaf.eventsubscription.model;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents load level information of a given NF instance.
 */
@Schema(description = "Represents load level information of a given NF instance.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")

public class NfLoadLevelInformation   {
  private Instant time;
  
  private OffsetDateTime timeStamp=null;
  
  @JsonProperty("nfType")
  private NFType nfType = null;

  @JsonProperty("nfInstanceId")
  private UUID nfInstanceId = null;

  @JsonProperty("nfSetId")
  private String nfSetId = null;

  @JsonProperty("nfStatus")
  private NfStatus nfStatus = null;

  @JsonProperty("nfCpuUsage")
  private Integer nfCpuUsage = null;

  @JsonProperty("nfMemoryUsage")
  private Integer nfMemoryUsage = null;

  @JsonProperty("nfStorageUsage")
  private Integer nfStorageUsage = null;

  @JsonProperty("nfLoadLevelAverage")
  private Integer nfLoadLevelAverage = null;

  @JsonProperty("nfLoadLevelpeak")
  private Integer nfLoadLevelpeak = null;

  @JsonProperty("nfLoadAvgInAoi")
  private Integer nfLoadAvgInAoi = null;

  @JsonProperty("snssai")
  private Snssai snssai = null;

  @JsonProperty("confidence")
  private Integer confidence = null;

  public NfLoadLevelInformation nfType(NFType nfType) {
    this.nfType = nfType;
    return this;
  }

  	
  public Instant getTime() {
	return time;
  }
  public void setTime(Instant time) {
	this.time = time;
	this.timeStamp = OffsetDateTime.ofInstant(time, TimeZone.getDefault().toZoneId());
  }
  public NfLoadLevelInformation time(Instant time) {
	this.time = time;
	this.timeStamp = OffsetDateTime.ofInstant(time, TimeZone.getDefault().toZoneId());
    return this;
  }
  /**
   * Get nfType
   * @return nfType
   **/
  @Schema(description = "")
  
    @Valid
    public NFType getNfType() {
    return nfType;
  }

  public void setNfType(NFType nfType) {
    this.nfType = nfType;
  }

  public NfLoadLevelInformation nfInstanceId(UUID nfInstanceId) {
    this.nfInstanceId = nfInstanceId;
    return this;
  }

  /**
   * String uniquely identifying a NF instance. The format of the NF Instance ID shall be a  Universally Unique Identifier (UUID) version 4, as described in IETF RFC 4122.  
   * @return nfInstanceId
   **/
  @Schema(required = true, description = "String uniquely identifying a NF instance. The format of the NF Instance ID shall be a  Universally Unique Identifier (UUID) version 4, as described in IETF RFC 4122.  ")
      @NotNull

    @Valid
    public UUID getNfInstanceId() {
    return nfInstanceId;
  }

  public void setNfInstanceId(UUID nfInstanceId) {
    this.nfInstanceId = nfInstanceId;
  }

  public NfLoadLevelInformation nfSetId(String nfSetId) {
    this.nfSetId = nfSetId;
    return this;
  }

  /**
   * NF Set Identifier (see clause 28.12 of 3GPP TS 23.003), formatted as the following string \"set<Set ID>.<nftype>set.5gc.mnc<MNC>.mcc<MCC>\", or  \"set<SetID>.<NFType>set.5gc.nid<NID>.mnc<MNC>.mcc<MCC>\" with  <MCC> encoded as defined in clause 5.4.2 (\"Mcc\" data type definition)  <MNC> encoding the Mobile Network Code part of the PLMN, comprising 3 digits.    If there are only 2 significant digits in the MNC, one \"0\" digit shall be inserted    at the left side to fill the 3 digits coding of MNC.  Pattern: '^[0-9]{3}$' <NFType> encoded as a value defined in Table 6.1.6.3.3-1 of 3GPP TS 29.510 but    with lower case characters <Set ID> encoded as a string of characters consisting of    alphabetic characters (A-Z and a-z), digits (0-9) and/or the hyphen (-) and that    shall end with either an alphabetic character or a digit.  
   * @return nfSetId
   **/
  @Schema(description = "NF Set Identifier (see clause 28.12 of 3GPP TS 23.003), formatted as the following string \"set<Set ID>.<nftype>set.5gc.mnc<MNC>.mcc<MCC>\", or  \"set<SetID>.<NFType>set.5gc.nid<NID>.mnc<MNC>.mcc<MCC>\" with  <MCC> encoded as defined in clause 5.4.2 (\"Mcc\" data type definition)  <MNC> encoding the Mobile Network Code part of the PLMN, comprising 3 digits.    If there are only 2 significant digits in the MNC, one \"0\" digit shall be inserted    at the left side to fill the 3 digits coding of MNC.  Pattern: '^[0-9]{3}$' <NFType> encoded as a value defined in Table 6.1.6.3.3-1 of 3GPP TS 29.510 but    with lower case characters <Set ID> encoded as a string of characters consisting of    alphabetic characters (A-Z and a-z), digits (0-9) and/or the hyphen (-) and that    shall end with either an alphabetic character or a digit.  ")
  
    public String getNfSetId() {
    return nfSetId;
  }

  public void setNfSetId(String nfSetId) {
    this.nfSetId = nfSetId;
  }

  public NfLoadLevelInformation nfStatus(NfStatus nfStatus) {
    this.nfStatus = nfStatus;
    return this;
  }

  /**
   * Get nfStatus
   * @return nfStatus
   **/
  @Schema(description = "")
  
    @Valid
    public NfStatus getNfStatus() {
    return nfStatus;
  }

  public void setNfStatus(NfStatus nfStatus) {
    this.nfStatus = nfStatus;
  }

  public NfLoadLevelInformation nfCpuUsage(Integer nfCpuUsage) {
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

  public NfLoadLevelInformation nfMemoryUsage(Integer nfMemoryUsage) {
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

  public NfLoadLevelInformation nfStorageUsage(Integer nfStorageUsage) {
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

  public NfLoadLevelInformation nfLoadLevelAverage(Integer nfLoadLevelAverage) {
    this.nfLoadLevelAverage = nfLoadLevelAverage;
    return this;
  }

  /**
   * Get nfLoadLevelAverage
   * @return nfLoadLevelAverage
   **/
  @Schema(description = "")
  
    public Integer getNfLoadLevelAverage() {
    return nfLoadLevelAverage;
  }

  public void setNfLoadLevelAverage(Integer nfLoadLevelAverage) {
    this.nfLoadLevelAverage = nfLoadLevelAverage;
  }

  public NfLoadLevelInformation nfLoadLevelpeak(Integer nfLoadLevelpeak) {
    this.nfLoadLevelpeak = nfLoadLevelpeak;
    return this;
  }

  /**
   * Get nfLoadLevelpeak
   * @return nfLoadLevelpeak
   **/
  @Schema(description = "")
  
    public Integer getNfLoadLevelpeak() {
    return nfLoadLevelpeak;
  }

  public void setNfLoadLevelpeak(Integer nfLoadLevelpeak) {
    this.nfLoadLevelpeak = nfLoadLevelpeak;
  }

  public NfLoadLevelInformation nfLoadAvgInAoi(Integer nfLoadAvgInAoi) {
    this.nfLoadAvgInAoi = nfLoadAvgInAoi;
    return this;
  }

  /**
   * Get nfLoadAvgInAoi
   * @return nfLoadAvgInAoi
   **/
  @Schema(description = "")
  
    public Integer getNfLoadAvgInAoi() {
    return nfLoadAvgInAoi;
  }

  public void setNfLoadAvgInAoi(Integer nfLoadAvgInAoi) {
    this.nfLoadAvgInAoi = nfLoadAvgInAoi;
  }

  public NfLoadLevelInformation snssai(Snssai snssai) {
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

  public NfLoadLevelInformation confidence(Integer confidence) {
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
    NfLoadLevelInformation nfLoadLevelInformation = (NfLoadLevelInformation) o;
    return Objects.equals(this.nfType, nfLoadLevelInformation.nfType) &&
        Objects.equals(this.nfInstanceId, nfLoadLevelInformation.nfInstanceId) &&
        Objects.equals(this.nfSetId, nfLoadLevelInformation.nfSetId) &&
        Objects.equals(this.nfStatus, nfLoadLevelInformation.nfStatus) &&
        Objects.equals(this.nfCpuUsage, nfLoadLevelInformation.nfCpuUsage) &&
        Objects.equals(this.nfMemoryUsage, nfLoadLevelInformation.nfMemoryUsage) &&
        Objects.equals(this.nfStorageUsage, nfLoadLevelInformation.nfStorageUsage) &&
        Objects.equals(this.nfLoadLevelAverage, nfLoadLevelInformation.nfLoadLevelAverage) &&
        Objects.equals(this.nfLoadLevelpeak, nfLoadLevelInformation.nfLoadLevelpeak) &&
        Objects.equals(this.nfLoadAvgInAoi, nfLoadLevelInformation.nfLoadAvgInAoi) &&
        Objects.equals(this.snssai, nfLoadLevelInformation.snssai) &&
        Objects.equals(this.confidence, nfLoadLevelInformation.confidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nfType, nfInstanceId, nfSetId, nfStatus, nfCpuUsage, nfMemoryUsage, nfStorageUsage, nfLoadLevelAverage, nfLoadLevelpeak, nfLoadAvgInAoi, snssai, confidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NfLoadLevelInformation {\n");
    
    sb.append("    nfType: ").append(toIndentedString(nfType)).append("\n");
    sb.append("    nfInstanceId: ").append(toIndentedString(nfInstanceId)).append("\n");
    sb.append("    nfSetId: ").append(toIndentedString(nfSetId)).append("\n");
    sb.append("    nfStatus: ").append(toIndentedString(nfStatus)).append("\n");
    sb.append("    nfCpuUsage: ").append(toIndentedString(nfCpuUsage)).append("\n");
    sb.append("    nfMemoryUsage: ").append(toIndentedString(nfMemoryUsage)).append("\n");
    sb.append("    nfStorageUsage: ").append(toIndentedString(nfStorageUsage)).append("\n");
    sb.append("    nfLoadLevelAverage: ").append(toIndentedString(nfLoadLevelAverage)).append("\n");
    sb.append("    nfLoadLevelpeak: ").append(toIndentedString(nfLoadLevelpeak)).append("\n");
    sb.append("    nfLoadAvgInAoi: ").append(toIndentedString(nfLoadAvgInAoi)).append("\n");
    sb.append("    snssai: ").append(toIndentedString(snssai)).append("\n");
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


public OffsetDateTime getTimeStamp() {
	return timeStamp;
}


public void setTimeStamp(OffsetDateTime timeStamp) {
	this.timeStamp = timeStamp;
}
}
