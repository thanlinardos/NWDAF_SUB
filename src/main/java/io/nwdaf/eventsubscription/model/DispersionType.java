package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are:   - DVDA: Data Volume Dispersion Analytics.   - TDA: Transactions Dispersion Analytics.   - DVDA_AND_TDA: Data Volume Dispersion Analytics and Transactions Dispersion Analytics. 
*/
@Schema(description = "Dispersion type.")
@Validated
public class DispersionType {
  public enum DispersionTypeEnum {
    DVDA("DVDA"),
    TDA("TDA"),
    DVDA_AND_TDA("DVDA_AND_TDA");
    private String value;

    DispersionTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DispersionTypeEnum fromValue(String text) {
      for (DispersionTypeEnum b : DispersionTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("disperType")
  private DispersionTypeEnum disperType = null;

  public DispersionType disperType(DispersionTypeEnum disperType){
    this.disperType = disperType;
    return this;
  }

  /** Get disperType
  * @return disperType
  **/
  @Schema(description="disperType")

  public DispersionTypeEnum getDisperType(){
    return disperType;
  }

  public void setDisperType(DispersionTypeEnum disperType){
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
    DispersionType dispersionTypeObject = (DispersionType) o;
    return Objects.equals(this.disperType, dispersionTypeObject.disperType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(disperType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DispersionType {\n");
    
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
