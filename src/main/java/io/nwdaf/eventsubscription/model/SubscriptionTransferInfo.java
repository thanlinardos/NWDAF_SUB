package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contains information about subscriptions that are requested to be transferred.
 */
@Schema(description = "Contains information about subscriptions that are requested to be transferred.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class SubscriptionTransferInfo   {
  @JsonProperty("transReqType")
  private TransferRequestType transReqType = null;

  @JsonProperty("nwdafEvSub")
  private NnwdafEventsSubscription nwdafEvSub = null;

  @JsonProperty("consumerId")
  private UUID consumerId = null;

  @JsonProperty("contextId")
  private AnalyticsContextIdentifier contextId = null;

  @JsonProperty("sourceNfIds")
  @Valid
  private List<UUID> sourceNfIds = null;

  @JsonProperty("sourceSetIds")
  @Valid
  private List<String> sourceSetIds = null;

  @JsonProperty("modelInfo")
  @Valid
  private List<ModelInfo> modelInfo = null;

  public SubscriptionTransferInfo transReqType(TransferRequestType transReqType) {
    this.transReqType = transReqType;
    return this;
  }

  /**
   * Get transReqType
   * @return transReqType
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public TransferRequestType getTransReqType() {
    return transReqType;
  }

  public void setTransReqType(TransferRequestType transReqType) {
    this.transReqType = transReqType;
  }

  public SubscriptionTransferInfo nwdafEvSub(NnwdafEventsSubscription nwdafEvSub) {
    this.nwdafEvSub = nwdafEvSub;
    return this;
  }

  /**
   * Get nwdafEvSub
   * @return nwdafEvSub
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public NnwdafEventsSubscription getNwdafEvSub() {
    return nwdafEvSub;
  }

  public void setNwdafEvSub(NnwdafEventsSubscription nwdafEvSub) {
    this.nwdafEvSub = nwdafEvSub;
  }

  public SubscriptionTransferInfo consumerId(UUID consumerId) {
    this.consumerId = consumerId;
    return this;
  }

  /**
   * String uniquely identifying a NF instance. The format of the NF Instance ID shall be a  Universally Unique Identifier (UUID) version 4, as described in IETF RFC 4122.  
   * @return consumerId
   **/
  @Schema(required = true, description = "String uniquely identifying a NF instance. The format of the NF Instance ID shall be a  Universally Unique Identifier (UUID) version 4, as described in IETF RFC 4122.  ")
      @NotNull

    @Valid
    public UUID getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(UUID consumerId) {
    this.consumerId = consumerId;
  }

  public SubscriptionTransferInfo contextId(AnalyticsContextIdentifier contextId) {
    this.contextId = contextId;
    return this;
  }

  /**
   * Get contextId
   * @return contextId
   **/
  @Schema(description = "")
  
    @Valid
    public AnalyticsContextIdentifier getContextId() {
    return contextId;
  }

  public void setContextId(AnalyticsContextIdentifier contextId) {
    this.contextId = contextId;
  }

  public SubscriptionTransferInfo sourceNfIds(List<UUID> sourceNfIds) {
    this.sourceNfIds = sourceNfIds;
    return this;
  }

  public SubscriptionTransferInfo addSourceNfIdsItem(UUID sourceNfIdsItem) {
    if (this.sourceNfIds == null) {
      this.sourceNfIds = new ArrayList<UUID>();
    }
    this.sourceNfIds.add(sourceNfIdsItem);
    return this;
  }

  /**
   * Get sourceNfIds
   * @return sourceNfIds
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<UUID> getSourceNfIds() {
    return sourceNfIds;
  }

  public void setSourceNfIds(List<UUID> sourceNfIds) {
    this.sourceNfIds = sourceNfIds;
  }

  public SubscriptionTransferInfo sourceSetIds(List<String> sourceSetIds) {
    this.sourceSetIds = sourceSetIds;
    return this;
  }

  public SubscriptionTransferInfo addSourceSetIdsItem(String sourceSetIdsItem) {
    if (this.sourceSetIds == null) {
      this.sourceSetIds = new ArrayList<String>();
    }
    this.sourceSetIds.add(sourceSetIdsItem);
    return this;
  }

  /**
   * Get sourceSetIds
   * @return sourceSetIds
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getSourceSetIds() {
    return sourceSetIds;
  }

  public void setSourceSetIds(List<String> sourceSetIds) {
    this.sourceSetIds = sourceSetIds;
  }

  public SubscriptionTransferInfo modelInfo(List<ModelInfo> modelInfo) {
    this.modelInfo = modelInfo;
    return this;
  }

  public SubscriptionTransferInfo addModelInfoItem(ModelInfo modelInfoItem) {
    if (this.modelInfo == null) {
      this.modelInfo = new ArrayList<ModelInfo>();
    }
    this.modelInfo.add(modelInfoItem);
    return this;
  }

  /**
   * Get modelInfo
   * @return modelInfo
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<ModelInfo> getModelInfo() {
    return modelInfo;
  }

  public void setModelInfo(List<ModelInfo> modelInfo) {
    this.modelInfo = modelInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscriptionTransferInfo subscriptionTransferInfo = (SubscriptionTransferInfo) o;
    return Objects.equals(this.transReqType, subscriptionTransferInfo.transReqType) &&
        Objects.equals(this.nwdafEvSub, subscriptionTransferInfo.nwdafEvSub) &&
        Objects.equals(this.consumerId, subscriptionTransferInfo.consumerId) &&
        Objects.equals(this.contextId, subscriptionTransferInfo.contextId) &&
        Objects.equals(this.sourceNfIds, subscriptionTransferInfo.sourceNfIds) &&
        Objects.equals(this.sourceSetIds, subscriptionTransferInfo.sourceSetIds) &&
        Objects.equals(this.modelInfo, subscriptionTransferInfo.modelInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transReqType, nwdafEvSub, consumerId, contextId, sourceNfIds, sourceSetIds, modelInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriptionTransferInfo {\n");
    
    sb.append("    transReqType: ").append(toIndentedString(transReqType)).append("\n");
    sb.append("    nwdafEvSub: ").append(toIndentedString(nwdafEvSub)).append("\n");
    sb.append("    consumerId: ").append(toIndentedString(consumerId)).append("\n");
    sb.append("    contextId: ").append(toIndentedString(contextId)).append("\n");
    sb.append("    sourceNfIds: ").append(toIndentedString(sourceNfIds)).append("\n");
    sb.append("    sourceSetIds: ").append(toIndentedString(sourceSetIds)).append("\n");
    sb.append("    modelInfo: ").append(toIndentedString(modelInfo)).append("\n");
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
