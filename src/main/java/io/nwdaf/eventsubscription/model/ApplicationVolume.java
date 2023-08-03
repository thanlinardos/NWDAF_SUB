package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Application data volume per Application Id.
 */
@Schema(description = "Application data volume per Application Id.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ApplicationVolume   {
  @JsonProperty("appId")
  private String appId = null;

  @JsonProperty("appVolume")
  private Long appVolume = null;

  public ApplicationVolume appId(String appId) {
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

  public ApplicationVolume appVolume(Long appVolume) {
    this.appVolume = appVolume;
    return this;
  }

  /**
   * Unsigned integer identifying a volume in units of bytes.
   * minimum: 0
   * @return appVolume
   **/
  @Schema(required = true, description = "Unsigned integer identifying a volume in units of bytes.")
      @NotNull

  @Min(0L)  public Long getAppVolume() {
    return appVolume;
  }

  public void setAppVolume(Long appVolume) {
    this.appVolume = appVolume;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicationVolume applicationVolume = (ApplicationVolume) o;
    return Objects.equals(this.appId, applicationVolume.appId) &&
        Objects.equals(this.appVolume, applicationVolume.appVolume);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, appVolume);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicationVolume {\n");
    
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    appVolume: ").append(toIndentedString(appVolume)).append("\n");
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
