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
 * Represents the dispersion analytics requirements.
 */
@Schema(description = "Represents the dispersion analytics requirements.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class DispersionRequirement   {
  @JsonProperty("disperType")
  private DispersionType disperType = null;

  @JsonProperty("classCriters")
  @Valid
  private List<ClassCriterion> classCriters = null;

  @JsonProperty("rankCriters")
  @Valid
  private List<RankingCriterion> rankCriters = null;

  @JsonProperty("dispOrderCriter")
  private DispersionOrderingCriterion dispOrderCriter = null;

  @JsonProperty("order")
  private MatchingDirection order = null;

  public DispersionRequirement disperType(DispersionType disperType) {
    this.disperType = disperType;
    return this;
  }

  /**
   * Get disperType
   * @return disperType
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public DispersionType getDisperType() {
    return disperType;
  }

  public void setDisperType(DispersionType disperType) {
    this.disperType = disperType;
  }

  public DispersionRequirement classCriters(List<ClassCriterion> classCriters) {
    this.classCriters = classCriters;
    return this;
  }

  public DispersionRequirement addClassCritersItem(ClassCriterion classCritersItem) {
    if (this.classCriters == null) {
      this.classCriters = new ArrayList<ClassCriterion>();
    }
    this.classCriters.add(classCritersItem);
    return this;
  }

  /**
   * Get classCriters
   * @return classCriters
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<ClassCriterion> getClassCriters() {
    return classCriters;
  }

  public void setClassCriters(List<ClassCriterion> classCriters) {
    this.classCriters = classCriters;
  }

  public DispersionRequirement rankCriters(List<RankingCriterion> rankCriters) {
    this.rankCriters = rankCriters;
    return this;
  }

  public DispersionRequirement addRankCritersItem(RankingCriterion rankCritersItem) {
    if (this.rankCriters == null) {
      this.rankCriters = new ArrayList<RankingCriterion>();
    }
    this.rankCriters.add(rankCritersItem);
    return this;
  }

  /**
   * Get rankCriters
   * @return rankCriters
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<RankingCriterion> getRankCriters() {
    return rankCriters;
  }

  public void setRankCriters(List<RankingCriterion> rankCriters) {
    this.rankCriters = rankCriters;
  }

  public DispersionRequirement dispOrderCriter(DispersionOrderingCriterion dispOrderCriter) {
    this.dispOrderCriter = dispOrderCriter;
    return this;
  }

  /**
   * Get dispOrderCriter
   * @return dispOrderCriter
   **/
  @Schema(description = "")
  
    @Valid
    public DispersionOrderingCriterion getDispOrderCriter() {
    return dispOrderCriter;
  }

  public void setDispOrderCriter(DispersionOrderingCriterion dispOrderCriter) {
    this.dispOrderCriter = dispOrderCriter;
  }

  public DispersionRequirement order(MatchingDirection order) {
    this.order = order;
    return this;
  }

  /**
   * Get order
   * @return order
   **/
  @Schema(description = "")
  
    @Valid
    public MatchingDirection getOrder() {
    return order;
  }

  public void setOrder(MatchingDirection order) {
    this.order = order;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DispersionRequirement dispersionRequirement = (DispersionRequirement) o;
    return Objects.equals(this.disperType, dispersionRequirement.disperType) &&
        Objects.equals(this.classCriters, dispersionRequirement.classCriters) &&
        Objects.equals(this.rankCriters, dispersionRequirement.rankCriters) &&
        Objects.equals(this.dispOrderCriter, dispersionRequirement.dispOrderCriter) &&
        Objects.equals(this.order, dispersionRequirement.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(disperType, classCriters, rankCriters, dispOrderCriter, order);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DispersionRequirement {\n");
    
    sb.append("    disperType: ").append(toIndentedString(disperType)).append("\n");
    sb.append("    classCriters: ").append(toIndentedString(classCriters)).append("\n");
    sb.append("    rankCriters: ").append(toIndentedString(rankCriters)).append("\n");
    sb.append("    dispOrderCriter: ").append(toIndentedString(dispOrderCriter)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
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
