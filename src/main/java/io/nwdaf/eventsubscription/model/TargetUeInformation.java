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
 * Identifies the target UE information.
 */
@Schema(description = "Identifies the target UE information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class TargetUeInformation   {
  @JsonProperty("anyUe")
  private Boolean anyUe = null;

  @JsonProperty("supis")
  @Valid
  private List<String> supis = null;

  @JsonProperty("gpsis")
  @Valid
  private List<String> gpsis = null;

  @JsonProperty("intGroupIds")
  @Valid
  private List<String> intGroupIds = null;

  public TargetUeInformation anyUe(Boolean anyUe) {
    this.anyUe = anyUe;
    return this;
  }

  /**
   * Get anyUe
   * @return anyUe
   **/
  @Schema(description = "")
  
    public Boolean isAnyUe() {
    return anyUe;
  }

  public void setAnyUe(Boolean anyUe) {
    this.anyUe = anyUe;
  }

  public TargetUeInformation supis(List<String> supis) {
    this.supis = supis;
    return this;
  }

  public TargetUeInformation addSupisItem(String supisItem) {
    if (this.supis == null) {
      this.supis = new ArrayList<String>();
    }
    this.supis.add(supisItem);
    return this;
  }

  /**
   * Get supis
   * @return supis
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getSupis() {
    return supis;
  }

  public void setSupis(List<String> supis) {
    this.supis = supis;
  }

  public TargetUeInformation gpsis(List<String> gpsis) {
    this.gpsis = gpsis;
    return this;
  }

  public TargetUeInformation addGpsisItem(String gpsisItem) {
    if (this.gpsis == null) {
      this.gpsis = new ArrayList<String>();
    }
    this.gpsis.add(gpsisItem);
    return this;
  }

  /**
   * Get gpsis
   * @return gpsis
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getGpsis() {
    return gpsis;
  }

  public void setGpsis(List<String> gpsis) {
    this.gpsis = gpsis;
  }

  public TargetUeInformation intGroupIds(List<String> intGroupIds) {
    this.intGroupIds = intGroupIds;
    return this;
  }

  public TargetUeInformation addIntGroupIdsItem(String intGroupIdsItem) {
    if (this.intGroupIds == null) {
      this.intGroupIds = new ArrayList<String>();
    }
    this.intGroupIds.add(intGroupIdsItem);
    return this;
  }

  /**
   * Get intGroupIds
   * @return intGroupIds
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getIntGroupIds() {
    return intGroupIds;
  }

  public void setIntGroupIds(List<String> intGroupIds) {
    this.intGroupIds = intGroupIds;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TargetUeInformation targetUeInformation = (TargetUeInformation) o;
    return Objects.equals(this.anyUe, targetUeInformation.anyUe) &&
        Objects.equals(this.supis, targetUeInformation.supis) &&
        Objects.equals(this.gpsis, targetUeInformation.gpsis) &&
        Objects.equals(this.intGroupIds, targetUeInformation.intGroupIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anyUe, supis, gpsis, intGroupIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TargetUeInformation {\n");
    
    sb.append("    anyUe: ").append(toIndentedString(anyUe)).append("\n");
    sb.append("    supis: ").append(toIndentedString(supis)).append("\n");
    sb.append("    gpsis: ").append(toIndentedString(gpsis)).append("\n");
    sb.append("    intGroupIds: ").append(toIndentedString(intGroupIds)).append("\n");
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
