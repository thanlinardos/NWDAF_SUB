package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - LOW: Low accuracy.   - HIGH: High accuracy. 
*/
@Schema(description = "Represents the preferred level of accuracy of the analytics")
@Validated

public class Accuracy {
  public enum AccuracyEnum {
    LOW("LOW"),
    HIGH("HIGH");
  
    private String value;

    AccuracyEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AccuracyEnum fromValue(String text) {
      for (AccuracyEnum b : AccuracyEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("accuracy")
  private AccuracyEnum accuracy = null;

  public Accuracy accuracy(AccuracyEnum accuracy){
    this.accuracy = accuracy;
    return this;
  }

  /** Get accuracy
  * @return accuracy
  **/
  @Schema(description="accuracy")

  public AccuracyEnum getAccuracy(){
    return accuracy;
  }

  public void setAccuracy(AccuracyEnum accuracy){
    this.accuracy = accuracy;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Accuracy accuracyObject = (Accuracy) o;
    return Objects.equals(this.accuracy, accuracyObject.accuracy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accuracy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Accuracy {\n");
    
    sb.append("    accuracy: ").append(toIndentedString(accuracy)).append("\n");
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
