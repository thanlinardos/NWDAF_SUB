package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contains the description of a circumstance.
 */
@Schema(description = "Contains the description of a circumstance.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class CircumstanceDescription   {
  @JsonProperty("freq")
  private Float freq = null;

  @JsonProperty("tm")
  private OffsetDateTime tm = null;

  @JsonProperty("locArea")
  private NetworkAreaInfo locArea = null;

  @JsonProperty("vol")
  private Long vol = null;

  public CircumstanceDescription freq(Float freq) {
    this.freq = freq;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return freq
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getFreq() {
    return freq;
  }

  public void setFreq(Float freq) {
    this.freq = freq;
  }

  public CircumstanceDescription tm(OffsetDateTime tm) {
    this.tm = tm;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return tm
   **/
  @Schema(description = "string with format 'date-time' as defined in OpenAPI.")
  
    @Valid
    public OffsetDateTime getTm() {
    return tm;
  }

  public void setTm(OffsetDateTime tm) {
    this.tm = tm;
  }

  public CircumstanceDescription locArea(NetworkAreaInfo locArea) {
    this.locArea = locArea;
    return this;
  }

  /**
   * Get locArea
   * @return locArea
   **/
  @Schema(description = "")
  
    @Valid
    public NetworkAreaInfo getLocArea() {
    return locArea;
  }

  public void setLocArea(NetworkAreaInfo locArea) {
    this.locArea = locArea;
  }

  public CircumstanceDescription vol(Long vol) {
    this.vol = vol;
    return this;
  }

  /**
   * Unsigned integer identifying a volume in units of bytes.
   * minimum: 0
   * @return vol
   **/
  @Schema(description = "Unsigned integer identifying a volume in units of bytes.")
  
  @Min(0L)  public Long getVol() {
    return vol;
  }

  public void setVol(Long vol) {
    this.vol = vol;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CircumstanceDescription circumstanceDescription = (CircumstanceDescription) o;
    return Objects.equals(this.freq, circumstanceDescription.freq) &&
        Objects.equals(this.tm, circumstanceDescription.tm) &&
        Objects.equals(this.locArea, circumstanceDescription.locArea) &&
        Objects.equals(this.vol, circumstanceDescription.vol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(freq, tm, locArea, vol);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CircumstanceDescription {\n");
    
    sb.append("    freq: ").append(toIndentedString(freq)).append("\n");
    sb.append("    tm: ").append(toIndentedString(tm)).append("\n");
    sb.append("    locArea: ").append(toIndentedString(locArea)).append("\n");
    sb.append("    vol: ").append(toIndentedString(vol)).append("\n");
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
