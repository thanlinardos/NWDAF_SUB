package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents average and variance information.
 */
@Schema(description = "Represents average and variance information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class NumberAverage   {
  @JsonProperty("number")
  private Float number = null;

  @JsonProperty("variance")
  private Float variance = null;

  @JsonProperty("skewness")
  private Float skewness = null;

  public NumberAverage number(Float number) {
    this.number = number;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return number
   **/
  @Schema(required = true, description = "string with format 'float' as defined in OpenAPI.")
      @NotNull

    public Float getNumber() {
    return number;
  }

  public void setNumber(Float number) {
    this.number = number;
  }

  public NumberAverage variance(Float variance) {
    this.variance = variance;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return variance
   **/
  @Schema(required = true, description = "string with format 'float' as defined in OpenAPI.")
      @NotNull

    public Float getVariance() {
    return variance;
  }

  public void setVariance(Float variance) {
    this.variance = variance;
  }

  public NumberAverage skewness(Float skewness) {
    this.skewness = skewness;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return skewness
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getSkewness() {
    return skewness;
  }

  public void setSkewness(Float skewness) {
    this.skewness = skewness;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NumberAverage numberAverage = (NumberAverage) o;
    return Objects.equals(this.number, numberAverage.number) &&
        Objects.equals(this.variance, numberAverage.variance) &&
        Objects.equals(this.skewness, numberAverage.skewness);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, variance, skewness);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NumberAverage {\n");
    
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    variance: ").append(toIndentedString(variance)).append("\n");
    sb.append("    skewness: ").append(toIndentedString(skewness)).append("\n");
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
