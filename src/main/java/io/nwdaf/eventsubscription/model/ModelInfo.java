package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contains information about an ML model.
 */
@Schema(description = "Contains information about an ML model.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ModelInfo   {
  @JsonProperty("analyticsId")
  private NwdafEvent analyticsId = null;

  @JsonProperty("mlModelInfos")
  @Valid
  private List<MLModelInfo> mlModelInfos = new ArrayList<MLModelInfo>();

  public ModelInfo analyticsId(NwdafEvent analyticsId) {
    this.analyticsId = analyticsId;
    return this;
  }

  /**
   * Get analyticsId
   * @return analyticsId
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public NwdafEvent getAnalyticsId() {
    return analyticsId;
  }

  public void setAnalyticsId(NwdafEvent analyticsId) {
    this.analyticsId = analyticsId;
  }

  public ModelInfo mlModelInfos(List<MLModelInfo> mlModelInfos) {
    this.mlModelInfos = mlModelInfos;
    return this;
  }

  public ModelInfo addMlModelInfosItem(MLModelInfo mlModelInfosItem) {
    this.mlModelInfos.add(mlModelInfosItem);
    return this;
  }

  /**
   * Get mlModelInfos
   * @return mlModelInfos
   **/
  @Schema(required = true, description = "")
      @NotNull
    @Valid
  @Size(min=1)   public List<MLModelInfo> getMlModelInfos() {
    return mlModelInfos;
  }

  public void setMlModelInfos(List<MLModelInfo> mlModelInfos) {
    this.mlModelInfos = mlModelInfos;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelInfo modelInfo = (ModelInfo) o;
    return Objects.equals(this.analyticsId, modelInfo.analyticsId) &&
        Objects.equals(this.mlModelInfos, modelInfo.mlModelInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(analyticsId, mlModelInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelInfo {\n");
    
    sb.append("    analyticsId: ").append(toIndentedString(analyticsId)).append("\n");
    sb.append("    mlModelInfos: ").append(toIndentedString(mlModelInfos)).append("\n");
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
