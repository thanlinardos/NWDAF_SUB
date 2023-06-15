package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NetworkPerfType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents the network performance information.
 */
@Schema(description = "Represents the network performance information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class NetworkPerfInfo   {
  @JsonProperty("networkArea")
  private NetworkAreaInfo networkArea = null;

  @JsonProperty("nwPerfType")
  private NetworkPerfType nwPerfType = null;

  @JsonProperty("relativeRatio")
  private Integer relativeRatio = null;

  @JsonProperty("absoluteNum")
  private Integer absoluteNum = null;

  @JsonProperty("confidence")
  private Integer confidence = null;

  public NetworkPerfInfo networkArea(NetworkAreaInfo networkArea) {
    this.networkArea = networkArea;
    return this;
  }

  /**
   * Get networkArea
   * @return networkArea
   **/
  @Schema(description = "")
  
    @Valid
    public NetworkAreaInfo getNetworkArea() {
    return networkArea;
  }

  public void setNetworkArea(NetworkAreaInfo networkArea) {
    this.networkArea = networkArea;
  }

  public NetworkPerfInfo nwPerfType(NetworkPerfType nwPerfType) {
    this.nwPerfType = nwPerfType;
    return this;
  }

  /**
   * Get nwPerfType
   * @return nwPerfType
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public NetworkPerfType getNwPerfType() {
    return nwPerfType;
  }

  public void setNwPerfType(NetworkPerfType nwPerfType) {
    this.nwPerfType = nwPerfType;
  }

  public NetworkPerfInfo relativeRatio(Integer relativeRatio) {
    this.relativeRatio = relativeRatio;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return relativeRatio
   **/
  @Schema(description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
  
  @Min(1) @Max(100)   public Integer getRelativeRatio() {
    return relativeRatio;
  }

  public void setRelativeRatio(Integer relativeRatio) {
    this.relativeRatio = relativeRatio;
  }

  public NetworkPerfInfo absoluteNum(Integer absoluteNum) {
    this.absoluteNum = absoluteNum;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return absoluteNum
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getAbsoluteNum() {
    return absoluteNum;
  }

  public void setAbsoluteNum(Integer absoluteNum) {
    this.absoluteNum = absoluteNum;
  }

  public NetworkPerfInfo confidence(Integer confidence) {
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
    NetworkPerfInfo networkPerfInfo = (NetworkPerfInfo) o;
    return Objects.equals(this.networkArea, networkPerfInfo.networkArea) &&
        Objects.equals(this.nwPerfType, networkPerfInfo.nwPerfType) &&
        Objects.equals(this.relativeRatio, networkPerfInfo.relativeRatio) &&
        Objects.equals(this.absoluteNum, networkPerfInfo.absoluteNum) &&
        Objects.equals(this.confidence, networkPerfInfo.confidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkArea, nwPerfType, relativeRatio, absoluteNum, confidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NetworkPerfInfo {\n");
    
    sb.append("    networkArea: ").append(toIndentedString(networkArea)).append("\n");
    sb.append("    nwPerfType: ").append(toIndentedString(nwPerfType)).append("\n");
    sb.append("    relativeRatio: ").append(toIndentedString(relativeRatio)).append("\n");
    sb.append("    absoluteNum: ").append(toIndentedString(absoluteNum)).append("\n");
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
