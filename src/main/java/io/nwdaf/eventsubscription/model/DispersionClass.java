package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - FIXED: Dispersion class as fixed UE its data or transaction usage at a location or a slice, is higher than its class threshold set for its all data or transaction usage. - CAMPER: Dispersion class as camper UE, its data or transaction usage at a location or a slice, is higher than its class threshold and lower than the fixed class threshold set for its all data or transaction usage.. - TRAVELLER: Dispersion class as traveller UE, its data or transaction usage at a location or a slice, is lower than the camper class threshold set for its all data or transaction usage. - TOP_HEAVY: Dispersion class as Top_Heavy UE, who&#x27;s dispersion percentile rating at a location or a slice, is higher than its class threshold. 
*/
@Schema(description="Dispersion class.")
@Validated
public class DispersionClass {
  public enum DispersionClassEnum {
    FIXED("FIXED"),
    CAMPER("CAMPER"),
    TRAVELLER("TRAVELLER"),
    TOP_HEAVY("TOP_HEAVY");
    private String value;

    DispersionClassEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DispersionClassEnum fromValue(String text) {
      for (DispersionClassEnum b : DispersionClassEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("disperClass")
  private DispersionClassEnum disperClass = null;

  public DispersionClass disperClass(DispersionClassEnum disperClass){
    this.disperClass = disperClass;
    return this;
  }

  /** Get disperClass
  * @return disperClass
  **/
  @Schema(description="disperClass")

  public DispersionClassEnum getDisperClass(){
    return disperClass;
  }

  public void setDisperClass(DispersionClassEnum disperClass){
    this.disperClass = disperClass;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DispersionClass dispersionClassObject = (DispersionClass) o;
    return Objects.equals(this.disperClass, dispersionClassObject.disperClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(disperClass);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DispersionClass {\n");
    
    sb.append("    disperClass: ").append(toIndentedString(disperClass)).append("\n");
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
