package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.DispersionCollection;
import io.nwdaf.eventsubscription.model.DispersionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents the Dispersion information. When subscribed event is \&quot;DISPERSION\&quot;, the  \&quot;disperInfos\&quot; attribute shall be included. 
 */
@Schema(description = "Represents the Dispersion information. When subscribed event is \"DISPERSION\", the  \"disperInfos\" attribute shall be included. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class DispersionInfo   {
  @JsonProperty("tsStart")
  private OffsetDateTime tsStart = null;

  @JsonProperty("tsDuration")
  private Integer tsDuration = null;

  @JsonProperty("disperCollects")
  @Valid
  private List<DispersionCollection> disperCollects = new ArrayList<DispersionCollection>();

  @JsonProperty("disperType")
  private DispersionType disperType = null;

  public DispersionInfo tsStart(OffsetDateTime tsStart) {
    this.tsStart = tsStart;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return tsStart
   **/
  @Schema(required = true, description = "string with format 'date-time' as defined in OpenAPI.")
      @NotNull

    @Valid
    public OffsetDateTime getTsStart() {
    return tsStart;
  }

  public void setTsStart(OffsetDateTime tsStart) {
    this.tsStart = tsStart;
  }

  public DispersionInfo tsDuration(Integer tsDuration) {
    this.tsDuration = tsDuration;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return tsDuration
   **/
  @Schema(required = true, description = "indicating a time in seconds.")
      @NotNull

    public Integer getTsDuration() {
    return tsDuration;
  }

  public void setTsDuration(Integer tsDuration) {
    this.tsDuration = tsDuration;
  }

  public DispersionInfo disperCollects(List<DispersionCollection> disperCollects) {
    this.disperCollects = disperCollects;
    return this;
  }

  public DispersionInfo addDisperCollectsItem(DispersionCollection disperCollectsItem) {
    this.disperCollects.add(disperCollectsItem);
    return this;
  }

  /**
   * Get disperCollects
   * @return disperCollects
   **/
  @Schema(required = true, description = "")
      @NotNull
    @Valid
  @Size(min=1)   public List<DispersionCollection> getDisperCollects() {
    return disperCollects;
  }

  public void setDisperCollects(List<DispersionCollection> disperCollects) {
    this.disperCollects = disperCollects;
  }

  public DispersionInfo disperType(DispersionType disperType) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DispersionInfo dispersionInfo = (DispersionInfo) o;
    return Objects.equals(this.tsStart, dispersionInfo.tsStart) &&
        Objects.equals(this.tsDuration, dispersionInfo.tsDuration) &&
        Objects.equals(this.disperCollects, dispersionInfo.disperCollects) &&
        Objects.equals(this.disperType, dispersionInfo.disperType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tsStart, tsDuration, disperCollects, disperType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DispersionInfo {\n");
    
    sb.append("    tsStart: ").append(toIndentedString(tsStart)).append("\n");
    sb.append("    tsDuration: ").append(toIndentedString(tsDuration)).append("\n");
    sb.append("    disperCollects: ").append(toIndentedString(disperCollects)).append("\n");
    sb.append("    disperType: ").append(toIndentedString(disperType)).append("\n");
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
