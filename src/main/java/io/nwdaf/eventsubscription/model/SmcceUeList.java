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
 * Represents the List of UEs classified based on experience level of Session Management  congestion control. 
 */
@Schema(description = "Represents the List of UEs classified based on experience level of Session Management  congestion control. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class SmcceUeList  implements AnyOfSmcceUeList {
  @JsonProperty("highLevel")
  @Valid
  private List<String> highLevel = null;

  @JsonProperty("mediumLevel")
  @Valid
  private List<String> mediumLevel = null;

  @JsonProperty("lowLevel")
  @Valid
  private List<String> lowLevel = null;

  public SmcceUeList highLevel(List<String> highLevel) {
    this.highLevel = highLevel;
    return this;
  }

  public SmcceUeList addHighLevelItem(String highLevelItem) {
    if (this.highLevel == null) {
      this.highLevel = new ArrayList<String>();
    }
    this.highLevel.add(highLevelItem);
    return this;
  }

  /**
   * Get highLevel
   * @return highLevel
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getHighLevel() {
    return highLevel;
  }

  public void setHighLevel(List<String> highLevel) {
    this.highLevel = highLevel;
  }

  public SmcceUeList mediumLevel(List<String> mediumLevel) {
    this.mediumLevel = mediumLevel;
    return this;
  }

  public SmcceUeList addMediumLevelItem(String mediumLevelItem) {
    if (this.mediumLevel == null) {
      this.mediumLevel = new ArrayList<String>();
    }
    this.mediumLevel.add(mediumLevelItem);
    return this;
  }

  /**
   * Get mediumLevel
   * @return mediumLevel
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getMediumLevel() {
    return mediumLevel;
  }

  public void setMediumLevel(List<String> mediumLevel) {
    this.mediumLevel = mediumLevel;
  }

  public SmcceUeList lowLevel(List<String> lowLevel) {
    this.lowLevel = lowLevel;
    return this;
  }

  public SmcceUeList addLowLevelItem(String lowLevelItem) {
    if (this.lowLevel == null) {
      this.lowLevel = new ArrayList<String>();
    }
    this.lowLevel.add(lowLevelItem);
    return this;
  }

  /**
   * Get lowLevel
   * @return lowLevel
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getLowLevel() {
    return lowLevel;
  }

  public void setLowLevel(List<String> lowLevel) {
    this.lowLevel = lowLevel;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmcceUeList smcceUeList = (SmcceUeList) o;
    return Objects.equals(this.highLevel, smcceUeList.highLevel) &&
        Objects.equals(this.mediumLevel, smcceUeList.mediumLevel) &&
        Objects.equals(this.lowLevel, smcceUeList.lowLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(highLevel, mediumLevel, lowLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmcceUeList {\n");
    
    sb.append("    highLevel: ").append(toIndentedString(highLevel)).append("\n");
    sb.append("    mediumLevel: ").append(toIndentedString(mediumLevel)).append("\n");
    sb.append("    lowLevel: ").append(toIndentedString(lowLevel)).append("\n");
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
