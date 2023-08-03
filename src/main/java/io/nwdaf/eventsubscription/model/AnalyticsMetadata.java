package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - NUM_OF_SAMPLES: Number of data samples used for the generation of the output analytics. - DATA_WINDOW: Data time window of the data samples. - DATA_STAT_PROPS: Dataset statistical properties of the data used to generate the analytics. - STRATEGY: Output strategy used for the reporting of the analytics. - ACCURACY: Level of accuracy reached for the analytics. 
*/
@Schema(description = "Represents the types of analytics metadata information that can be requested.")
@Validated
public class AnalyticsMetadata {
  public enum AnalyticsMetadataEnum {
    NUM_OF_SAMPLES("NUM_OF_SAMPLES"),
    DATA_WINDOW("DATA_WINDOW"),
    DATA_STAT_PROPS("DATA_STAT_PROPS"),
    STRATEGY("STRATEGY"),
    ACCURACY("ACCURACY");
    private String value;

    AnalyticsMetadataEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AnalyticsMetadataEnum fromValue(String text) {
      for (AnalyticsMetadataEnum b : AnalyticsMetadataEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("anaMeta")
  private AnalyticsMetadataEnum anaMeta = null;

  public AnalyticsMetadata anaMeta(AnalyticsMetadataEnum anaMeta){
    this.anaMeta = anaMeta;
    return this;
  }

  /** Get anaMeta
  * @return anaMeta
  **/
  @Schema(description="anaMeta")

  public AnalyticsMetadataEnum getAnaMeta(){
    return anaMeta;
  }

  public void setAnaMeta(AnalyticsMetadataEnum anaMeta){
    this.anaMeta = anaMeta;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsMetadata analyticsMetadataObject = (AnalyticsMetadata) o;
    return Objects.equals(this.anaMeta, analyticsMetadataObject.anaMeta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anaMeta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsMetadata {\n");
    
    sb.append("    anaMeta: ").append(toIndentedString(anaMeta)).append("\n");
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
