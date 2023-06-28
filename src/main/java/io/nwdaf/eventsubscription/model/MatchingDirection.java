package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - ASCENDING: Threshold is crossed in ascending direction. - DESCENDING: Threshold is crossed in descending direction. - CROSSED: Threshold is crossed either in ascending or descending direction. 
*/
@Schema(description = "Defines the matching direction when crossing a threshold.")
@Validated
public class MatchingDirection {
  public enum MatchingDirectionEnum {
    ASCENDING("ASCENDING"),
    DESCENDING("DESCENDING"),
    CROSSED("CROSSED");
    private String value;

    MatchingDirectionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static MatchingDirectionEnum fromValue(String text) {
      for (MatchingDirectionEnum b : MatchingDirectionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("matchingDir")
  private MatchingDirectionEnum matchingDir = null;

  public MatchingDirection matchingDir(MatchingDirectionEnum matchingDir){
    this.matchingDir = matchingDir;
    return this;
  }

  /** Get matchingDir
  * @return matchingDir
  **/
  @Schema(description="matchingDir")

  public MatchingDirectionEnum getMatchingDir(){
    return matchingDir;
  }

  public void setMatchingDir(MatchingDirectionEnum matchingDir){
    this.matchingDir = matchingDir;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchingDirection matchingDirectionObject = (MatchingDirection) o;
    return Objects.equals(this.matchingDir, matchingDirectionObject.matchingDir);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchingDir);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchingDirection {\n");
    
    sb.append("    matchingDir: ").append(toIndentedString(matchingDir)).append("\n");
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
