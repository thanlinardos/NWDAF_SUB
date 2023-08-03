package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents the RAT type and/or Frequency information.
 */
@Schema(description = "Represents the RAT type and/or Frequency information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class RatFreqInformation   {
  @JsonProperty("allFreq")
  private Boolean allFreq = null;

  @JsonProperty("allRat")
  private Boolean allRat = null;

  @JsonProperty("freq")
  private Integer freq = null;

  @JsonProperty("ratType")
  private RatType ratType = null;

  @JsonProperty("svcExpThreshold")
  private ThresholdLevel svcExpThreshold = null;

  @JsonProperty("matchingDir")
  private MatchingDirection matchingDir = null;

  public RatFreqInformation allFreq(Boolean allFreq) {
    this.allFreq = allFreq;
    return this;
  }

  /**
   * Set to \"true\" to indicate to handle all the frequencies the NWDAF received, otherwise  set to \"false\" or omit. The \"allFreq\" attribute and the \"freq\" attribute are mutually  exclusive. 
   * @return allFreq
   **/
  @Schema(description = "Set to \"true\" to indicate to handle all the frequencies the NWDAF received, otherwise  set to \"false\" or omit. The \"allFreq\" attribute and the \"freq\" attribute are mutually  exclusive. ")
  
    public Boolean isAllFreq() {
    return allFreq;
  }

  public void setAllFreq(Boolean allFreq) {
    this.allFreq = allFreq;
  }

  public RatFreqInformation allRat(Boolean allRat) {
    this.allRat = allRat;
    return this;
  }

  /**
   * Set to \"true\" to indicate to handle all the RAT Types the NWDAF received, otherwise  set to \"false\" or omit. The \"allRat\" attribute and the \"ratType\" attribute are mutually  exclusive. 
   * @return allRat
   **/
  @Schema(description = "Set to \"true\" to indicate to handle all the RAT Types the NWDAF received, otherwise  set to \"false\" or omit. The \"allRat\" attribute and the \"ratType\" attribute are mutually  exclusive. ")
  
    public Boolean isAllRat() {
    return allRat;
  }

  public void setAllRat(Boolean allRat) {
    this.allRat = allRat;
  }

  public RatFreqInformation freq(Integer freq) {
    this.freq = freq;
    return this;
  }

  /**
   * Integer value indicating the ARFCN applicable for a downlink, uplink or bi-directional (TDD) NR global frequency raster, as definition of \"ARFCN-ValueNR\" IE in clause 6.3.2 of 3GPP TS 38.331. 
   * minimum: 0
   * maximum: 3279165
   * @return freq
   **/
  @Schema(description = "Integer value indicating the ARFCN applicable for a downlink, uplink or bi-directional (TDD) NR global frequency raster, as definition of \"ARFCN-ValueNR\" IE in clause 6.3.2 of 3GPP TS 38.331. ")
  
  @Min(0) @Max(3279165)   public Integer getFreq() {
    return freq;
  }

  public void setFreq(Integer freq) {
    this.freq = freq;
  }

  public RatFreqInformation ratType(RatType ratType) {
    this.ratType = ratType;
    return this;
  }

  /**
   * Get ratType
   * @return ratType
   **/
  @Schema(description = "")
  
    @Valid
    public RatType getRatType() {
    return ratType;
  }

  public void setRatType(RatType ratType) {
    this.ratType = ratType;
  }

  public RatFreqInformation svcExpThreshold(ThresholdLevel svcExpThreshold) {
    this.svcExpThreshold = svcExpThreshold;
    return this;
  }

  /**
   * Get svcExpThreshold
   * @return svcExpThreshold
   **/
  @Schema(description = "")
  
    @Valid
    public ThresholdLevel getSvcExpThreshold() {
    return svcExpThreshold;
  }

  public void setSvcExpThreshold(ThresholdLevel svcExpThreshold) {
    this.svcExpThreshold = svcExpThreshold;
  }

  public RatFreqInformation matchingDir(MatchingDirection matchingDir) {
    this.matchingDir = matchingDir;
    return this;
  }

  /**
   * Get matchingDir
   * @return matchingDir
   **/
  @Schema(description = "")
  
    @Valid
    public MatchingDirection getMatchingDir() {
    return matchingDir;
  }

  public void setMatchingDir(MatchingDirection matchingDir) {
    this.matchingDir = matchingDir;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatFreqInformation ratFreqInformation = (RatFreqInformation) o;
    return Objects.equals(this.allFreq, ratFreqInformation.allFreq) &&
        Objects.equals(this.allRat, ratFreqInformation.allRat) &&
        Objects.equals(this.freq, ratFreqInformation.freq) &&
        Objects.equals(this.ratType, ratFreqInformation.ratType) &&
        Objects.equals(this.svcExpThreshold, ratFreqInformation.svcExpThreshold) &&
        Objects.equals(this.matchingDir, ratFreqInformation.matchingDir);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allFreq, allRat, freq, ratType, svcExpThreshold, matchingDir);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatFreqInformation {\n");
    
    sb.append("    allFreq: ").append(toIndentedString(allFreq)).append("\n");
    sb.append("    allRat: ").append(toIndentedString(allRat)).append("\n");
    sb.append("    freq: ").append(toIndentedString(freq)).append("\n");
    sb.append("    ratType: ").append(toIndentedString(ratType)).append("\n");
    sb.append("    svcExpThreshold: ").append(toIndentedString(svcExpThreshold)).append("\n");
    sb.append("    matchingDir: ").append(toIndentedString(matchingDir)).append("\n");
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
