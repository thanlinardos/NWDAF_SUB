package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.WlanPerTsPerformanceInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * The WLAN performance per SSID.
 */
@Schema(description = "The WLAN performance per SSID.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class WlanPerSsIdPerformanceInfo   {
  @JsonProperty("ssId")
  private String ssId = null;

  @JsonProperty("wlanPerTsInfos")
  @Valid
  private List<WlanPerTsPerformanceInfo> wlanPerTsInfos = new ArrayList<WlanPerTsPerformanceInfo>();

  public WlanPerSsIdPerformanceInfo ssId(String ssId) {
    this.ssId = ssId;
    return this;
  }

  /**
   * Get ssId
   * @return ssId
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getSsId() {
    return ssId;
  }

  public void setSsId(String ssId) {
    this.ssId = ssId;
  }

  public WlanPerSsIdPerformanceInfo wlanPerTsInfos(List<WlanPerTsPerformanceInfo> wlanPerTsInfos) {
    this.wlanPerTsInfos = wlanPerTsInfos;
    return this;
  }

  public WlanPerSsIdPerformanceInfo addWlanPerTsInfosItem(WlanPerTsPerformanceInfo wlanPerTsInfosItem) {
    this.wlanPerTsInfos.add(wlanPerTsInfosItem);
    return this;
  }

  /**
   * Get wlanPerTsInfos
   * @return wlanPerTsInfos
   **/
  @Schema(required = true, description = "")
      @NotNull
    @Valid
  @Size(min=1)   public List<WlanPerTsPerformanceInfo> getWlanPerTsInfos() {
    return wlanPerTsInfos;
  }

  public void setWlanPerTsInfos(List<WlanPerTsPerformanceInfo> wlanPerTsInfos) {
    this.wlanPerTsInfos = wlanPerTsInfos;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WlanPerSsIdPerformanceInfo wlanPerSsIdPerformanceInfo = (WlanPerSsIdPerformanceInfo) o;
    return Objects.equals(this.ssId, wlanPerSsIdPerformanceInfo.ssId) &&
        Objects.equals(this.wlanPerTsInfos, wlanPerSsIdPerformanceInfo.wlanPerTsInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ssId, wlanPerTsInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WlanPerSsIdPerformanceInfo {\n");
    
    sb.append("    ssId: ").append(toIndentedString(ssId)).append("\n");
    sb.append("    wlanPerTsInfos: ").append(toIndentedString(wlanPerTsInfos)).append("\n");
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
