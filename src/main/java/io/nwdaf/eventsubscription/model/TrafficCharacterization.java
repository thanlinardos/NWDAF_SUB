package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.IpEthFlowDescription;
import io.nwdaf.eventsubscription.model.Snssai;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Identifies the detailed traffic characterization.
 */
@Schema(description = "Identifies the detailed traffic characterization.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class TrafficCharacterization  implements AnyOfTrafficCharacterization {
  @JsonProperty("dnn")
  private String dnn = null;

  @JsonProperty("snssai")
  private Snssai snssai = null;

  @JsonProperty("appId")
  private String appId = null;

  @JsonProperty("fDescs")
  @Valid
  private List<IpEthFlowDescription> fDescs = null;

  @JsonProperty("ulVol")
  private Long ulVol = null;

  @JsonProperty("ulVolVariance")
  private Float ulVolVariance = null;

  @JsonProperty("dlVol")
  private Long dlVol = null;

  @JsonProperty("dlVolVariance")
  private Float dlVolVariance = null;

  public TrafficCharacterization dnn(String dnn) {
    this.dnn = dnn;
    return this;
  }

  /**
   * String representing a Data Network as defined in clause 9A of 3GPP TS 23.003;  it shall contain either a DNN Network Identifier, or a full DNN with both the Network  Identifier and Operator Identifier, as specified in 3GPP TS 23.003 clause 9.1.1 and 9.1.2. It shall be coded as string in which the labels are separated by dots  (e.g. \"Label1.Label2.Label3\"). 
   * @return dnn
   **/
  @Schema(description = "String representing a Data Network as defined in clause 9A of 3GPP TS 23.003;  it shall contain either a DNN Network Identifier, or a full DNN with both the Network  Identifier and Operator Identifier, as specified in 3GPP TS 23.003 clause 9.1.1 and 9.1.2. It shall be coded as string in which the labels are separated by dots  (e.g. \"Label1.Label2.Label3\"). ")
  
    public String getDnn() {
    return dnn;
  }

  public void setDnn(String dnn) {
    this.dnn = dnn;
  }

  public TrafficCharacterization snssai(Snssai snssai) {
    this.snssai = snssai;
    return this;
  }

  /**
   * Get snssai
   * @return snssai
   **/
  @Schema(description = "")
  
    @Valid
    public Snssai getSnssai() {
    return snssai;
  }

  public void setSnssai(Snssai snssai) {
    this.snssai = snssai;
  }

  public TrafficCharacterization appId(String appId) {
    this.appId = appId;
    return this;
  }

  /**
   * String providing an application identifier.
   * @return appId
   **/
  @Schema(description = "String providing an application identifier.")
  
    public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public TrafficCharacterization fDescs(List<IpEthFlowDescription> fDescs) {
    this.fDescs = fDescs;
    return this;
  }

  public TrafficCharacterization addFDescsItem(IpEthFlowDescription fDescsItem) {
    if (this.fDescs == null) {
      this.fDescs = new ArrayList<IpEthFlowDescription>();
    }
    this.fDescs.add(fDescsItem);
    return this;
  }

  /**
   * Get fDescs
   * @return fDescs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1,max=2)   public List<IpEthFlowDescription> getFDescs() {
    return fDescs;
  }

  public void setFDescs(List<IpEthFlowDescription> fDescs) {
    this.fDescs = fDescs;
  }

  public TrafficCharacterization ulVol(Long ulVol) {
    this.ulVol = ulVol;
    return this;
  }

  /**
   * Unsigned integer identifying a volume in units of bytes.
   * minimum: 0
   * @return ulVol
   **/
  @Schema(description = "Unsigned integer identifying a volume in units of bytes.")
  
  @Min(0L)  public Long getUlVol() {
    return ulVol;
  }

  public void setUlVol(Long ulVol) {
    this.ulVol = ulVol;
  }

  public TrafficCharacterization ulVolVariance(Float ulVolVariance) {
    this.ulVolVariance = ulVolVariance;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return ulVolVariance
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getUlVolVariance() {
    return ulVolVariance;
  }

  public void setUlVolVariance(Float ulVolVariance) {
    this.ulVolVariance = ulVolVariance;
  }

  public TrafficCharacterization dlVol(Long dlVol) {
    this.dlVol = dlVol;
    return this;
  }

  /**
   * Unsigned integer identifying a volume in units of bytes.
   * minimum: 0
   * @return dlVol
   **/
  @Schema(description = "Unsigned integer identifying a volume in units of bytes.")
  
  @Min(0L)  public Long getDlVol() {
    return dlVol;
  }

  public void setDlVol(Long dlVol) {
    this.dlVol = dlVol;
  }

  public TrafficCharacterization dlVolVariance(Float dlVolVariance) {
    this.dlVolVariance = dlVolVariance;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return dlVolVariance
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getDlVolVariance() {
    return dlVolVariance;
  }

  public void setDlVolVariance(Float dlVolVariance) {
    this.dlVolVariance = dlVolVariance;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrafficCharacterization trafficCharacterization = (TrafficCharacterization) o;
    return Objects.equals(this.dnn, trafficCharacterization.dnn) &&
        Objects.equals(this.snssai, trafficCharacterization.snssai) &&
        Objects.equals(this.appId, trafficCharacterization.appId) &&
        Objects.equals(this.fDescs, trafficCharacterization.fDescs) &&
        Objects.equals(this.ulVol, trafficCharacterization.ulVol) &&
        Objects.equals(this.ulVolVariance, trafficCharacterization.ulVolVariance) &&
        Objects.equals(this.dlVol, trafficCharacterization.dlVol) &&
        Objects.equals(this.dlVolVariance, trafficCharacterization.dlVolVariance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dnn, snssai, appId, fDescs, ulVol, ulVolVariance, dlVol, dlVolVariance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrafficCharacterization {\n");
    
    sb.append("    dnn: ").append(toIndentedString(dnn)).append("\n");
    sb.append("    snssai: ").append(toIndentedString(snssai)).append("\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    fDescs: ").append(toIndentedString(fDescs)).append("\n");
    sb.append("    ulVol: ").append(toIndentedString(ulVol)).append("\n");
    sb.append("    ulVolVariance: ").append(toIndentedString(ulVolVariance)).append("\n");
    sb.append("    dlVol: ").append(toIndentedString(dlVol)).append("\n");
    sb.append("    dlVolVariance: ").append(toIndentedString(dlVolVariance)).append("\n");
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
