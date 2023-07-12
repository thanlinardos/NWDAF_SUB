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
 * Represents IP flow information.
 */
@Schema(description = "Represents IP flow information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class FlowInfo   {
  @JsonProperty("flowId")
  private Integer flowId = null;

  @JsonProperty("flowDescriptions")
  @Valid
  private List<String> flowDescriptions = null;

  public FlowInfo flowId(Integer flowId) {
    this.flowId = flowId;
    return this;
  }

  /**
   * Indicates the IP flow identifier.
   * @return flowId
   **/
  @Schema(required = true, description = "Indicates the IP flow identifier.")
      @NotNull

    public Integer getFlowId() {
    return flowId;
  }

  public void setFlowId(Integer flowId) {
    this.flowId = flowId;
  }

  public FlowInfo flowDescriptions(List<String> flowDescriptions) {
    this.flowDescriptions = flowDescriptions;
    return this;
  }

  public FlowInfo addFlowDescriptionsItem(String flowDescriptionsItem) {
    if (this.flowDescriptions == null) {
      this.flowDescriptions = new ArrayList<String>();
    }
    this.flowDescriptions.add(flowDescriptionsItem);
    return this;
  }

  /**
   * Indicates the packet filters of the IP flow. Refer to clause 5.3.8 of 3GPP TS 29.214 for encoding. It shall contain UL and/or DL IP flow description. 
   * @return flowDescriptions
   **/
  @Schema(description = "Indicates the packet filters of the IP flow. Refer to clause 5.3.8 of 3GPP TS 29.214 for encoding. It shall contain UL and/or DL IP flow description. ")
  
  @Size(min=1,max=2)   public List<String> getFlowDescriptions() {
    return flowDescriptions;
  }

  public void setFlowDescriptions(List<String> flowDescriptions) {
    this.flowDescriptions = flowDescriptions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlowInfo flowInfo = (FlowInfo) o;
    return Objects.equals(this.flowId, flowInfo.flowId) &&
        Objects.equals(this.flowDescriptions, flowInfo.flowDescriptions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flowId, flowDescriptions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlowInfo {\n");
    
    sb.append("    flowId: ").append(toIndentedString(flowId)).append("\n");
    sb.append("    flowDescriptions: ").append(toIndentedString(flowDescriptions)).append("\n");
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
