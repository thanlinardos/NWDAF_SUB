package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.DispersionClass;
import io.nwdaf.eventsubscription.model.MatchingDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Indicates the dispersion class criterion for fixed, camper and/or traveller UE, and/or the  top-heavy UE dispersion class criterion. 
 */
@Schema(description = "Indicates the dispersion class criterion for fixed, camper and/or traveller UE, and/or the  top-heavy UE dispersion class criterion. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ClassCriterion   {
  @JsonProperty("disperClass")
  private DispersionClass disperClass = null;

  @JsonProperty("classThreshold")
  private Integer classThreshold = null;

  @JsonProperty("thresMatch")
  private MatchingDirection thresMatch = null;

  public ClassCriterion disperClass(DispersionClass disperClass) {
    this.disperClass = disperClass;
    return this;
  }

  /**
   * Get disperClass
   * @return disperClass
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public DispersionClass getDisperClass() {
    return disperClass;
  }

  public void setDisperClass(DispersionClass disperClass) {
    this.disperClass = disperClass;
  }

  public ClassCriterion classThreshold(Integer classThreshold) {
    this.classThreshold = classThreshold;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return classThreshold
   **/
  @Schema(required = true, description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
      @NotNull

  @Min(1) @Max(100)   public Integer getClassThreshold() {
    return classThreshold;
  }

  public void setClassThreshold(Integer classThreshold) {
    this.classThreshold = classThreshold;
  }

  public ClassCriterion thresMatch(MatchingDirection thresMatch) {
    this.thresMatch = thresMatch;
    return this;
  }

  /**
   * Get thresMatch
   * @return thresMatch
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public MatchingDirection getThresMatch() {
    return thresMatch;
  }

  public void setThresMatch(MatchingDirection thresMatch) {
    this.thresMatch = thresMatch;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClassCriterion classCriterion = (ClassCriterion) o;
    return Objects.equals(this.disperClass, classCriterion.disperClass) &&
        Objects.equals(this.classThreshold, classCriterion.classThreshold) &&
        Objects.equals(this.thresMatch, classCriterion.thresMatch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(disperClass, classThreshold, thresMatch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClassCriterion {\n");
    
    sb.append("    disperClass: ").append(toIndentedString(disperClass)).append("\n");
    sb.append("    classThreshold: ").append(toIndentedString(classThreshold)).append("\n");
    sb.append("    thresMatch: ").append(toIndentedString(thresMatch)).append("\n");
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
