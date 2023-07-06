package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.vladmihalcea.hibernate.type.json.JsonType;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;



import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents an Individual NWDAF Event Subscription resource.
 */
@Schema(description = "Represents an Individual NWDAF Event Subscription resource.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")

public class NnwdafEventsSubscription   {

  @JsonProperty("eventSubscriptions")
  @Valid
  private List<EventSubscription> eventSubscriptions = new ArrayList<EventSubscription>();

  @JsonProperty("evtReq")
  private ReportingInformation evtReq = null;

  @JsonProperty("notificationURI")
  private String notificationURI = null;

  @JsonProperty("notifCorrId")
  private String notifCorrId = null;

  @JsonProperty("supportedFeatures")
  private String supportedFeatures = null;

  @JsonProperty("eventNotifications")
  @Valid
  private List<EventNotification> eventNotifications = null;

  @JsonProperty("failEventReports")
  @Valid
  private List<FailureEventInfo> failEventReports = null;

  @JsonProperty("prevSub")
  private PrevSubInfo prevSub = null;

  @JsonProperty("consNfInfo")
  private ConsumerNfInformation consNfInfo = null;

  public NnwdafEventsSubscription eventSubscriptions(List<EventSubscription> eventSubscriptions) {
    this.eventSubscriptions = eventSubscriptions;
    return this;
  }

  public NnwdafEventsSubscription addEventSubscriptionsItem(EventSubscription eventSubscriptionsItem) {
    this.eventSubscriptions.add(eventSubscriptionsItem);
    return this;
  }
  
  
  /**
   * Subscribed events
   * @return eventSubscriptions
   **/
  @Schema(required = true, description = "Subscribed events")
      @NotNull
    @Valid
  @Size(min=1)   public List<EventSubscription> getEventSubscriptions() {
    return eventSubscriptions;
  }

  public void setEventSubscriptions(List<EventSubscription> eventSubscriptions) {
    this.eventSubscriptions = eventSubscriptions;
  }

  public NnwdafEventsSubscription evtReq(ReportingInformation evtReq) {
    this.evtReq = evtReq;
    return this;
  }

  /**
   * Get evtReq
   * @return evtReq
   **/
  @Schema(description = "")
  
    @Valid
    public ReportingInformation getEvtReq() {
    return evtReq;
  }

  public void setEvtReq(ReportingInformation evtReq) {
    this.evtReq = evtReq;
  }

  public NnwdafEventsSubscription notificationURI(String notificationURI) {
    this.notificationURI = notificationURI;
    return this;
  }

  /**
   * String providing an URI formatted according to RFC 3986.
   * @return notificationURI
   **/
  @Schema(description = "String providing an URI formatted according to RFC 3986.")
  
    public String getNotificationURI() {
    return notificationURI;
  }

  public void setNotificationURI(String notificationURI) {
    this.notificationURI = notificationURI;
  }

  public NnwdafEventsSubscription notifCorrId(String notifCorrId) {
    this.notifCorrId = notifCorrId;
    return this;
  }

  /**
   * Notification correlation identifier.
   * @return notifCorrId
   **/
  @Schema(description = "Notification correlation identifier.")
  
    public String getNotifCorrId() {
    return notifCorrId;
  }

  public void setNotifCorrId(String notifCorrId) {
    this.notifCorrId = notifCorrId;
  }

  public NnwdafEventsSubscription supportedFeatures(String supportedFeatures) {
    this.supportedFeatures = supportedFeatures;
    return this;
  }

  /**
   * A string used to indicate the features supported by an API that is used as defined in clause  6.6 in 3GPP TS 29.500. The string shall contain a bitmask indicating supported features in  hexadecimal representation Each character in the string shall take a value of \"0\" to \"9\",  \"a\" to \"f\" or \"A\" to \"F\" and shall represent the support of 4 features as described in  table 5.2.2-3. The most significant character representing the highest-numbered features shall  appear first in the string, and the character representing features 1 to 4 shall appear last  in the string. The list of features and their numbering (starting with 1) are defined  separately for each API. If the string contains a lower number of characters than there are  defined features for an API, all features that would be represented by characters that are not  present in the string are not supported. 
   * @return supportedFeatures
   **/
  @Schema(description = "A string used to indicate the features supported by an API that is used as defined in clause  6.6 in 3GPP TS 29.500. The string shall contain a bitmask indicating supported features in  hexadecimal representation Each character in the string shall take a value of \"0\" to \"9\",  \"a\" to \"f\" or \"A\" to \"F\" and shall represent the support of 4 features as described in  table 5.2.2-3. The most significant character representing the highest-numbered features shall  appear first in the string, and the character representing features 1 to 4 shall appear last  in the string. The list of features and their numbering (starting with 1) are defined  separately for each API. If the string contains a lower number of characters than there are  defined features for an API, all features that would be represented by characters that are not  present in the string are not supported. ")
  
