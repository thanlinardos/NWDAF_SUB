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
 * Contains information about available analytics contexts.
 */
@Schema(description = "Contains information about available analytics contexts.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class AnalyticsContextIdentifier   {
  @JsonProperty("subscriptionId")
  private String subscriptionId = null;

  @JsonProperty("nfAnaCtxts")
  @Valid
  private List<NwdafEvent> nfAnaCtxts = null;

  @JsonProperty("ueAnaCtxts")
  @Valid
  private List<UeAnalyticsContextDescriptor> ueAnaCtxts = null;

  public AnalyticsContextIdentifier subscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

  /**
   * The identifier of a subscription.
   * @return subscriptionId
   **/
  @Schema(required = true, description = "The identifier of a subscription.")
      @NotNull

    public String getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public AnalyticsContextIdentifier nfAnaCtxts(List<NwdafEvent> nfAnaCtxts) {
    this.nfAnaCtxts = nfAnaCtxts;
    return this;
  }

  public AnalyticsContextIdentifier addNfAnaCtxtsItem(NwdafEvent nfAnaCtxtsItem) {
    if (this.nfAnaCtxts == null) {
      this.nfAnaCtxts = new ArrayList<NwdafEvent>();
    }
    this.nfAnaCtxts.add(nfAnaCtxtsItem);
    return this;
  }

  /**
   * List of analytics types for which NF related analytics contexts can be retrieved. 
   * @return nfAnaCtxts
   **/
  @Schema(description = "List of analytics types for which NF related analytics contexts can be retrieved. ")
      @Valid
  @Size(min=1)   public List<NwdafEvent> getNfAnaCtxts() {
    return nfAnaCtxts;
  }

  public void setNfAnaCtxts(List<NwdafEvent> nfAnaCtxts) {
    this.nfAnaCtxts = nfAnaCtxts;
  }

  public AnalyticsContextIdentifier ueAnaCtxts(List<UeAnalyticsContextDescriptor> ueAnaCtxts) {
    this.ueAnaCtxts = ueAnaCtxts;
    return this;
  }

  public AnalyticsContextIdentifier addUeAnaCtxtsItem(UeAnalyticsContextDescriptor ueAnaCtxtsItem) {
    if (this.ueAnaCtxts == null) {
      this.ueAnaCtxts = new ArrayList<UeAnalyticsContextDescriptor>();
    }
    this.ueAnaCtxts.add(ueAnaCtxtsItem);
    return this;
  }

  /**
   * List of objects that indicate for which SUPI and analytics types combinations analytics  context can be retrieved. 
   * @return ueAnaCtxts
   **/
  @Schema(description = "List of objects that indicate for which SUPI and analytics types combinations analytics  context can be retrieved. ")
      @Valid
  @Size(min=1)   public List<UeAnalyticsContextDescriptor> getUeAnaCtxts() {
    return ueAnaCtxts;
  }

  public void setUeAnaCtxts(List<UeAnalyticsContextDescriptor> ueAnaCtxts) {
    this.ueAnaCtxts = ueAnaCtxts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsContextIdentifier analyticsContextIdentifier = (AnalyticsContextIdentifier) o;
    return Objects.equals(this.subscriptionId, analyticsContextIdentifier.subscriptionId) &&
        Objects.equals(this.nfAnaCtxts, analyticsContextIdentifier.nfAnaCtxts) &&
        Objects.equals(this.ueAnaCtxts, analyticsContextIdentifier.ueAnaCtxts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionId, nfAnaCtxts, ueAnaCtxts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsContextIdentifier {\n");
    
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    nfAnaCtxts: ").append(toIndentedString(nfAnaCtxts)).append("\n");
    sb.append("    ueAnaCtxts: ").append(toIndentedString(ueAnaCtxts)).append("\n");
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
