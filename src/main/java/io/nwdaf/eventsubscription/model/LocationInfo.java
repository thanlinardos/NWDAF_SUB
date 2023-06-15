package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.UserLocation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents UE location information.
 */
@Schema(description = "Represents UE location information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class LocationInfo   {
  @JsonProperty("loc")
  private UserLocation loc = null;

  @JsonProperty("ratio")
  private Integer ratio = null;

  @JsonProperty("confidence")
  private Integer confidence = null;

  public LocationInfo loc(UserLocation loc) {
    this.loc = loc;
    return this;
  }

  /**
   * Get loc
   * @return loc
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public UserLocation getLoc() {
    return loc;
  }

  public void setLoc(UserLocation loc) {
    this.loc = loc;
  }

  public LocationInfo ratio(Integer ratio) {
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

  public LocationInfo confidence(Integer confidence) {
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
    LocationInfo locationInfo = (LocationInfo) o;
    return Objects.equals(this.loc, locationInfo.loc) &&
        Objects.equals(this.ratio, locationInfo.ratio) &&
        Objects.equals(this.confidence, locationInfo.confidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(loc, ratio, confidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationInfo {\n");
    
    sb.append("    loc: ").append(toIndentedString(loc)).append("\n");
    sb.append("    ratio: ").append(toIndentedString(ratio)).append("\n");
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
