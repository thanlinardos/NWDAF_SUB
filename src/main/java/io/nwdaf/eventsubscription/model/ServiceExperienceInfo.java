package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.AddrFqdn;
import io.nwdaf.eventsubscription.model.LocationInfo;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.RatFreqInformation;
import io.nwdaf.eventsubscription.model.ServiceExperienceType;
import io.nwdaf.eventsubscription.model.Snssai;
import io.nwdaf.eventsubscription.model.SvcExperience;
import io.nwdaf.eventsubscription.model.UpfInformation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents service experience information.
 */
@Schema(description = "Represents service experience information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class ServiceExperienceInfo   {
  @JsonProperty("svcExprc")
  private SvcExperience svcExprc = null;

  @JsonProperty("svcExprcVariance")
  private Float svcExprcVariance = null;

  @JsonProperty("supis")
  @Valid
  private List<String> supis = null;

  @JsonProperty("snssai")
  private Snssai snssai = null;

  @JsonProperty("appId")
  private String appId = null;

  @JsonProperty("srvExpcType")
  private ServiceExperienceType srvExpcType = null;

  @JsonProperty("ueLocs")
  @Valid
  private List<LocationInfo> ueLocs = null;

  @JsonProperty("upfInfo")
  private UpfInformation upfInfo = null;

  @JsonProperty("dnai")
  private String dnai = null;

  @JsonProperty("appServerInst")
  private AddrFqdn appServerInst = null;

  @JsonProperty("confidence")
  private Integer confidence = null;

  @JsonProperty("dnn")
  private String dnn = null;

  @JsonProperty("networkArea")
  private NetworkAreaInfo networkArea = null;

  @JsonProperty("nsiId")
  private String nsiId = null;

  @JsonProperty("ratio")
  private Integer ratio = null;

  @JsonProperty("ratFreq")
  private RatFreqInformation ratFreq = null;

  public ServiceExperienceInfo svcExprc(SvcExperience svcExprc) {
    this.svcExprc = svcExprc;
    return this;
  }

  /**
   * Get svcExprc
   * @return svcExprc
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public SvcExperience getSvcExprc() {
    return svcExprc;
  }

  public void setSvcExprc(SvcExperience svcExprc) {
    this.svcExprc = svcExprc;
  }

  public ServiceExperienceInfo svcExprcVariance(Float svcExprcVariance) {
    this.svcExprcVariance = svcExprcVariance;
    return this;
  }

  /**
   * string with format 'float' as defined in OpenAPI.
   * @return svcExprcVariance
   **/
  @Schema(description = "string with format 'float' as defined in OpenAPI.")
  
    public Float getSvcExprcVariance() {
    return svcExprcVariance;
  }

  public void setSvcExprcVariance(Float svcExprcVariance) {
    this.svcExprcVariance = svcExprcVariance;
  }

  public ServiceExperienceInfo supis(List<String> supis) {
    this.supis = supis;
    return this;
  }

  public ServiceExperienceInfo addSupisItem(String supisItem) {
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

  public ServiceExperienceInfo snssai(Snssai snssai) {
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

  public ServiceExperienceInfo appId(String appId) {
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

  public ServiceExperienceInfo srvExpcType(ServiceExperienceType srvExpcType) {
    this.srvExpcType = srvExpcType;
    return this;
  }

  /**
   * Get srvExpcType
   * @return srvExpcType
   **/
  @Schema(description = "")
  
    @Valid
    public ServiceExperienceType getSrvExpcType() {
    return srvExpcType;
  }

  public void setSrvExpcType(ServiceExperienceType srvExpcType) {
    this.srvExpcType = srvExpcType;
  }

  public ServiceExperienceInfo ueLocs(List<LocationInfo> ueLocs) {
    this.ueLocs = ueLocs;
    return this;
  }

  public ServiceExperienceInfo addUeLocsItem(LocationInfo ueLocsItem) {
    if (this.ueLocs == null) {
      this.ueLocs = new ArrayList<LocationInfo>();
    }
    this.ueLocs.add(ueLocsItem);
    return this;
  }

  /**
   * Get ueLocs
   * @return ueLocs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<LocationInfo> getUeLocs() {
    return ueLocs;
  }

  public void setUeLocs(List<LocationInfo> ueLocs) {
    this.ueLocs = ueLocs;
  }

  public ServiceExperienceInfo upfInfo(UpfInformation upfInfo) {
    this.upfInfo = upfInfo;
    return this;
  }

  /**
   * Get upfInfo
   * @return upfInfo
   **/
  @Schema(description = "")
  
    @Valid
    public UpfInformation getUpfInfo() {
    return upfInfo;
  }

  public void setUpfInfo(UpfInformation upfInfo) {
    this.upfInfo = upfInfo;
  }

  public ServiceExperienceInfo dnai(String dnai) {
    this.dnai = dnai;
    return this;
  }

  /**
   * DNAI (Data network access identifier), see clause 5.6.7 of 3GPP TS 23.501.
   * @return dnai
   **/
  @Schema(description = "DNAI (Data network access identifier), see clause 5.6.7 of 3GPP TS 23.501.")
  
    public String getDnai() {
    return dnai;
  }

  public void setDnai(String dnai) {
    this.dnai = dnai;
  }

  public ServiceExperienceInfo appServerInst(AddrFqdn appServerInst) {
    this.appServerInst = appServerInst;
    return this;
  }

  /**
   * Get appServerInst
   * @return appServerInst
   **/
  @Schema(description = "")
  
    @Valid
    public AddrFqdn getAppServerInst() {
    return appServerInst;
  }

  public void setAppServerInst(AddrFqdn appServerInst) {
    this.appServerInst = appServerInst;
  }

  public ServiceExperienceInfo confidence(Integer confidence) {
    this.confidence = confidence;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return confidence
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getConfidence() {
    return confidence;
  }

  public void setConfidence(Integer confidence) {
    this.confidence = confidence;
  }

  public ServiceExperienceInfo dnn(String dnn) {
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

  public ServiceExperienceInfo networkArea(NetworkAreaInfo networkArea) {
    this.networkArea = networkArea;
    return this;
  }

  /**
   * Get networkArea
   * @return networkArea
   **/
  @Schema(description = "")
  
    @Valid
    public NetworkAreaInfo getNetworkArea() {
    return networkArea;
  }

  public void setNetworkArea(NetworkAreaInfo networkArea) {
    this.networkArea = networkArea;
  }

  public ServiceExperienceInfo nsiId(String nsiId) {
    this.nsiId = nsiId;
    return this;
  }

  /**
   * Contains the Identifier of the selected Network Slice instance
   * @return nsiId
   **/
  @Schema(description = "Contains the Identifier of the selected Network Slice instance")
  
    public String getNsiId() {
    return nsiId;
  }

  public void setNsiId(String nsiId) {
    this.nsiId = nsiId;
  }

  public ServiceExperienceInfo ratio(Integer ratio) {
    this.ratio = ratio;
    return this;
  }

  /**
   * Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  
   * minimum: 1
   * maximum: 100
   * @return ratio
   **/
  @Schema(description = "Unsigned integer indicating Sampling Ratio (see clauses 4.15.1 of 3GPP TS 23.502), expressed in percent.  ")
  
  @Min(1) @Max(100)   public Integer getRatio() {
    return ratio;
  }

  public void setRatio(Integer ratio) {
    this.ratio = ratio;
  }

  public ServiceExperienceInfo ratFreq(RatFreqInformation ratFreq) {
    this.ratFreq = ratFreq;
    return this;
  }

  /**
   * Get ratFreq
   * @return ratFreq
   **/
  @Schema(description = "")
  
    @Valid
    public RatFreqInformation getRatFreq() {
    return ratFreq;
  }

  public void setRatFreq(RatFreqInformation ratFreq) {
    this.ratFreq = ratFreq;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceExperienceInfo serviceExperienceInfo = (ServiceExperienceInfo) o;
    return Objects.equals(this.svcExprc, serviceExperienceInfo.svcExprc) &&
        Objects.equals(this.svcExprcVariance, serviceExperienceInfo.svcExprcVariance) &&
        Objects.equals(this.supis, serviceExperienceInfo.supis) &&
        Objects.equals(this.snssai, serviceExperienceInfo.snssai) &&
        Objects.equals(this.appId, serviceExperienceInfo.appId) &&
        Objects.equals(this.srvExpcType, serviceExperienceInfo.srvExpcType) &&
        Objects.equals(this.ueLocs, serviceExperienceInfo.ueLocs) &&
        Objects.equals(this.upfInfo, serviceExperienceInfo.upfInfo) &&
        Objects.equals(this.dnai, serviceExperienceInfo.dnai) &&
        Objects.equals(this.appServerInst, serviceExperienceInfo.appServerInst) &&
        Objects.equals(this.confidence, serviceExperienceInfo.confidence) &&
        Objects.equals(this.dnn, serviceExperienceInfo.dnn) &&
        Objects.equals(this.networkArea, serviceExperienceInfo.networkArea) &&
        Objects.equals(this.nsiId, serviceExperienceInfo.nsiId) &&
        Objects.equals(this.ratio, serviceExperienceInfo.ratio) &&
        Objects.equals(this.ratFreq, serviceExperienceInfo.ratFreq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(svcExprc, svcExprcVariance, supis, snssai, appId, srvExpcType, ueLocs, upfInfo, dnai, appServerInst, confidence, dnn, networkArea, nsiId, ratio, ratFreq);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceExperienceInfo {\n");
    
    sb.append("    svcExprc: ").append(toIndentedString(svcExprc)).append("\n");
    sb.append("    svcExprcVariance: ").append(toIndentedString(svcExprcVariance)).append("\n");
    sb.append("    supis: ").append(toIndentedString(supis)).append("\n");
    sb.append("    snssai: ").append(toIndentedString(snssai)).append("\n");
    sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
    sb.append("    srvExpcType: ").append(toIndentedString(srvExpcType)).append("\n");
    sb.append("    ueLocs: ").append(toIndentedString(ueLocs)).append("\n");
    sb.append("    upfInfo: ").append(toIndentedString(upfInfo)).append("\n");
    sb.append("    dnai: ").append(toIndentedString(dnai)).append("\n");
    sb.append("    appServerInst: ").append(toIndentedString(appServerInst)).append("\n");
    sb.append("    confidence: ").append(toIndentedString(confidence)).append("\n");
    sb.append("    dnn: ").append(toIndentedString(dnn)).append("\n");
    sb.append("    networkArea: ").append(toIndentedString(networkArea)).append("\n");
    sb.append("    nsiId: ").append(toIndentedString(nsiId)).append("\n");
    sb.append("    ratio: ").append(toIndentedString(ratio)).append("\n");
    sb.append("    ratFreq: ").append(toIndentedString(ratFreq)).append("\n");
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
