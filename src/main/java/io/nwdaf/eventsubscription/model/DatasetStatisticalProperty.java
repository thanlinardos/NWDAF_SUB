package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - UNIFORM_DIST_DATA: Indicates the use of data samples that are uniformly distributed according to the different aspects of the requested analytics. - NO_OUTLIERS: Indicates that the data samples shall disregard data samples that are at the extreme boundaries of the value range. 
*/
@Schema(description="Dataset statistical properties of the data used to generate the analytics.")
@Validated
public class DatasetStatisticalProperty {
  public enum DatasetStatisticalPropertyEnum {
    UNIFORM_DIST_DATA("UNIFORM_DIST_DATA"),
    NO_OUTLIERS("NO_OUTLIERS");
    private String value;

    DatasetStatisticalPropertyEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DatasetStatisticalPropertyEnum fromValue(String text) {
      for (DatasetStatisticalPropertyEnum b : DatasetStatisticalPropertyEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("dataStatProps")
  private DatasetStatisticalPropertyEnum dataStatProps = null;

  public DatasetStatisticalProperty dataStatProps(DatasetStatisticalPropertyEnum dataStatProps){
    this.dataStatProps = dataStatProps;
    return this;
  }

  /** Get dataStatProps
  * @return dataStatProps
  **/
  @Schema(description="dataStatProps")

  public DatasetStatisticalPropertyEnum getDataStatProps(){
    return dataStatProps;
  }

  public void setDataStatProps(DatasetStatisticalPropertyEnum dataStatProps){
    this.dataStatProps = dataStatProps;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DatasetStatisticalProperty datasetStatisticalPropertyObject = (DatasetStatisticalProperty) o;
    return Objects.equals(this.dataStatProps, datasetStatisticalPropertyObject.dataStatProps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataStatProps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatasetStatisticalProperty {\n");
    
    sb.append("    dataStatProps: ").append(toIndentedString(dataStatProps)).append("\n");
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