  @Pattern(regexp="^[A-Fa-f0-9]*$")   public String getSupportedFeatures() {
    return supportedFeatures;
  }

  public void setSupportedFeatures(String supportedFeatures) {
    this.supportedFeatures = supportedFeatures;
  }

  public NnwdafEventsSubscription eventNotifications(List<EventNotification> eventNotifications) {
    this.eventNotifications = eventNotifications;
    return this;
  }

  public NnwdafEventsSubscription addEventNotificationsItem(EventNotification eventNotificationsItem) {
    if (this.eventNotifications == null) {
      this.eventNotifications = new ArrayList<EventNotification>();
    }
    this.eventNotifications.add(eventNotificationsItem);
    return this;
  }

  /**
   * Get eventNotifications
   * @return eventNotifications
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<EventNotification> getEventNotifications() {
    return eventNotifications;
  }

  public void setEventNotifications(List<EventNotification> eventNotifications) {
    this.eventNotifications = eventNotifications;
  }

  public NnwdafEventsSubscription failEventReports(List<FailureEventInfo> failEventReports) {
    this.failEventReports = failEventReports;
    return this;
  }

  public NnwdafEventsSubscription addFailEventReportsItem(FailureEventInfo failEventReportsItem) {
    if (this.failEventReports == null) {
      this.failEventReports = new ArrayList<FailureEventInfo>();
    }
    this.failEventReports.add(failEventReportsItem);
    return this;
  }

  /**
   * Get failEventReports
   * @return failEventReports
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<FailureEventInfo> getFailEventReports() {
    return failEventReports;
  }

  public void setFailEventReports(List<FailureEventInfo> failEventReports) {
    this.failEventReports = failEventReports;
  }

  public NnwdafEventsSubscription prevSub(PrevSubInfo prevSub) {
    this.prevSub = prevSub;
    return this;
  }

  /**
   * Get prevSub
   * @return prevSub
   **/
  @Schema(description = "")
  
    @Valid
    public PrevSubInfo getPrevSub() {
    return prevSub;
  }

  public void setPrevSub(PrevSubInfo prevSub) {
    this.prevSub = prevSub;
  }

  public NnwdafEventsSubscription consNfInfo(ConsumerNfInformation consNfInfo) {
    this.consNfInfo = consNfInfo;
    return this;
  }

  /**
   * Get consNfInfo
   * @return consNfInfo
   **/
  @Schema(description = "")
  
    @Valid
    public ConsumerNfInformation getConsNfInfo() {
    return consNfInfo;
  }

  public void setConsNfInfo(ConsumerNfInformation consNfInfo) {
    this.consNfInfo = consNfInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NnwdafEventsSubscription nnwdafEventsSubscription = (NnwdafEventsSubscription) o;
    return Objects.equals(this.eventSubscriptions, nnwdafEventsSubscription.eventSubscriptions) &&
        Objects.equals(this.evtReq, nnwdafEventsSubscription.evtReq) &&
        Objects.equals(this.notificationURI, nnwdafEventsSubscription.notificationURI) &&
        Objects.equals(this.notifCorrId, nnwdafEventsSubscription.notifCorrId) &&
        Objects.equals(this.supportedFeatures, nnwdafEventsSubscription.supportedFeatures) &&
        Objects.equals(this.eventNotifications, nnwdafEventsSubscription.eventNotifications) &&
        Objects.equals(this.failEventReports, nnwdafEventsSubscription.failEventReports) &&
        Objects.equals(this.prevSub, nnwdafEventsSubscription.prevSub) &&
        Objects.equals(this.consNfInfo, nnwdafEventsSubscription.consNfInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventSubscriptions, evtReq, notificationURI, notifCorrId, supportedFeatures, eventNotifications, failEventReports, prevSub, consNfInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NnwdafEventsSubscription {\n");
    
    sb.append("    eventSubscriptions: ").append(toIndentedString(eventSubscriptions)).append("\n");
    sb.append("    evtReq: ").append(toIndentedString(evtReq)).append("\n");
    sb.append("    notificationURI: ").append(toIndentedString(notificationURI)).append("\n");
    sb.append("    notifCorrId: ").append(toIndentedString(notifCorrId)).append("\n");
    sb.append("    supportedFeatures: ").append(toIndentedString(supportedFeatures)).append("\n");
    sb.append("    eventNotifications: ").append(toIndentedString(eventNotifications)).append("\n");
    sb.append("    failEventReports: ").append(toIndentedString(failEventReports)).append("\n");
    sb.append("    prevSub: ").append(toIndentedString(prevSub)).append("\n");
    sb.append("    consNfInfo: ").append(toIndentedString(consNfInfo)).append("\n");
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
