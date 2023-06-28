package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - BINARY: Indicates that the analytics shall only be reported when the requested level of accuracy is reached within a cycle of periodic notification. - GRADIENT: Indicates that the analytics shall be reported according with the periodicity irrespective of whether the requested level of accuracy has been reached or not. 
*/
@Schema(description = "Represents the output strategy used for the reporting of the analytics.")
@Validated
public class OutputStrategy {
  public enum OutputStrategyEnum {
    BINARY ("BINARY "),
    GRADIENT("GRADIENT");
    private String value;

    OutputStrategyEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OutputStrategyEnum fromValue(String text) {
      for (OutputStrategyEnum b : OutputStrategyEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("strategy")
  private OutputStrategyEnum strategy = null;

  public OutputStrategy strategy(OutputStrategyEnum strategy){
    this.strategy = strategy;
    return this;
  }

  /** Get strategy
  * @return strategy
  **/
  @Schema(description="strategy")

  public OutputStrategyEnum getStrategy(){
    return strategy;
  }

  public void setStrategy(OutputStrategyEnum strategy){
    this.strategy = strategy;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutputStrategy outputStrategyObject = (OutputStrategy) o;
    return Objects.equals(this.strategy, outputStrategyObject.strategy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(strategy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutputStrategy {\n");
    
    sb.append("    strategy: ").append(toIndentedString(strategy)).append("\n");
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
