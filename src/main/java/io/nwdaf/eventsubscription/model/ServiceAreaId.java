package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contains a Service Area Identifier as defined in 3GPP TS 23.003, clause 12.5.
 */
@Schema(description = "Contains a Service Area Identifier as defined in 3GPP TS 23.003, clause 12.5.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ServiceAreaId   {
  @JsonProperty("plmnId")
  private PlmnId plmnId = null;

  @JsonProperty("lac")
  private String lac = null;

  @JsonProperty("sac")
  private String sac = null;

  public ServiceAreaId plmnId(PlmnId plmnId) {
    this.plmnId = plmnId;
    return this;
  }

  /**
   * Get plmnId
   * @return plmnId
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public PlmnId getPlmnId() {
    return plmnId;
  }

  public void setPlmnId(PlmnId plmnId) {
    this.plmnId = plmnId;
  }

  public ServiceAreaId lac(String lac) {
    this.lac = lac;
    return this;
  }

  /**
   * Location Area Code.
   * @return lac
   **/
  @Schema(required = true, description = "Location Area Code.")
      @NotNull

  @Pattern(regexp="^[A-Fa-f0-9]{4}$")   public String getLac() {
    return lac;
  }

  public void setLac(String lac) {
    this.lac = lac;
  }

  public ServiceAreaId sac(String sac) {
    this.sac = sac;
    return this;
  }

  /**
   * Service Area Code.
   * @return sac
   **/
  @Schema(required = true, description = "Service Area Code.")
      @NotNull

  @Pattern(regexp="^[A-Fa-f0-9]{4}$")   public String getSac() {
    return sac;
  }

  public void setSac(String sac) {
    this.sac = sac;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceAreaId serviceAreaId = (ServiceAreaId) o;
    return Objects.equals(this.plmnId, serviceAreaId.plmnId) &&
        Objects.equals(this.lac, serviceAreaId.lac) &&
        Objects.equals(this.sac, serviceAreaId.sac);
  }

  @Override
  public int hashCode() {
    return Objects.hash(plmnId, lac, sac);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceAreaId {\n");
    
    sb.append("    plmnId: ").append(toIndentedString(plmnId)).append("\n");
    sb.append("    lac: ").append(toIndentedString(lac)).append("\n");
    sb.append("    sac: ").append(toIndentedString(sac)).append("\n");
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
