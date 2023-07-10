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
 * Represents a subscription to a single event.
 */
@Schema(description = "Represents a subscription to a single event.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class EventSubscription   {
  @JsonProperty("anySlice")
  private Boolean anySlice = null;

  @JsonProperty("appIds")
  @Valid
  private List<String> appIds = null;

  @JsonProperty("dnns")
  @Valid
  private List<String> dnns = null;

  @JsonProperty("dnais")
  @Valid
  private List<String> dnais = null;

  @JsonProperty("event")
  private NwdafEvent event = null;

  @JsonProperty("extraReportReq")
  private EventReportingRequirement extraReportReq = null;

  @JsonProperty("ladnDnns")
  @Valid
  private List<String> ladnDnns = null;

  @JsonProperty("loadLevelThreshold")
  private Integer loadLevelThreshold = null;

  @JsonProperty("notificationMethod")
  private NotificationMethod notificationMethod = null;

  @JsonProperty("matchingDir")
  private MatchingDirection matchingDir = null;

  @JsonProperty("nfLoadLvlThds")
  @Valid
  private List<ThresholdLevel> nfLoadLvlThds = null;

  @JsonProperty("nfInstanceIds")
  @Valid
  private List<UUID> nfInstanceIds = null;

  @JsonProperty("nfSetIds")
  @Valid
  private List<String> nfSetIds = null;

  @JsonProperty("nfTypes")
  @Valid
  private List<NFType> nfTypes = null;

  @JsonProperty("networkArea")
  private NetworkAreaInfo networkArea = null;

  @JsonProperty("visitedAreas")
  @Valid
  private List<NetworkAreaInfo> visitedAreas = null;

  @JsonProperty("maxTopAppUlNbr")
  private Integer maxTopAppUlNbr = null;

  @JsonProperty("maxTopAppDlNbr")
  private Integer maxTopAppDlNbr = null;

  @JsonProperty("nsiIdInfos")
  @Valid
  private List<NsiIdInfo> nsiIdInfos = null;

  @JsonProperty("nsiLevelThrds")
  @Valid
  private List<Integer> nsiLevelThrds = null;

  @JsonProperty("qosRequ")
  private QosRequirement qosRequ = null;

  @JsonProperty("qosFlowRetThds")
  @Valid
  private List<RetainabilityThreshold> qosFlowRetThds = null;

  @JsonProperty("ranUeThrouThds")
  @Valid
  private List<String> ranUeThrouThds = null;

  @JsonProperty("repetitionPeriod")
  private Integer repetitionPeriod = null;

  @JsonProperty("snssaia")
  @Valid
  private List<Snssai> snssaia = null;

  @JsonProperty("tgtUe")
  private TargetUeInformation tgtUe = null;

  @JsonProperty("congThresholds")
  @Valid
  private List<ThresholdLevel> congThresholds = null;

  @JsonProperty("nwPerfRequs")
  @Valid
  private List<NetworkPerfRequirement> nwPerfRequs = null;

  @JsonProperty("bwRequs")
  @Valid
  private List<BwRequirement> bwRequs = null;

  @JsonProperty("excepRequs")
  @Valid
  private List<Exception> excepRequs = null;

  @JsonProperty("exptAnaType")
  private ExpectedAnalyticsType exptAnaType = null;

  @JsonProperty("exptUeBehav")
  private ExpectedUeBehaviourData exptUeBehav = null;

  @JsonProperty("ratFreqs")
  @Valid
  private List<RatFreqInformation> ratFreqs = null;

  @JsonProperty("listOfAnaSubsets")
  @Valid
  private List<AnalyticsSubset> listOfAnaSubsets = null;

  @JsonProperty("disperReqs")
  @Valid
  private List<DispersionRequirement> disperReqs = null;

  @JsonProperty("redTransReqs")
  @Valid
  private List<RedundantTransmissionExpReq> redTransReqs = null;

  @JsonProperty("wlanReqs")
  @Valid
  private List<WlanPerformanceReq> wlanReqs = null;

  @JsonProperty("upfInfo")
  private UpfInformation upfInfo = null;

  @JsonProperty("appServerAddrs")
  @Valid
  private List<AddrFqdn> appServerAddrs = null;

  @JsonProperty("dnPerfReqs")
  @Valid
  private List<DnPerformanceReq> dnPerfReqs = null;

  public EventSubscription anySlice(Boolean anySlice) {
    this.anySlice = anySlice;
    return this;
  }

  /**
   * FALSE represents not applicable for all slices. TRUE represents applicable for all slices. 
   * @return anySlice
   **/
  @Schema(description = "FALSE represents not applicable for all slices. TRUE represents applicable for all slices. ")
  
    public Boolean isAnySlice() {
    return anySlice;
  }

  public void setAnySlice(Boolean anySlice) {
    this.anySlice = anySlice;
  }

  public EventSubscription appIds(List<String> appIds) {
    this.appIds = appIds;
    return this;
  }

  public EventSubscription addAppIdsItem(String appIdsItem) {
    if (this.appIds == null) {
      this.appIds = new ArrayList<String>();
    }
    this.appIds.add(appIdsItem);
    return this;
  }

  /**
   * Identification(s) of application to which the subscription applies.
   * @return appIds
   **/
  @Schema(description = "Identification(s) of application to which the subscription applies.")
  
  @Size(min=1)   public List<String> getAppIds() {
    return appIds;
  }

  public void setAppIds(List<String> appIds) {
    this.appIds = appIds;
  }

  public EventSubscription dnns(List<String> dnns) {
    this.dnns = dnns;
    return this;
  }

  public EventSubscription addDnnsItem(String dnnsItem) {
    if (this.dnns == null) {
      this.dnns = new ArrayList<String>();
    }
    this.dnns.add(dnnsItem);
    return this;
  }

  /**
   * Identification(s) of DNN to which the subscription applies.
   * @return dnns
   **/
  @Schema(description = "Identification(s) of DNN to which the subscription applies.")
  
  @Size(min=1)   public List<String> getDnns() {
    return dnns;
  }

  public void setDnns(List<String> dnns) {
    this.dnns = dnns;
  }

  public EventSubscription dnais(List<String> dnais) {
    this.dnais = dnais;
    return this;
  }

  public EventSubscription addDnaisItem(String dnaisItem) {
    if (this.dnais == null) {
      this.dnais = new ArrayList<String>();
    }
    this.dnais.add(dnaisItem);
    return this;
  }

  /**
   * Get dnais
   * @return dnais
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getDnais() {
    return dnais;
  }

  public void setDnais(List<String> dnais) {
    this.dnais = dnais;
  }

  public EventSubscription event(NwdafEvent event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public NwdafEvent getEvent() {
    return event;
  }

  public void setEvent(NwdafEvent event) {
    this.event = event;
  }

  public EventSubscription extraReportReq(EventReportingRequirement extraReportReq) {
    this.extraReportReq = extraReportReq;
    return this;
  }

  /**
   * Get extraReportReq
   * @return extraReportReq
   **/
  @Schema(description = "")
  
    @Valid
    public EventReportingRequirement getExtraReportReq() {
    return extraReportReq;
  }

  public void setExtraReportReq(EventReportingRequirement extraReportReq) {
    this.extraReportReq = extraReportReq;
  }

  public EventSubscription ladnDnns(List<String> ladnDnns) {
    this.ladnDnns = ladnDnns;
    return this;
  }

  public EventSubscription addLadnDnnsItem(String ladnDnnsItem) {
    if (this.ladnDnns == null) {
      this.ladnDnns = new ArrayList<String>();
    }
    this.ladnDnns.add(ladnDnnsItem);
    return this;
  }

  /**
   * Identification(s) of LADN DNN to indicate the LADN service area as the AOI.
   * @return ladnDnns
   **/
  @Schema(description = "Identification(s) of LADN DNN to indicate the LADN service area as the AOI.")
  
  @Size(min=1)   public List<String> getLadnDnns() {
    return ladnDnns;
  }

  public void setLadnDnns(List<String> ladnDnns) {
    this.ladnDnns = ladnDnns;
  }

  public EventSubscription loadLevelThreshold(Integer loadLevelThreshold) {
    this.loadLevelThreshold = loadLevelThreshold;
    return this;
  }

  /**
   * Indicates that the NWDAF shall report the corresponding network slice load level to the NF  service consumer where the load level of the network slice identified by snssais is  reached. 
   * @return loadLevelThreshold
   **/
  @Schema(description = "Indicates that the NWDAF shall report the corresponding network slice load level to the NF  service consumer where the load level of the network slice identified by snssais is  reached. ")
  
    public Integer getLoadLevelThreshold() {
    return loadLevelThreshold;
  }

  public void setLoadLevelThreshold(Integer loadLevelThreshold) {
    this.loadLevelThreshold = loadLevelThreshold;
  }

  public EventSubscription notificationMethod(NotificationMethod notificationMethod) {
    this.notificationMethod = notificationMethod;
    return this;
  }

  /**
   * Get notificationMethod
   * @return notificationMethod
   **/
  @Schema(description = "")
  
    @Valid
    public NotificationMethod getNotificationMethod() {
    return notificationMethod;
  }

  public void setNotificationMethod(NotificationMethod notificationMethod) {
    this.notificationMethod = notificationMethod;
  }

  public EventSubscription matchingDir(MatchingDirection matchingDir) {
    this.matchingDir = matchingDir;
    return this;
  }

  /**
   * Get matchingDir
   * @return matchingDir
   **/
  @Schema(description = "")
  
    @Valid
    public MatchingDirection getMatchingDir() {
    return matchingDir;
  }

  public void setMatchingDir(MatchingDirection matchingDir) {
    this.matchingDir = matchingDir;
  }

  public EventSubscription nfLoadLvlThds(List<ThresholdLevel> nfLoadLvlThds) {
    this.nfLoadLvlThds = nfLoadLvlThds;
    return this;
  }

  public EventSubscription addNfLoadLvlThdsItem(ThresholdLevel nfLoadLvlThdsItem) {
    if (this.nfLoadLvlThds == null) {
      this.nfLoadLvlThds = new ArrayList<ThresholdLevel>();
    }
    this.nfLoadLvlThds.add(nfLoadLvlThdsItem);
    return this;
  }

  /**
   * Shall be supplied in order to start reporting when an average load level is reached. 
   * @return nfLoadLvlThds
   **/
  @Schema(description = "Shall be supplied in order to start reporting when an average load level is reached. ")
      @Valid
  @Size(min=1)   public List<ThresholdLevel> getNfLoadLvlThds() {
    return nfLoadLvlThds;
  }

  public void setNfLoadLvlThds(List<ThresholdLevel> nfLoadLvlThds) {
    this.nfLoadLvlThds = nfLoadLvlThds;
  }

  public EventSubscription nfInstanceIds(List<UUID> nfInstanceIds) {
    this.nfInstanceIds = nfInstanceIds;
    return this;
  }

  public EventSubscription addNfInstanceIdsItem(UUID nfInstanceIdsItem) {
    if (this.nfInstanceIds == null) {
      this.nfInstanceIds = new ArrayList<UUID>();
    }
    this.nfInstanceIds.add(nfInstanceIdsItem);
    return this;
  }

  /**
   * Get nfInstanceIds
   * @return nfInstanceIds
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<UUID> getNfInstanceIds() {
    return nfInstanceIds;
  }

  public void setNfInstanceIds(List<UUID> nfInstanceIds) {
    this.nfInstanceIds = nfInstanceIds;
  }

  public EventSubscription nfSetIds(List<String> nfSetIds) {
    this.nfSetIds = nfSetIds;
    return this;
  }

  public EventSubscription addNfSetIdsItem(String nfSetIdsItem) {
    if (this.nfSetIds == null) {
      this.nfSetIds = new ArrayList<String>();
    }
    this.nfSetIds.add(nfSetIdsItem);
    return this;
  }

  /**
   * Get nfSetIds
   * @return nfSetIds
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getNfSetIds() {
    return nfSetIds;
  }

  public void setNfSetIds(List<String> nfSetIds) {
    this.nfSetIds = nfSetIds;
  }

  public EventSubscription nfTypes(List<NFType> nfTypes) {
    this.nfTypes = nfTypes;
    return this;
  }

  public EventSubscription addNfTypesItem(NFType nfTypesItem) {
    if (this.nfTypes == null) {
      this.nfTypes = new ArrayList<NFType>();
    }
    this.nfTypes.add(nfTypesItem);
    return this;
  }

  /**
   * Get nfTypes
   * @return nfTypes
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<NFType> getNfTypes() {
    return nfTypes;
  }

  public void setNfTypes(List<NFType> nfTypes) {
    this.nfTypes = nfTypes;
  }

  public EventSubscription networkArea(NetworkAreaInfo networkArea) {
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

  public EventSubscription visitedAreas(List<NetworkAreaInfo> visitedAreas) {
    this.visitedAreas = visitedAreas;
    return this;
  }

  public EventSubscription addVisitedAreasItem(NetworkAreaInfo visitedAreasItem) {
    if (this.visitedAreas == null) {
      this.visitedAreas = new ArrayList<NetworkAreaInfo>();
    }
    this.visitedAreas.add(visitedAreasItem);
    return this;
  }

  /**
   * Get visitedAreas
   * @return visitedAreas
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<NetworkAreaInfo> getVisitedAreas() {
    return visitedAreas;
  }

  public void setVisitedAreas(List<NetworkAreaInfo> visitedAreas) {
    this.visitedAreas = visitedAreas;
  }

  public EventSubscription maxTopAppUlNbr(Integer maxTopAppUlNbr) {
    this.maxTopAppUlNbr = maxTopAppUlNbr;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return maxTopAppUlNbr
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getMaxTopAppUlNbr() {
    return maxTopAppUlNbr;
  }

  public void setMaxTopAppUlNbr(Integer maxTopAppUlNbr) {
    this.maxTopAppUlNbr = maxTopAppUlNbr;
  }

  public EventSubscription maxTopAppDlNbr(Integer maxTopAppDlNbr) {
    this.maxTopAppDlNbr = maxTopAppDlNbr;
    return this;
  }

  /**
   * Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.
   * minimum: 0
   * @return maxTopAppDlNbr
   **/
  @Schema(description = "Unsigned Integer, i.e. only value 0 and integers above 0 are permissible.")
  
  @Min(0)  public Integer getMaxTopAppDlNbr() {
    return maxTopAppDlNbr;
  }

  public void setMaxTopAppDlNbr(Integer maxTopAppDlNbr) {
    this.maxTopAppDlNbr = maxTopAppDlNbr;
  }

  public EventSubscription nsiIdInfos(List<NsiIdInfo> nsiIdInfos) {
    this.nsiIdInfos = nsiIdInfos;
    return this;
  }

  public EventSubscription addNsiIdInfosItem(NsiIdInfo nsiIdInfosItem) {
    if (this.nsiIdInfos == null) {
      this.nsiIdInfos = new ArrayList<NsiIdInfo>();
    }
    this.nsiIdInfos.add(nsiIdInfosItem);
    return this;
  }

  /**
   * Get nsiIdInfos
   * @return nsiIdInfos
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<NsiIdInfo> getNsiIdInfos() {
    return nsiIdInfos;
  }

  public void setNsiIdInfos(List<NsiIdInfo> nsiIdInfos) {
    this.nsiIdInfos = nsiIdInfos;
  }

  public EventSubscription nsiLevelThrds(List<Integer> nsiLevelThrds) {
    this.nsiLevelThrds = nsiLevelThrds;
    return this;
  }

  public EventSubscription addNsiLevelThrdsItem(Integer nsiLevelThrdsItem) {
    if (this.nsiLevelThrds == null) {
      this.nsiLevelThrds = new ArrayList<Integer>();
    }
    this.nsiLevelThrds.add(nsiLevelThrdsItem);
    return this;
  }

  /**
   * Get nsiLevelThrds
   * @return nsiLevelThrds
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<Integer> getNsiLevelThrds() {
    return nsiLevelThrds;
  }

  public void setNsiLevelThrds(List<Integer> nsiLevelThrds) {
    this.nsiLevelThrds = nsiLevelThrds;
  }

  public EventSubscription qosRequ(QosRequirement qosRequ) {
    this.qosRequ = qosRequ;
    return this;
  }

  /**
   * Get qosRequ
   * @return qosRequ
   **/
  @Schema(description = "")
  
    @Valid
    public QosRequirement getQosRequ() {
    return qosRequ;
  }

  public void setQosRequ(QosRequirement qosRequ) {
    this.qosRequ = qosRequ;
  }

  public EventSubscription qosFlowRetThds(List<RetainabilityThreshold> qosFlowRetThds) {
    this.qosFlowRetThds = qosFlowRetThds;
    return this;
  }

  public EventSubscription addQosFlowRetThdsItem(RetainabilityThreshold qosFlowRetThdsItem) {
    if (this.qosFlowRetThds == null) {
      this.qosFlowRetThds = new ArrayList<RetainabilityThreshold>();
    }
    this.qosFlowRetThds.add(qosFlowRetThdsItem);
    return this;
  }

  /**
   * Get qosFlowRetThds
   * @return qosFlowRetThds
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<RetainabilityThreshold> getQosFlowRetThds() {
    return qosFlowRetThds;
  }

  public void setQosFlowRetThds(List<RetainabilityThreshold> qosFlowRetThds) {
    this.qosFlowRetThds = qosFlowRetThds;
  }

  public EventSubscription ranUeThrouThds(List<String> ranUeThrouThds) {
    this.ranUeThrouThds = ranUeThrouThds;
    return this;
  }

  public EventSubscription addRanUeThrouThdsItem(String ranUeThrouThdsItem) {
    if (this.ranUeThrouThds == null) {
      this.ranUeThrouThds = new ArrayList<String>();
    }
    this.ranUeThrouThds.add(ranUeThrouThdsItem);
    return this;
  }

  /**
   * Get ranUeThrouThds
   * @return ranUeThrouThds
   **/
  @Schema(description = "")
  
  @Size(min=1)   public List<String> getRanUeThrouThds() {
    return ranUeThrouThds;
  }

  public void setRanUeThrouThds(List<String> ranUeThrouThds) {
    this.ranUeThrouThds = ranUeThrouThds;
  }

  public EventSubscription repetitionPeriod(Integer repetitionPeriod) {
    this.repetitionPeriod = repetitionPeriod;
    return this;
  }

  /**
   * indicating a time in seconds.
   * @return repetitionPeriod
   **/
  @Schema(description = "indicating a time in seconds.")
  
    public Integer getRepetitionPeriod() {
    return repetitionPeriod;
  }

  public void setRepetitionPeriod(Integer repetitionPeriod) {
    this.repetitionPeriod = repetitionPeriod;
  }

  public EventSubscription snssaia(List<Snssai> snssaia) {
    this.snssaia = snssaia;
    return this;
  }

  public EventSubscription addSnssaiaItem(Snssai snssaiaItem) {
    if (this.snssaia == null) {
      this.snssaia = new ArrayList<Snssai>();
    }
    this.snssaia.add(snssaiaItem);
    return this;
  }

  /**
   * Identification(s) of network slice to which the subscription applies. It corresponds to  snssais in the data model definition of 3GPP TS 29.520.  
   * @return snssaia
   **/
  @Schema(description = "Identification(s) of network slice to which the subscription applies. It corresponds to  snssais in the data model definition of 3GPP TS 29.520.  ")
      @Valid
  @Size(min=1)   public List<Snssai> getSnssaia() {
    return snssaia;
  }

  public void setSnssaia(List<Snssai> snssaia) {
    this.snssaia = snssaia;
  }

  public EventSubscription tgtUe(TargetUeInformation tgtUe) {
    this.tgtUe = tgtUe;
    return this;
  }

  /**
   * Get tgtUe
   * @return tgtUe
   **/
  @Schema(description = "")
  
    @Valid
    public TargetUeInformation getTgtUe() {
    return tgtUe;
  }

  public void setTgtUe(TargetUeInformation tgtUe) {
    this.tgtUe = tgtUe;
  }

  public EventSubscription congThresholds(List<ThresholdLevel> congThresholds) {
    this.congThresholds = congThresholds;
    return this;
  }

  public EventSubscription addCongThresholdsItem(ThresholdLevel congThresholdsItem) {
    if (this.congThresholds == null) {
      this.congThresholds = new ArrayList<ThresholdLevel>();
    }
    this.congThresholds.add(congThresholdsItem);
    return this;
  }

  /**
   * Get congThresholds
   * @return congThresholds
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<ThresholdLevel> getCongThresholds() {
    return congThresholds;
  }

  public void setCongThresholds(List<ThresholdLevel> congThresholds) {
    this.congThresholds = congThresholds;
  }

  public EventSubscription nwPerfRequs(List<NetworkPerfRequirement> nwPerfRequs) {
    this.nwPerfRequs = nwPerfRequs;
    return this;
  }

  public EventSubscription addNwPerfRequsItem(NetworkPerfRequirement nwPerfRequsItem) {
    if (this.nwPerfRequs == null) {
      this.nwPerfRequs = new ArrayList<NetworkPerfRequirement>();
    }
    this.nwPerfRequs.add(nwPerfRequsItem);
    return this;
  }

  /**
   * Get nwPerfRequs
   * @return nwPerfRequs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<NetworkPerfRequirement> getNwPerfRequs() {
    return nwPerfRequs;
  }

  public void setNwPerfRequs(List<NetworkPerfRequirement> nwPerfRequs) {
    this.nwPerfRequs = nwPerfRequs;
  }

  public EventSubscription bwRequs(List<BwRequirement> bwRequs) {
    this.bwRequs = bwRequs;
    return this;
  }

  public EventSubscription addBwRequsItem(BwRequirement bwRequsItem) {
    if (this.bwRequs == null) {
      this.bwRequs = new ArrayList<BwRequirement>();
    }
    this.bwRequs.add(bwRequsItem);
    return this;
  }

  /**
   * Get bwRequs
   * @return bwRequs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<BwRequirement> getBwRequs() {
    return bwRequs;
  }

  public void setBwRequs(List<BwRequirement> bwRequs) {
    this.bwRequs = bwRequs;
  }

  public EventSubscription excepRequs(List<Exception> excepRequs) {
    this.excepRequs = excepRequs;
    return this;
  }

  public EventSubscription addExcepRequsItem(Exception excepRequsItem) {
    if (this.excepRequs == null) {
      this.excepRequs = new ArrayList<Exception>();
    }
    this.excepRequs.add(excepRequsItem);
    return this;
  }

  /**
   * Get excepRequs
   * @return excepRequs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<Exception> getExcepRequs() {
    return excepRequs;
  }

  public void setExcepRequs(List<Exception> excepRequs) {
    this.excepRequs = excepRequs;
  }

  public EventSubscription exptAnaType(ExpectedAnalyticsType exptAnaType) {
    this.exptAnaType = exptAnaType;
    return this;
  }

  /**
   * Get exptAnaType
   * @return exptAnaType
   **/
  @Schema(description = "")
  
    @Valid
    public ExpectedAnalyticsType getExptAnaType() {
    return exptAnaType;
  }

  public void setExptAnaType(ExpectedAnalyticsType exptAnaType) {
    this.exptAnaType = exptAnaType;
  }

  public EventSubscription exptUeBehav(ExpectedUeBehaviourData exptUeBehav) {
    this.exptUeBehav = exptUeBehav;
    return this;
  }

  /**
   * Get exptUeBehav
   * @return exptUeBehav
   **/
  @Schema(description = "")
  
    @Valid
    public ExpectedUeBehaviourData getExptUeBehav() {
    return exptUeBehav;
  }

  public void setExptUeBehav(ExpectedUeBehaviourData exptUeBehav) {
    this.exptUeBehav = exptUeBehav;
  }

  public EventSubscription ratFreqs(List<RatFreqInformation> ratFreqs) {
    this.ratFreqs = ratFreqs;
    return this;
  }

  public EventSubscription addRatFreqsItem(RatFreqInformation ratFreqsItem) {
    if (this.ratFreqs == null) {
      this.ratFreqs = new ArrayList<RatFreqInformation>();
    }
    this.ratFreqs.add(ratFreqsItem);
    return this;
  }

  /**
   * Get ratFreqs
   * @return ratFreqs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<RatFreqInformation> getRatFreqs() {
    return ratFreqs;
  }

  public void setRatFreqs(List<RatFreqInformation> ratFreqs) {
    this.ratFreqs = ratFreqs;
  }

  public EventSubscription listOfAnaSubsets(List<AnalyticsSubset> listOfAnaSubsets) {
    this.listOfAnaSubsets = listOfAnaSubsets;
    return this;
  }

  public EventSubscription addListOfAnaSubsetsItem(AnalyticsSubset listOfAnaSubsetsItem) {
    if (this.listOfAnaSubsets == null) {
      this.listOfAnaSubsets = new ArrayList<AnalyticsSubset>();
    }
    this.listOfAnaSubsets.add(listOfAnaSubsetsItem);
    return this;
  }

  /**
   * Get listOfAnaSubsets
   * @return listOfAnaSubsets
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<AnalyticsSubset> getListOfAnaSubsets() {
    return listOfAnaSubsets;
  }

  public void setListOfAnaSubsets(List<AnalyticsSubset> listOfAnaSubsets) {
    this.listOfAnaSubsets = listOfAnaSubsets;
  }

  public EventSubscription disperReqs(List<DispersionRequirement> disperReqs) {
    this.disperReqs = disperReqs;
    return this;
  }

  public EventSubscription addDisperReqsItem(DispersionRequirement disperReqsItem) {
    if (this.disperReqs == null) {
      this.disperReqs = new ArrayList<DispersionRequirement>();
    }
    this.disperReqs.add(disperReqsItem);
    return this;
  }

  /**
   * Get disperReqs
   * @return disperReqs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<DispersionRequirement> getDisperReqs() {
    return disperReqs;
  }

  public void setDisperReqs(List<DispersionRequirement> disperReqs) {
    this.disperReqs = disperReqs;
  }

  public EventSubscription redTransReqs(List<RedundantTransmissionExpReq> redTransReqs) {
    this.redTransReqs = redTransReqs;
    return this;
  }

  public EventSubscription addRedTransReqsItem(RedundantTransmissionExpReq redTransReqsItem) {
    if (this.redTransReqs == null) {
      this.redTransReqs = new ArrayList<RedundantTransmissionExpReq>();
    }
    this.redTransReqs.add(redTransReqsItem);
    return this;
  }

  /**
   * Get redTransReqs
   * @return redTransReqs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<RedundantTransmissionExpReq> getRedTransReqs() {
    return redTransReqs;
  }

  public void setRedTransReqs(List<RedundantTransmissionExpReq> redTransReqs) {
    this.redTransReqs = redTransReqs;
  }

  public EventSubscription wlanReqs(List<WlanPerformanceReq> wlanReqs) {
    this.wlanReqs = wlanReqs;
    return this;
  }

  public EventSubscription addWlanReqsItem(WlanPerformanceReq wlanReqsItem) {
    if (this.wlanReqs == null) {
      this.wlanReqs = new ArrayList<WlanPerformanceReq>();
    }
    this.wlanReqs.add(wlanReqsItem);
    return this;
  }

  /**
   * Get wlanReqs
   * @return wlanReqs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<WlanPerformanceReq> getWlanReqs() {
    return wlanReqs;
  }

  public void setWlanReqs(List<WlanPerformanceReq> wlanReqs) {
    this.wlanReqs = wlanReqs;
  }

  public EventSubscription upfInfo(UpfInformation upfInfo) {
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

  public EventSubscription appServerAddrs(List<AddrFqdn> appServerAddrs) {
    this.appServerAddrs = appServerAddrs;
    return this;
  }

  public EventSubscription addAppServerAddrsItem(AddrFqdn appServerAddrsItem) {
    if (this.appServerAddrs == null) {
      this.appServerAddrs = new ArrayList<AddrFqdn>();
    }
    this.appServerAddrs.add(appServerAddrsItem);
    return this;
  }

  /**
   * Get appServerAddrs
   * @return appServerAddrs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<AddrFqdn> getAppServerAddrs() {
    return appServerAddrs;
  }

  public void setAppServerAddrs(List<AddrFqdn> appServerAddrs) {
    this.appServerAddrs = appServerAddrs;
  }

  public EventSubscription dnPerfReqs(List<DnPerformanceReq> dnPerfReqs) {
    this.dnPerfReqs = dnPerfReqs;
    return this;
  }

  public EventSubscription addDnPerfReqsItem(DnPerformanceReq dnPerfReqsItem) {
    if (this.dnPerfReqs == null) {
      this.dnPerfReqs = new ArrayList<DnPerformanceReq>();
    }
    this.dnPerfReqs.add(dnPerfReqsItem);
    return this;
  }

  /**
   * Get dnPerfReqs
   * @return dnPerfReqs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<DnPerformanceReq> getDnPerfReqs() {
    return dnPerfReqs;
  }

  public void setDnPerfReqs(List<DnPerformanceReq> dnPerfReqs) {
    this.dnPerfReqs = dnPerfReqs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventSubscription eventSubscription = (EventSubscription) o;
    return Objects.equals(this.anySlice, eventSubscription.anySlice) &&
        Objects.equals(this.appIds, eventSubscription.appIds) &&
        Objects.equals(this.dnns, eventSubscription.dnns) &&
        Objects.equals(this.dnais, eventSubscription.dnais) &&
        Objects.equals(this.event, eventSubscription.event) &&
        Objects.equals(this.extraReportReq, eventSubscription.extraReportReq) &&
        Objects.equals(this.ladnDnns, eventSubscription.ladnDnns) &&
        Objects.equals(this.loadLevelThreshold, eventSubscription.loadLevelThreshold) &&
        Objects.equals(this.notificationMethod, eventSubscription.notificationMethod) &&
        Objects.equals(this.matchingDir, eventSubscription.matchingDir) &&
        Objects.equals(this.nfLoadLvlThds, eventSubscription.nfLoadLvlThds) &&
        Objects.equals(this.nfInstanceIds, eventSubscription.nfInstanceIds) &&
        Objects.equals(this.nfSetIds, eventSubscription.nfSetIds) &&
        Objects.equals(this.nfTypes, eventSubscription.nfTypes) &&
        Objects.equals(this.networkArea, eventSubscription.networkArea) &&
        Objects.equals(this.visitedAreas, eventSubscription.visitedAreas) &&
        Objects.equals(this.maxTopAppUlNbr, eventSubscription.maxTopAppUlNbr) &&
        Objects.equals(this.maxTopAppDlNbr, eventSubscription.maxTopAppDlNbr) &&
        Objects.equals(this.nsiIdInfos, eventSubscription.nsiIdInfos) &&
        Objects.equals(this.nsiLevelThrds, eventSubscription.nsiLevelThrds) &&
        Objects.equals(this.qosRequ, eventSubscription.qosRequ) &&
        Objects.equals(this.qosFlowRetThds, eventSubscription.qosFlowRetThds) &&
        Objects.equals(this.ranUeThrouThds, eventSubscription.ranUeThrouThds) &&
        Objects.equals(this.repetitionPeriod, eventSubscription.repetitionPeriod) &&
        Objects.equals(this.snssaia, eventSubscription.snssaia) &&
        Objects.equals(this.tgtUe, eventSubscription.tgtUe) &&
        Objects.equals(this.congThresholds, eventSubscription.congThresholds) &&
        Objects.equals(this.nwPerfRequs, eventSubscription.nwPerfRequs) &&
        Objects.equals(this.bwRequs, eventSubscription.bwRequs) &&
        Objects.equals(this.excepRequs, eventSubscription.excepRequs) &&
        Objects.equals(this.exptAnaType, eventSubscription.exptAnaType) &&
        Objects.equals(this.exptUeBehav, eventSubscription.exptUeBehav) &&
        Objects.equals(this.ratFreqs, eventSubscription.ratFreqs) &&
        Objects.equals(this.listOfAnaSubsets, eventSubscription.listOfAnaSubsets) &&
        Objects.equals(this.disperReqs, eventSubscription.disperReqs) &&
        Objects.equals(this.redTransReqs, eventSubscription.redTransReqs) &&
        Objects.equals(this.wlanReqs, eventSubscription.wlanReqs) &&
        Objects.equals(this.upfInfo, eventSubscription.upfInfo) &&
        Objects.equals(this.appServerAddrs, eventSubscription.appServerAddrs) &&
        Objects.equals(this.dnPerfReqs, eventSubscription.dnPerfReqs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anySlice, appIds, dnns, dnais, event, extraReportReq, ladnDnns, loadLevelThreshold, notificationMethod, matchingDir, nfLoadLvlThds, nfInstanceIds, nfSetIds, nfTypes, networkArea, visitedAreas, maxTopAppUlNbr, maxTopAppDlNbr, nsiIdInfos, nsiLevelThrds, qosRequ, qosFlowRetThds, ranUeThrouThds, repetitionPeriod, snssaia, tgtUe, congThresholds, nwPerfRequs, bwRequs, excepRequs, exptAnaType, exptUeBehav, ratFreqs, listOfAnaSubsets, disperReqs, redTransReqs, wlanReqs, upfInfo, appServerAddrs, dnPerfReqs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventSubscription {\n");
    
    sb.append("    anySlice: ").append(toIndentedString(anySlice)).append("\n");
    sb.append("    appIds: ").append(toIndentedString(appIds)).append("\n");
    sb.append("    dnns: ").append(toIndentedString(dnns)).append("\n");
    sb.append("    dnais: ").append(toIndentedString(dnais)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    extraReportReq: ").append(toIndentedString(extraReportReq)).append("\n");
    sb.append("    ladnDnns: ").append(toIndentedString(ladnDnns)).append("\n");
    sb.append("    loadLevelThreshold: ").append(toIndentedString(loadLevelThreshold)).append("\n");
    sb.append("    notificationMethod: ").append(toIndentedString(notificationMethod)).append("\n");
    sb.append("    matchingDir: ").append(toIndentedString(matchingDir)).append("\n");
    sb.append("    nfLoadLvlThds: ").append(toIndentedString(nfLoadLvlThds)).append("\n");
    sb.append("    nfInstanceIds: ").append(toIndentedString(nfInstanceIds)).append("\n");
    sb.append("    nfSetIds: ").append(toIndentedString(nfSetIds)).append("\n");
    sb.append("    nfTypes: ").append(toIndentedString(nfTypes)).append("\n");
    sb.append("    networkArea: ").append(toIndentedString(networkArea)).append("\n");
    sb.append("    visitedAreas: ").append(toIndentedString(visitedAreas)).append("\n");
    sb.append("    maxTopAppUlNbr: ").append(toIndentedString(maxTopAppUlNbr)).append("\n");
    sb.append("    maxTopAppDlNbr: ").append(toIndentedString(maxTopAppDlNbr)).append("\n");
    sb.append("    nsiIdInfos: ").append(toIndentedString(nsiIdInfos)).append("\n");
    sb.append("    nsiLevelThrds: ").append(toIndentedString(nsiLevelThrds)).append("\n");
    sb.append("    qosRequ: ").append(toIndentedString(qosRequ)).append("\n");
    sb.append("    qosFlowRetThds: ").append(toIndentedString(qosFlowRetThds)).append("\n");
    sb.append("    ranUeThrouThds: ").append(toIndentedString(ranUeThrouThds)).append("\n");
    sb.append("    repetitionPeriod: ").append(toIndentedString(repetitionPeriod)).append("\n");
    sb.append("    snssaia: ").append(toIndentedString(snssaia)).append("\n");
    sb.append("    tgtUe: ").append(toIndentedString(tgtUe)).append("\n");
    sb.append("    congThresholds: ").append(toIndentedString(congThresholds)).append("\n");
    sb.append("    nwPerfRequs: ").append(toIndentedString(nwPerfRequs)).append("\n");
    sb.append("    bwRequs: ").append(toIndentedString(bwRequs)).append("\n");
    sb.append("    excepRequs: ").append(toIndentedString(excepRequs)).append("\n");
    sb.append("    exptAnaType: ").append(toIndentedString(exptAnaType)).append("\n");
    sb.append("    exptUeBehav: ").append(toIndentedString(exptUeBehav)).append("\n");
    sb.append("    ratFreqs: ").append(toIndentedString(ratFreqs)).append("\n");
    sb.append("    listOfAnaSubsets: ").append(toIndentedString(listOfAnaSubsets)).append("\n");
    sb.append("    disperReqs: ").append(toIndentedString(disperReqs)).append("\n");
    sb.append("    redTransReqs: ").append(toIndentedString(redTransReqs)).append("\n");
    sb.append("    wlanReqs: ").append(toIndentedString(wlanReqs)).append("\n");
    sb.append("    upfInfo: ").append(toIndentedString(upfInfo)).append("\n");
    sb.append("    appServerAddrs: ").append(toIndentedString(appServerAddrs)).append("\n");
    sb.append("    dnPerfReqs: ").append(toIndentedString(dnPerfReqs)).append("\n");
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
