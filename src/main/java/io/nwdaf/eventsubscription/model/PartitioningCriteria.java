package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - \&quot;TAC\&quot;: Type Allocation Code - \&quot;SUBPLMN\&quot;: Subscriber PLMN ID - \&quot;GEOAREA\&quot;: Geographical area, i.e. list(s) of TAI(s) - \&quot;SNSSAI\&quot;: S-NSSAI - \&quot;DNN\&quot;: DNN 
*/
@Schema(description = "The enumeration PartitioningCriteria indicates criteria for grouping the UEs.")
@Validated
public class PartitioningCriteria {
  public enum PartitioningCriteriaEnum {
    TAC("TAC"),
    SUBPLMN("SUBPLMN"),
    GEOAREA("GEOAREA"),
    SNSSAI("SNSSAI"),
    DNN("DNN");
    private String value;

    PartitioningCriteriaEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PartitioningCriteriaEnum fromValue(String text) {
      for (PartitioningCriteriaEnum b : PartitioningCriteriaEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("partitionCriteria")
  private PartitioningCriteriaEnum partitionCriteria = null;

  public PartitioningCriteria partitionCriteria(PartitioningCriteriaEnum partitionCriteria){
    this.partitionCriteria = partitionCriteria;
    return this;
  }

  /** Get partitionCriteria
  * @return partitionCriteria
  **/
  @Schema(description="partitionCriteria")

  public PartitioningCriteriaEnum getPartitionCriteria(){
    return partitionCriteria;
  }

  public void setPartitionCriteria(PartitioningCriteriaEnum partitionCriteria){
    this.partitionCriteria = partitionCriteria;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartitioningCriteria partitioningCriteriaObject = (PartitioningCriteria) o;
    return Objects.equals(this.partitionCriteria, partitioningCriteriaObject.partitionCriteria);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partitionCriteria);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartitioningCriteria {\n");
    
    sb.append("    partitionCriteria: ").append(toIndentedString(partitionCriteria)).append("\n");
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
