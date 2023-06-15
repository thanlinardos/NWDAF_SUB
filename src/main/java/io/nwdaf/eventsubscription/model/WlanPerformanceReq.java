package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.MatchingDirection;
import io.nwdaf.eventsubscription.model.WlanOrderingCriterion;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents other WLAN performance analytics requirements.
 */
@Schema(description = "Represents other WLAN performance analytics requirements.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class WlanPerformanceReq   {
  @JsonProperty("ssIds")
  @Valid
  private List<String> ssIds = null;

  @JsonProperty("bssIds")
  @Valid
  private List<String> bssIds = null;

  @JsonProperty("wlanOrderCriter")
  private WlanOrderingCriterion wlanOrderCriter = null;

  @JsonProperty("order")
  private MatchingDirection order = null;

  public WlanPerformanceReq ssIds(List<String> ssIds) {
    this.ssIds = ssIds;
    return this;
  }

  public WlanPerformanceReq addSsIdsItem(String ssIdsItem) {
    if (this.ssIds == null) {
      this.ssIds = new ArrayList<String>();
    }
    this.ssIds.add(ssIdsItem);
    return this;
  }

  /**
   * Get ssIds
   * @return ssIds
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getSsIds() {
    return ssIds;
  }

  public void setSsIds(List<String> ssIds) {
    this.ssIds = ssIds;
  }

  public WlanPerformanceReq bssIds(List<String> bssIds) {
    this.bssIds = bssIds;
    return this;
  }

  public WlanPerformanceReq addBssIdsItem(String bssIdsItem) {
    if (this.bssIds == null) {
      this.bssIds = new ArrayList<String>();
    }
    this.bssIds.add(bssIdsItem);
    return this;
  }

  /**
   * Get bssIds
   * @return bssIds
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getBssIds() {
    return bssIds;
  }

  public void setBssIds(List<String> bssIds) {
    this.bssIds = bssIds;
  }

  public WlanPerformanceReq wlanOrderCriter(WlanOrderingCriterion wlanOrderCriter) {
    this.wlanOrderCriter = wlanOrderCriter;
    return this;
  }

  /**
   * Get wlanOrderCriter
   * @return wlanOrderCriter
   **/
  @Schema(description = "")
  
    @Valid
    public WlanOrderingCriterion getWlanOrderCriter() {
    return wlanOrderCriter;
  }

  public void setWlanOrderCriter(WlanOrderingCriterion wlanOrderCriter) {
    this.wlanOrderCriter = wlanOrderCriter;
  }

  public WlanPerformanceReq order(MatchingDirection order) {
    this.order = order;
    return this;
  }

  /**
   * Get order
   * @return order
   **/
  @Schema(description = "")
  
    @Valid
    public MatchingDirection getOrder() {
    return order;
  }

  public void setOrder(MatchingDirection order) {
    this.order = order;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WlanPerformanceReq wlanPerformanceReq = (WlanPerformanceReq) o;
    return Objects.equals(this.ssIds, wlanPerformanceReq.ssIds) &&
        Objects.equals(this.bssIds, wlanPerformanceReq.bssIds) &&
        Objects.equals(this.wlanOrderCriter, wlanPerformanceReq.wlanOrderCriter) &&
        Objects.equals(this.order, wlanPerformanceReq.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ssIds, bssIds, wlanOrderCriter, order);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WlanPerformanceReq {\n");
    
    sb.append("    ssIds: ").append(toIndentedString(ssIds)).append("\n");
    sb.append("    bssIds: ").append(toIndentedString(bssIds)).append("\n");
    sb.append("    wlanOrderCriter: ").append(toIndentedString(wlanOrderCriter)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
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
