package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.UeAnalyticsContextDescriptor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Information of the previous subscription.
 */
@Schema(description = "Information of the previous subscription.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class PrevSubInfo  implements OneOfPrevSubInfo {
  @JsonProperty("producerId")
  private UUID producerId = null;

  @JsonProperty("producerSetId")
  private String producerSetId = null;

  @JsonProperty("subscriptionId")
  private String subscriptionId = null;

  @JsonProperty("nfAnaEvents")
  @Valid
  private List<NwdafEvent> nfAnaEvents = null;

  @JsonProperty("ueAnaEvents")
  @Valid
  private List<UeAnalyticsContextDescriptor> ueAnaEvents = null;

  public PrevSubInfo producerId(UUID producerId) {
    this.producerId = producerId;
    return this;
  }

  /**
   * String uniquely identifying a NF instance. The format of the NF Instance ID shall be a  Universally Unique Identifier (UUID) version 4, as described in IETF RFC 4122.  
   * @return producerId
   **/
  @Schema(description = "String uniquely identifying a NF instance. The format of the NF Instance ID shall be a  Universally Unique Identifier (UUID) version 4, as described in IETF RFC 4122.  ")
  
    @Valid
    public UUID getProducerId() {
    return producerId;
  }

  public void setProducerId(UUID producerId) {
    this.producerId = producerId;
  }

  public PrevSubInfo producerSetId(String producerSetId) {
    this.producerSetId = producerSetId;
    return this;
  }

  /**
   * NF Set Identifier (see clause 28.12 of 3GPP TS 23.003), formatted as the following string \"set<Set ID>.<nftype>set.5gc.mnc<MNC>.mcc<MCC>\", or  \"set<SetID>.<NFType>set.5gc.nid<NID>.mnc<MNC>.mcc<MCC>\" with  <MCC> encoded as defined in clause 5.4.2 (\"Mcc\" data type definition)  <MNC> encoding the Mobile Network Code part of the PLMN, comprising 3 digits.    If there are only 2 significant digits in the MNC, one \"0\" digit shall be inserted    at the left side to fill the 3 digits coding of MNC.  Pattern: '^[0-9]{3}$' <NFType> encoded as a value defined in Table 6.1.6.3.3-1 of 3GPP TS 29.510 but    with lower case characters <Set ID> encoded as a string of characters consisting of    alphabetic characters (A-Z and a-z), digits (0-9) and/or the hyphen (-) and that    shall end with either an alphabetic character or a digit.  
   * @return producerSetId
   **/
  @Schema(description = "NF Set Identifier (see clause 28.12 of 3GPP TS 23.003), formatted as the following string \"set<Set ID>.<nftype>set.5gc.mnc<MNC>.mcc<MCC>\", or  \"set<SetID>.<NFType>set.5gc.nid<NID>.mnc<MNC>.mcc<MCC>\" with  <MCC> encoded as defined in clause 5.4.2 (\"Mcc\" data type definition)  <MNC> encoding the Mobile Network Code part of the PLMN, comprising 3 digits.    If there are only 2 significant digits in the MNC, one \"0\" digit shall be inserted    at the left side to fill the 3 digits coding of MNC.  Pattern: '^[0-9]{3}$' <NFType> encoded as a value defined in Table 6.1.6.3.3-1 of 3GPP TS 29.510 but    with lower case characters <Set ID> encoded as a string of characters consisting of    alphabetic characters (A-Z and a-z), digits (0-9) and/or the hyphen (-) and that    shall end with either an alphabetic character or a digit.  ")
  
    public String getProducerSetId() {
    return producerSetId;
  }

  public void setProducerSetId(String producerSetId) {
    this.producerSetId = producerSetId;
  }

  public PrevSubInfo subscriptionId(String subscriptionId) {
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

  public PrevSubInfo nfAnaEvents(List<NwdafEvent> nfAnaEvents) {
    this.nfAnaEvents = nfAnaEvents;
    return this;
  }

  public PrevSubInfo addNfAnaEventsItem(NwdafEvent nfAnaEventsItem) {
    if (this.nfAnaEvents == null) {
      this.nfAnaEvents = new ArrayList<NwdafEvent>();
    }
    this.nfAnaEvents.add(nfAnaEventsItem);
    return this;
  }

  /**
   * Get nfAnaEvents
   * @return nfAnaEvents
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<NwdafEvent> getNfAnaEvents() {
    return nfAnaEvents;
  }

  public void setNfAnaEvents(List<NwdafEvent> nfAnaEvents) {
    this.nfAnaEvents = nfAnaEvents;
  }

  public PrevSubInfo ueAnaEvents(List<UeAnalyticsContextDescriptor> ueAnaEvents) {
    this.ueAnaEvents = ueAnaEvents;
    return this;
  }

  public PrevSubInfo addUeAnaEventsItem(UeAnalyticsContextDescriptor ueAnaEventsItem) {
    if (this.ueAnaEvents == null) {
      this.ueAnaEvents = new ArrayList<UeAnalyticsContextDescriptor>();
    }
    this.ueAnaEvents.add(ueAnaEventsItem);
    return this;
  }

  /**
   * Get ueAnaEvents
   * @return ueAnaEvents
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<UeAnalyticsContextDescriptor> getUeAnaEvents() {
    return ueAnaEvents;
  }

  public void setUeAnaEvents(List<UeAnalyticsContextDescriptor> ueAnaEvents) {
    this.ueAnaEvents = ueAnaEvents;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PrevSubInfo prevSubInfo = (PrevSubInfo) o;
    return Objects.equals(this.producerId, prevSubInfo.producerId) &&
        Objects.equals(this.producerSetId, prevSubInfo.producerSetId) &&
        Objects.equals(this.subscriptionId, prevSubInfo.subscriptionId) &&
        Objects.equals(this.nfAnaEvents, prevSubInfo.nfAnaEvents) &&
        Objects.equals(this.ueAnaEvents, prevSubInfo.ueAnaEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(producerId, producerSetId, subscriptionId, nfAnaEvents, ueAnaEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrevSubInfo {\n");
    
    sb.append("    producerId: ").append(toIndentedString(producerId)).append("\n");
    sb.append("    producerSetId: ").append(toIndentedString(producerSetId)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    nfAnaEvents: ").append(toIndentedString(nfAnaEvents)).append("\n");
    sb.append("    ueAnaEvents: ").append(toIndentedString(ueAnaEvents)).append("\n");
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
