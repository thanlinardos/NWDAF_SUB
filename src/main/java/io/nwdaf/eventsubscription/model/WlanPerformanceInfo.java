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
 * The WLAN performance related information.
 */
@Schema(description = "The WLAN performance related information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class WlanPerformanceInfo   {
  @JsonProperty("networkArea")
  private NetworkAreaInfo networkArea = null;

  @JsonProperty("wlanPerSsidInfos")
  @Valid
  private List<WlanPerSsIdPerformanceInfo> wlanPerSsidInfos = new ArrayList<WlanPerSsIdPerformanceInfo>();

  public WlanPerformanceInfo networkArea(NetworkAreaInfo networkArea) {
    this.networkArea = networkArea;
    return this;
  }

  /**
   * Get networkArea
   * @return networkArea
   **/
  @Schema(description = "")
  
    @Valid
    public NetworkAreaInfo getNetworkArea() {
    return networkArea;
  }

  public void setNetworkArea(NetworkAreaInfo networkArea) {
    this.networkArea = networkArea;
  }

  public WlanPerformanceInfo wlanPerSsidInfos(List<WlanPerSsIdPerformanceInfo> wlanPerSsidInfos) {
    this.wlanPerSsidInfos = wlanPerSsidInfos;
    return this;
  }

  public WlanPerformanceInfo addWlanPerSsidInfosItem(WlanPerSsIdPerformanceInfo wlanPerSsidInfosItem) {
    this.wlanPerSsidInfos.add(wlanPerSsidInfosItem);
    return this;
  }

  /**
   * Get wlanPerSsidInfos
   * @return wlanPerSsidInfos
   **/
  @Schema(required = true, description = "")
      @NotNull
    @Valid
  @Size(min=1)   public List<WlanPerSsIdPerformanceInfo> getWlanPerSsidInfos() {
    return wlanPerSsidInfos;
  }

  public void setWlanPerSsidInfos(List<WlanPerSsIdPerformanceInfo> wlanPerSsidInfos) {
    this.wlanPerSsidInfos = wlanPerSsidInfos;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WlanPerformanceInfo wlanPerformanceInfo = (WlanPerformanceInfo) o;
    return Objects.equals(this.networkArea, wlanPerformanceInfo.networkArea) &&
        Objects.equals(this.wlanPerSsidInfos, wlanPerformanceInfo.wlanPerSsidInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkArea, wlanPerSsidInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WlanPerformanceInfo {\n");
    
    sb.append("    networkArea: ").append(toIndentedString(networkArea)).append("\n");
    sb.append("    wlanPerSsidInfos: ").append(toIndentedString(wlanPerSsidInfos)).append("\n");
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
