package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Indicates the usage ranking criterion between the high, medium and low usage UE.
 */
@Schema(description = "Indicates the usage ranking criterion between the high, medium and low usage UE.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class RankingCriterion   {
  @JsonProperty("highBase")
  private Integer highBase = null;

  @JsonProperty("lowBase")
  private Integer lowBase = null;

  public RankingCriterion highBase(Integer highBase) {
    this.highBase = highBase;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return highBase
   **/
  @Schema(required = true, description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
      @NotNull

  @Min(1) @Max(100)   public Integer getHighBase() {
    return highBase;
  }

  public void setHighBase(Integer highBase) {
    this.highBase = highBase;
  }

  public RankingCriterion lowBase(Integer lowBase) {
    this.lowBase = lowBase;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return lowBase
   **/
  @Schema(required = true, description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
      @NotNull

  @Min(1) @Max(100)   public Integer getLowBase() {
    return lowBase;
  }

  public void setLowBase(Integer lowBase) {
    this.lowBase = lowBase;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RankingCriterion rankingCriterion = (RankingCriterion) o;
    return Objects.equals(this.highBase, rankingCriterion.highBase) &&
        Objects.equals(this.lowBase, rankingCriterion.lowBase);
  }

  @Override
  public int hashCode() {
    return Objects.hash(highBase, lowBase);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RankingCriterion {\n");
    
    sb.append("    highBase: ").append(toIndentedString(highBase)).append("\n");
    sb.append("    lowBase: ").append(toIndentedString(lowBase)).append("\n");
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
