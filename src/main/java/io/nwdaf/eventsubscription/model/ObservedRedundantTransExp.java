package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Represents the observed redundant transmission experience related information.
 */
@Schema(description = "Represents the observed redundant transmission experience related information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ObservedRedundantTransExp   {
  @JsonProperty("avgPktDropRateUl")
  private Integer avgPktDropRateUl = null;

  @JsonProperty("varPktDropRateUl")
  private Float varPktDropRateUl = null;

  @JsonProperty("avgPktDropRateDl")
  private Integer avgPktDropRateDl = null;

  @JsonProperty("varPktDropRateDl")
  private Float varPktDropRateDl = null;

  @JsonProperty("avgPktDelayUl")
  private Integer avgPktDelayUl = null;

  @JsonProperty("varPktDelayUl")
  private Float varPktDelayUl = null;

  @JsonProperty("avgPktDelayDl")
  private Integer avgPktDelayDl = null;

  @JsonProperty("varPktDelayDl")
  private Float varPktDelayDl = null;

  public ObservedRedundantTransExp avgPktDropRateUl(Integer avgPktDropRateUl) {
    this.avgPktDropRateUl = avgPktDropRateUl;
    return this;
  }

  /**
   * Unsigned integer indicating Packet Loss Rate (see clauses 5.7.2.8 and 5.7.4 of 3GPP TS 23.501), expressed in tenth of percent. 
   * minimum: 0
   * maximum: 1000
   * @return avgPktDropRateUl
   **/
  @Schema(description = "Unsigned integer indicating Packet Loss Rate (see clauses 5.7.2.8 and 5.7.4 of 3GPP TS 23.501), expressed in tenth of percent. ")
  
  @Min(0) @Max(1000)   public Integer getAvgPktDropRateUl() {
    return avgPktDropRateUl;
  }

  public void setAvgPktDropRateUl(Integer avgPktDropRateUl) {
    this.avgPktDropRateUl = avgPktDropRateUl;
  }

  public ObservedRedundantTransExp varPktDropRateUl(Float varPktDropRateUl) {
    this.varPktDropRateUl = varPktDropRateUl;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return varPktDropRateUl
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getVarPktDropRateUl() {
    return varPktDropRateUl;
  }

  public void setVarPktDropRateUl(Float varPktDropRateUl) {
    this.varPktDropRateUl = varPktDropRateUl;
  }

  public ObservedRedundantTransExp avgPktDropRateDl(Integer avgPktDropRateDl) {
    this.avgPktDropRateDl = avgPktDropRateDl;
    return this;
  }

  /**
   * Unsigned integer indicating Packet Loss Rate (see clauses 5.7.2.8 and 5.7.4 of 3GPP TS 23.501), expressed in tenth of percent. 
   * minimum: 0
   * maximum: 1000
   * @return avgPktDropRateDl
   **/
  @Schema(description = "Unsigned integer indicating Packet Loss Rate (see clauses 5.7.2.8 and 5.7.4 of 3GPP TS 23.501), expressed in tenth of percent. ")
  
  @Min(0) @Max(1000)   public Integer getAvgPktDropRateDl() {
    return avgPktDropRateDl;
  }

  public void setAvgPktDropRateDl(Integer avgPktDropRateDl) {
    this.avgPktDropRateDl = avgPktDropRateDl;
  }

  public ObservedRedundantTransExp varPktDropRateDl(Float varPktDropRateDl) {
    this.varPktDropRateDl = varPktDropRateDl;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return varPktDropRateDl
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getVarPktDropRateDl() {
    return varPktDropRateDl;
  }

  public void setVarPktDropRateDl(Float varPktDropRateDl) {
    this.varPktDropRateDl = varPktDropRateDl;
  }

  public ObservedRedundantTransExp avgPktDelayUl(Integer avgPktDelayUl) {
    this.avgPktDelayUl = avgPktDelayUl;
    return this;
  }

  /**
   * Unsigned integer indicating Packet Delay Budget (see clauses 5.7.3.4 and 5.7.4 of 3GPP TS 23.501), expressed in milliseconds. 
   * minimum: 1
   * @return avgPktDelayUl
   **/
  @Schema(description = "Unsigned integer indicating Packet Delay Budget (see clauses 5.7.3.4 and 5.7.4 of 3GPP TS 23.501), expressed in milliseconds. ")
  
  @Min(1)  public Integer getAvgPktDelayUl() {
    return avgPktDelayUl;
  }

  public void setAvgPktDelayUl(Integer avgPktDelayUl) {
    this.avgPktDelayUl = avgPktDelayUl;
  }

  public ObservedRedundantTransExp varPktDelayUl(Float varPktDelayUl) {
    this.varPktDelayUl = varPktDelayUl;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return varPktDelayUl
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getVarPktDelayUl() {
    return varPktDelayUl;
  }

  public void setVarPktDelayUl(Float varPktDelayUl) {
    this.varPktDelayUl = varPktDelayUl;
  }

  public ObservedRedundantTransExp avgPktDelayDl(Integer avgPktDelayDl) {
    this.avgPktDelayDl = avgPktDelayDl;
    return this;
  }

  /**
   * Unsigned integer indicating Packet Delay Budget (see clauses 5.7.3.4 and 5.7.4 of 3GPP TS 23.501), expressed in milliseconds. 
   * minimum: 1
   * @return avgPktDelayDl
   **/
  @Schema(description = "Unsigned integer indicating Packet Delay Budget (see clauses 5.7.3.4 and 5.7.4 of 3GPP TS 23.501), expressed in milliseconds. ")
  
  @Min(1)  public Integer getAvgPktDelayDl() {
    return avgPktDelayDl;
  }

  public void setAvgPktDelayDl(Integer avgPktDelayDl) {
    this.avgPktDelayDl = avgPktDelayDl;
  }

  public ObservedRedundantTransExp varPktDelayDl(Float varPktDelayDl) {
    this.varPktDelayDl = varPktDelayDl;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return varPktDelayDl
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getVarPktDelayDl() {
    return varPktDelayDl;
  }

  public void setVarPktDelayDl(Float varPktDelayDl) {
    this.varPktDelayDl = varPktDelayDl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ObservedRedundantTransExp observedRedundantTransExp = (ObservedRedundantTransExp) o;
    return Objects.equals(this.avgPktDropRateUl, observedRedundantTransExp.avgPktDropRateUl) &&
        Objects.equals(this.varPktDropRateUl, observedRedundantTransExp.varPktDropRateUl) &&
        Objects.equals(this.avgPktDropRateDl, observedRedundantTransExp.avgPktDropRateDl) &&
        Objects.equals(this.varPktDropRateDl, observedRedundantTransExp.varPktDropRateDl) &&
        Objects.equals(this.avgPktDelayUl, observedRedundantTransExp.avgPktDelayUl) &&
        Objects.equals(this.varPktDelayUl, observedRedundantTransExp.varPktDelayUl) &&
        Objects.equals(this.avgPktDelayDl, observedRedundantTransExp.avgPktDelayDl) &&
        Objects.equals(this.varPktDelayDl, observedRedundantTransExp.varPktDelayDl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(avgPktDropRateUl, varPktDropRateUl, avgPktDropRateDl, varPktDropRateDl, avgPktDelayUl, varPktDelayUl, avgPktDelayDl, varPktDelayDl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ObservedRedundantTransExp {\n");
    
    sb.append("    avgPktDropRateUl: ").append(toIndentedString(avgPktDropRateUl)).append("\n");
    sb.append("    varPktDropRateUl: ").append(toIndentedString(varPktDropRateUl)).append("\n");
    sb.append("    avgPktDropRateDl: ").append(toIndentedString(avgPktDropRateDl)).append("\n");
    sb.append("    varPktDropRateDl: ").append(toIndentedString(varPktDropRateDl)).append("\n");
    sb.append("    avgPktDelayUl: ").append(toIndentedString(avgPktDelayUl)).append("\n");
    sb.append("    varPktDelayUl: ").append(toIndentedString(varPktDelayUl)).append("\n");
    sb.append("    avgPktDelayDl: ").append(toIndentedString(avgPktDelayDl)).append("\n");
    sb.append("    varPktDelayDl: ").append(toIndentedString(varPktDelayDl)).append("\n");
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
