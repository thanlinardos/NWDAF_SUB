package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.FlowInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Top application that contributes the most to the traffic.
 */
@Schema(description = "Top application that contributes the most to the traffic.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class TopApplication  implements OneOfTopApplication {
  @JsonProperty("appId")
  private String appId = null;

  @JsonProperty("ipTrafficFilter")
  private FlowInfo ipTrafficFilter = null;

  @JsonProperty("ratio")
  private Integer ratio = null;

  public TopApplication appId(String appId) {
    this.appId = appId;
    return this;
  }

  /**
   * String providing an application identifier.
   * @return appId
   **/
  @Schema(description = "String providing an application identifier.")
  
    public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public TopApplication ipTrafficFilter(FlowInfo ipTrafficFilter) {
    this.ipTrafficFilter = ipTrafficFilter;
    return this;
  }

  /**
   * Get ipTrafficFilter
   * @return ipTrafficFilter
   **/
  @Schema(description = "")
  
    @Valid
    public FlowInfo getIpTrafficFilter() {
    return ipTrafficFilter;
  }

  public void setIpTrafficFilter(FlowInfo ipTrafficFilter) {
    this.ipTrafficFilter = ipTrafficFilter;
  }

  public TopApplication ratio(Integer ratio) {
    this.ratio = ratio;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return ratio
   **/
  @Schema(description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
  
  @Min(1) @Max(100)   public Integer getRatio() {
    return ratio;
  }

  public void setRatio(Integer ratio) {
    this.ratio = ratio;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopApplication topApplication = (TopApplication) o;
    return Objects.equals(this.appId, topApplication.appId) &&
        Objects.equals(this.ipTrafficFilter, topApplication.ipTrafficFilter) &&
        Objects.equals(this.ratio, topApplication.ratio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, ipTrafficFilter, ratio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopApplication {\n");
    
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    ipTrafficFilter: ").append(toIndentedString(ipTrafficFilter)).append("\n");
    sb.append("    ratio: ").append(toIndentedString(ratio)).append("\n");
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
