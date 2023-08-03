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
 * Contains information about a request to transfer analytics subscriptions.
 */
@Schema(description = "Contains information about a request to transfer analytics subscriptions.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class AnalyticsSubscriptionsTransfer   {
  @JsonProperty("subsTransInfos")
  @Valid
  private List<SubscriptionTransferInfo> subsTransInfos = new ArrayList<SubscriptionTransferInfo>();

  public AnalyticsSubscriptionsTransfer subsTransInfos(List<SubscriptionTransferInfo> subsTransInfos) {
    this.subsTransInfos = subsTransInfos;
    return this;
  }

  public AnalyticsSubscriptionsTransfer addSubsTransInfosItem(SubscriptionTransferInfo subsTransInfosItem) {
    this.subsTransInfos.add(subsTransInfosItem);
    return this;
  }

  /**
   * Get subsTransInfos
   * @return subsTransInfos
   **/
  @Schema(required = true, description = "")
      @NotNull
    @Valid
  @Size(min=1)   public List<SubscriptionTransferInfo> getSubsTransInfos() {
    return subsTransInfos;
  }

  public void setSubsTransInfos(List<SubscriptionTransferInfo> subsTransInfos) {
    this.subsTransInfos = subsTransInfos;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsSubscriptionsTransfer analyticsSubscriptionsTransfer = (AnalyticsSubscriptionsTransfer) o;
    return Objects.equals(this.subsTransInfos, analyticsSubscriptionsTransfer.subsTransInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subsTransInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsSubscriptionsTransfer {\n");
    
    sb.append("    subsTransInfos: ").append(toIndentedString(subsTransInfos)).append("\n");
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
