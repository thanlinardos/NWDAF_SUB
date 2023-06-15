package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.BatteryIndication;
import io.nwdaf.eventsubscription.model.LocationArea;
import io.nwdaf.eventsubscription.model.ScheduledCommunicationTime1;
import io.nwdaf.eventsubscription.model.ScheduledCommunicationType;
import io.nwdaf.eventsubscription.model.StationaryIndication;
import io.nwdaf.eventsubscription.model.TrafficProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ExpectedUeBehaviourData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ExpectedUeBehaviourData   {
  @JsonProperty("stationaryIndication")
  private StationaryIndication stationaryIndication = null;

  @JsonProperty("communicationDurationTime")
  private Integer communicationDurationTime = null;

  @JsonProperty("periodicTime")
  private Integer periodicTime = null;

  @JsonProperty("scheduledCommunicationTime")
  private ScheduledCommunicationTime1 scheduledCommunicationTime = null;

  @JsonProperty("scheduledCommunicationType")
  private ScheduledCommunicationType scheduledCommunicationType = null;

  @JsonProperty("expectedUmts")
  @Valid
  private List<LocationArea> expectedUmts = null;

  @JsonProperty("trafficProfile")
  private TrafficProfile trafficProfile = null;

  @JsonProperty("batteryIndication")
  private BatteryIndication batteryIndication = null;

  @JsonProperty("validityTime")
  private OffsetDateTime validityTime = null;

  public ExpectedUeBehaviourData stationaryIndication(StationaryIndication stationaryIndication) {
    this.stationaryIndication = stationaryIndication;
    return this;
  }

  /**
   * Get stationaryIndication
   * @return stationaryIndication
   **/
  @Schema(description = "")
  
    @Valid
    public StationaryIndication getStationaryIndication() {
    return stationaryIndication;
  }

  public void setStationaryIndication(StationaryIndication stationaryIndication) {
    this.stationaryIndication = stationaryIndication;
  }

  public ExpectedUeBehaviourData communicationDurationTime(Integer communicationDurationTime) {
    this.communicationDurationTime = communicationDurationTime;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return communicationDurationTime
   **/
  @Schema(description = "indicating a time in seconds.")
  
    public Integer getCommunicationDurationTime() {
    return communicationDurationTime;
  }

  public void setCommunicationDurationTime(Integer communicationDurationTime) {
    this.communicationDurationTime = communicationDurationTime;
  }

  public ExpectedUeBehaviourData periodicTime(Integer periodicTime) {
    this.periodicTime = periodicTime;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return periodicTime
   **/
  @Schema(description = "indicating a time in seconds.")
  
    public Integer getPeriodicTime() {
    return periodicTime;
  }

  public void setPeriodicTime(Integer periodicTime) {
    this.periodicTime = periodicTime;
  }

  public ExpectedUeBehaviourData scheduledCommunicationTime(ScheduledCommunicationTime1 scheduledCommunicationTime) {
    this.scheduledCommunicationTime = scheduledCommunicationTime;
    return this;
  }

  /**
   * Get scheduledCommunicationTime
   * @return scheduledCommunicationTime
   **/
  @Schema(description = "")
  
    @Valid
    public ScheduledCommunicationTime1 getScheduledCommunicationTime() {
    return scheduledCommunicationTime;
  }

  public void setScheduledCommunicationTime(ScheduledCommunicationTime1 scheduledCommunicationTime) {
    this.scheduledCommunicationTime = scheduledCommunicationTime;
  }

  public ExpectedUeBehaviourData scheduledCommunicationType(ScheduledCommunicationType scheduledCommunicationType) {
    this.scheduledCommunicationType = scheduledCommunicationType;
    return this;
  }

  /**
   * Get scheduledCommunicationType
   * @return scheduledCommunicationType
   **/
  @Schema(description = "")
  
    @Valid
    public ScheduledCommunicationType getScheduledCommunicationType() {
    return scheduledCommunicationType;
  }

  public void setScheduledCommunicationType(ScheduledCommunicationType scheduledCommunicationType) {
    this.scheduledCommunicationType = scheduledCommunicationType;
  }

  public ExpectedUeBehaviourData expectedUmts(List<LocationArea> expectedUmts) {
    this.expectedUmts = expectedUmts;
    return this;
  }

  public ExpectedUeBehaviourData addExpectedUmtsItem(LocationArea expectedUmtsItem) {
    if (this.expectedUmts == null) {
      this.expectedUmts = new ArrayList<LocationArea>();
    }
    this.expectedUmts.add(expectedUmtsItem);
    return this;
  }

  /**
   * Identifies the UE's expected geographical movement. The attribute is only applicable in 5G.
   * @return expectedUmts
   **/
  @Schema(description = "Identifies the UE's expected geographical movement. The attribute is only applicable in 5G.")
      @Valid
  @Size(min=1)   public List<LocationArea> getExpectedUmts() {
    return expectedUmts;
  }

  public void setExpectedUmts(List<LocationArea> expectedUmts) {
    this.expectedUmts = expectedUmts;
  }

  public ExpectedUeBehaviourData trafficProfile(TrafficProfile trafficProfile) {
    this.trafficProfile = trafficProfile;
    return this;
  }

  /**
   * Get trafficProfile
   * @return trafficProfile
   **/
  @Schema(description = "")
  
    @Valid
    public TrafficProfile getTrafficProfile() {
    return trafficProfile;
  }

  public void setTrafficProfile(TrafficProfile trafficProfile) {
    this.trafficProfile = trafficProfile;
  }

  public ExpectedUeBehaviourData batteryIndication(BatteryIndication batteryIndication) {
    this.batteryIndication = batteryIndication;
    return this;
  }

  /**
   * Get batteryIndication
   * @return batteryIndication
   **/
  @Schema(description = "")
  
    @Valid
    public BatteryIndication getBatteryIndication() {
    return batteryIndication;
  }

  public void setBatteryIndication(BatteryIndication batteryIndication) {
    this.batteryIndication = batteryIndication;
  }

  public ExpectedUeBehaviourData validityTime(OffsetDateTime validityTime) {
    this.validityTime = validityTime;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return validityTime
   **/
  @Schema(description = "string with format 'date-time' as defined in OpenAPI.")
  
    @Valid
    public OffsetDateTime getValidityTime() {
    return validityTime;
  }

  public void setValidityTime(OffsetDateTime validityTime) {
    this.validityTime = validityTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpectedUeBehaviourData expectedUeBehaviourData = (ExpectedUeBehaviourData) o;
    return Objects.equals(this.stationaryIndication, expectedUeBehaviourData.stationaryIndication) &&
        Objects.equals(this.communicationDurationTime, expectedUeBehaviourData.communicationDurationTime) &&
        Objects.equals(this.periodicTime, expectedUeBehaviourData.periodicTime) &&
        Objects.equals(this.scheduledCommunicationTime, expectedUeBehaviourData.scheduledCommunicationTime) &&
        Objects.equals(this.scheduledCommunicationType, expectedUeBehaviourData.scheduledCommunicationType) &&
        Objects.equals(this.expectedUmts, expectedUeBehaviourData.expectedUmts) &&
        Objects.equals(this.trafficProfile, expectedUeBehaviourData.trafficProfile) &&
        Objects.equals(this.batteryIndication, expectedUeBehaviourData.batteryIndication) &&
        Objects.equals(this.validityTime, expectedUeBehaviourData.validityTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stationaryIndication, communicationDurationTime, periodicTime, scheduledCommunicationTime, scheduledCommunicationType, expectedUmts, trafficProfile, batteryIndication, validityTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpectedUeBehaviourData {\n");
    
    sb.append("    stationaryIndication: ").append(toIndentedString(stationaryIndication)).append("\n");
    sb.append("    communicationDurationTime: ").append(toIndentedString(communicationDurationTime)).append("\n");
    sb.append("    periodicTime: ").append(toIndentedString(periodicTime)).append("\n");
    sb.append("    scheduledCommunicationTime: ").append(toIndentedString(scheduledCommunicationTime)).append("\n");
    sb.append("    scheduledCommunicationType: ").append(toIndentedString(scheduledCommunicationType)).append("\n");
    sb.append("    expectedUmts: ").append(toIndentedString(expectedUmts)).append("\n");
    sb.append("    trafficProfile: ").append(toIndentedString(trafficProfile)).append("\n");
    sb.append("    batteryIndication: ").append(toIndentedString(batteryIndication)).append("\n");
    sb.append("    validityTime: ").append(toIndentedString(validityTime)).append("\n");
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
