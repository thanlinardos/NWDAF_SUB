package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * Contains the description of an Uplink and/or Downlink Ethernet flow.
 */
@Schema(description = "Contains the description of an Uplink and/or Downlink Ethernet flow.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class IpEthFlowDescription  implements OneOfIpEthFlowDescription {
  @JsonProperty("ipTrafficFilter")
  private String ipTrafficFilter = null;

  @JsonProperty("ethTrafficFilter")
  private EthFlowDescription ethTrafficFilter = null;

  public IpEthFlowDescription ipTrafficFilter(String ipTrafficFilter) {
    this.ipTrafficFilter = ipTrafficFilter;
    return this;
  }

  /**
   * Defines a packet filter of an IP flow.
   * @return ipTrafficFilter
   **/
  @Schema(description = "Defines a packet filter of an IP flow.")
  
    public String getIpTrafficFilter() {
    return ipTrafficFilter;
  }

  public void setIpTrafficFilter(String ipTrafficFilter) {
    this.ipTrafficFilter = ipTrafficFilter;
  }

  public IpEthFlowDescription ethTrafficFilter(EthFlowDescription ethTrafficFilter) {
    this.ethTrafficFilter = ethTrafficFilter;
    return this;
  }

  /**
   * Get ethTrafficFilter
   * @return ethTrafficFilter
   **/
  @Schema(description = "")
  
    @Valid
    public EthFlowDescription getEthTrafficFilter() {
    return ethTrafficFilter;
  }

  public void setEthTrafficFilter(EthFlowDescription ethTrafficFilter) {
    this.ethTrafficFilter = ethTrafficFilter;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IpEthFlowDescription ipEthFlowDescription = (IpEthFlowDescription) o;
    return Objects.equals(this.ipTrafficFilter, ipEthFlowDescription.ipTrafficFilter) &&
        Objects.equals(this.ethTrafficFilter, ipEthFlowDescription.ethTrafficFilter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ipTrafficFilter, ethTrafficFilter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IpEthFlowDescription {\n");
    
    sb.append("    ipTrafficFilter: ").append(toIndentedString(ipTrafficFilter)).append("\n");
    sb.append("    ethTrafficFilter: ").append(toIndentedString(ethTrafficFilter)).append("\n");
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
