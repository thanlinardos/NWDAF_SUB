package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents the analytics of the application list used by UE.
 */
@Schema(description = "Represents the analytics of the application list used by UE.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class AppListForUeComm   {
  @JsonProperty("appId")
  private String appId = null;

  @JsonProperty("startTime")
  private OffsetDateTime startTime = null;

  @JsonProperty("appDur")
  private Integer appDur = null;

  @JsonProperty("occurRatio")
  private Integer occurRatio = null;

  @JsonProperty("spatialValidity")
  private NetworkAreaInfo spatialValidity = null;

  public AppListForUeComm appId(String appId) {
    this.appId = appId;
    return this;
  }

  /**
   * String providing an application identifier.
   * @return appId
   **/
  @Schema(required = true, description = "String providing an application identifier.")
      @NotNull

    public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public AppListForUeComm startTime(OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * string with format 'date-time' as defined in OpenAPI.
   * @return startTime
   **/
  @Schema(description = "string with format 'date-time' as defined in OpenAPI.")
  
    @Valid
    public OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public AppListForUeComm appDur(Integer appDur) {
    this.appDur = appDur;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return appDur
   **/
  @Schema(description = "indicating a time in seconds.")
  
    public Integer getAppDur() {
    return appDur;
  }

  public void setAppDur(Integer appDur) {
    this.appDur = appDur;
  }

  public AppListForUeComm occurRatio(Integer occurRatio) {
    this.occurRatio = occurRatio;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return occurRatio
   **/
  @Schema(description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
  
  @Min(1) @Max(100)   public Integer getOccurRatio() {
    return occurRatio;
  }

  public void setOccurRatio(Integer occurRatio) {
    this.occurRatio = occurRatio;
  }

  public AppListForUeComm spatialValidity(NetworkAreaInfo spatialValidity) {
    this.spatialValidity = spatialValidity;
    return this;
  }

  /**
   * Get spatialValidity
   * @return spatialValidity
   **/
  @Schema(description = "")
  
    @Valid
    public NetworkAreaInfo getSpatialValidity() {
    return spatialValidity;
  }

  public void setSpatialValidity(NetworkAreaInfo spatialValidity) {
    this.spatialValidity = spatialValidity;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppListForUeComm appListForUeComm = (AppListForUeComm) o;
    return Objects.equals(this.appId, appListForUeComm.appId) &&
        Objects.equals(this.startTime, appListForUeComm.startTime) &&
        Objects.equals(this.appDur, appListForUeComm.appDur) &&
        Objects.equals(this.occurRatio, appListForUeComm.occurRatio) &&
        Objects.equals(this.spatialValidity, appListForUeComm.spatialValidity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, startTime, appDur, occurRatio, spatialValidity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppListForUeComm {\n");
    
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    appDur: ").append(toIndentedString(appDur)).append("\n");
    sb.append("    occurRatio: ").append(toIndentedString(occurRatio)).append("\n");
    sb.append("    spatialValidity: ").append(toIndentedString(spatialValidity)).append("\n");
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
