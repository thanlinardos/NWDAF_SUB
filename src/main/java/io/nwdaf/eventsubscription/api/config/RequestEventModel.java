package io.nwdaf.eventsubscription.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.util.Pair;

public class RequestEventModel {
	private String event;
	private String notificationMethod;
	private List<String>optionals = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null));
	private List<String> anaMeta = new ArrayList<String>();
	private List<String> anaMetaInd = new ArrayList<String>();
	private List<String> dataStatProps = new ArrayList<String>();
	private List<String> accPerSubset = new ArrayList<String>();
	private List<String>args = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null,null));
	private List<List<String>>nfLoadLvlThds = new ArrayList<List<String>>();
	private List<String> supis = new ArrayList<String>();
	private List<String> intGroupIds = new ArrayList<String>();
	private List<String> nfInstanceIds = new ArrayList<String>();
	private List<String> nfSetIds = new ArrayList<String>();
	private List<String> appIds = new ArrayList<String>();
	private List<String> dnns = new ArrayList<String>();
	private List<String> dnais = new ArrayList<String>();
	private List<String> ladnDnns = new ArrayList<String>();
	private List<String> nfTypes = new ArrayList<String>();
	private List<List<List<List<String>>>> visitedAreas = new ArrayList<List<List<List<String>>>>();
	private List<List<List<String>>> nsiIdInfos = new ArrayList<List<List<String>>>();
	private List<Integer> nsiLevelThrds = new ArrayList<Integer>();
	private List<List<String>> qosFlowRetThds = new ArrayList<List<String>>();
	private List<String> ranUeThrouThds = new ArrayList<String>();
	private List<List<String>> snssaia = new ArrayList<List<String>>();
	private List<List<String>> congThresholds = new ArrayList<List<String>>();
	private List<List<String>> nwPerfRequs = new ArrayList<List<String>>();
	private List<List<String>> bwRequs = new ArrayList<List<String>>();
	private List<List<String>> excepRequs = new ArrayList<List<String>>();
	private List<List<List<String>>> ratFreqs = new ArrayList<List<List<String>>>();
	private List<String> listOfAnaSubsets = new ArrayList<String>();
	private List<List<List<List<String>>>> disperReqs = new ArrayList<List<List<List<String>>>>();
	private List<List<String>> redTransReqs = new ArrayList<List<String>>();
	private List<List<List<String>>> wlanReqs = new ArrayList<List<List<String>>>();
	private List<List<String>> appServerAddrs = new ArrayList<List<String>>();
	private List<List<List<List<String>>>> dnPerfReqs = new ArrayList<List<List<List<String>>>>();
	private List<List<List<String>>> networkArea = new ArrayList<List<List<String>>>();
	private List<String> qosRequ = new ArrayList<String>();
	private List<List<List<List<List<List<String>>>>>> exptUeBehav = new ArrayList<List<List<List<List<List<String>>>>>>();
	private List<String> upfInfo = new ArrayList<String>();
	//show booleans
	private Boolean showExtraRepReq=false;
	private Boolean showTgtUe=false;
	private Boolean showLoadLevelThreshold=false;
	private Boolean showSnssais=false;
	private Boolean showAnySlice=false;
	private Boolean showNsiIdInfos=false;
	private Boolean showNsiLevelThrds=false;
	private Boolean showListOfAnaSubsets=false;
	private Boolean showNetworkArea=false;
	private Boolean showSupis=false;
	private Boolean showNfLoadLvlThds=false;
	private Boolean showNfInstanceIds=false;
	private Boolean showNfSetIds=false;
	private Boolean showMatchingDir=false;
	private Boolean showIntGroupIds=false;
	private Boolean showBwRequs=false;
	private Boolean showRatFreqs=false;
	private Boolean showUpfInfo=false;
	private Boolean showAppServerAddrs=false;
	private Boolean showLadnDnns=false;
	private Boolean showVisitedAreas=false;
	private Boolean showQosFlowRetThds=false;
	private Boolean showRanUeThrouThds=false;
	private Boolean showExptAnaType=false;
	private Boolean showExptUeBehav=false;
	private Boolean showGpsis=false;
	private Boolean showCongThresholds=false;
	private Boolean showMaxTopAppUlNbr=false;
	private Boolean showMaxTopAppDlNbr=false;
	private Boolean showDisperReqs=false;
	private Boolean showRedTransReqs=false;
	private Boolean showWlanReqs=false;
	private Boolean showDnais=false;
	private Boolean showDnPerfReqs=false;
	//optionals
	
	//args
	private Integer repetitionPeriod;
	
	public void setAllLists() {
		if(repetitionPeriod!=null) {
			args.set(6, String.valueOf(repetitionPeriod));
		}
	}
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getNotificationMethod() {
		return notificationMethod;
	}
	public void setNotificationMethod(String notifMethod) {
		this.notificationMethod = notifMethod;
	}
	public List<String> getOptionals() {
		return optionals;
	}
	public void setOptionals(List<String> optionals) {
		this.optionals = optionals;
	}
	public List<String> getAnaMeta() {
		return anaMeta;
	}
	public void setAnaMeta(List<String> anaMeta) {
		this.anaMeta = anaMeta;
	}
	public List<String> getAnaMetaInd() {
		return anaMetaInd;
	}
	public void setAnaMetaInd(List<String> anaMetaInd) {
		this.anaMetaInd = anaMetaInd;
	}
	public List<String> getDataStatProps() {
		return dataStatProps;
	}
	public void setDataStatProps(List<String> dataStatProps) {
		this.dataStatProps = dataStatProps;
	}
	public List<String> getAccPerSubset() {
		return accPerSubset;
	}
	public void setAccPerSubset(List<String> accPerSubset) {
		this.accPerSubset = accPerSubset;
	}
	public List<String> getArgs() {
		return args;
	}
	public void setArgs(List<String> args) {
		this.args = args;
	}
	public List<List<String>> getNfLoadLvlThds() {
		return nfLoadLvlThds;
	}
	public void setNfLoadLvlThds(List<List<String>> nfLoadLvlThds) {
		this.nfLoadLvlThds = nfLoadLvlThds;
	}
	public List<String> getSupis() {
		return supis;
	}
	public void setSupis(List<String> supis) {
		this.supis = supis;
	}
	public List<String> getIntGroupIds() {
		return intGroupIds;
	}
	public void setIntGroupIds(List<String> intGroupIds) {
		this.intGroupIds = intGroupIds;
	}
	public List<String> getNfInstanceIds() {
		return nfInstanceIds;
	}
	public void setNfInstanceIds(List<String> nfInstanceIds) {
		this.nfInstanceIds = nfInstanceIds;
	}
	public List<String> getNfSetIds() {
		return nfSetIds;
	}
	public void setNfSetIds(List<String> nfSetIds) {
		this.nfSetIds = nfSetIds;
	}
	public List<String> getAppIds() {
		return appIds;
	}
	public void setAppIds(List<String> appIds) {
		this.appIds = appIds;
	}
	public List<String> getDnns() {
		return dnns;
	}
	public void setDnns(List<String> dnns) {
		this.dnns = dnns;
	}
	public List<String> getDnais() {
		return dnais;
	}
	public void setDnais(List<String> dnais) {
		this.dnais = dnais;
	}
	public List<String> getLadnDnns() {
		return ladnDnns;
	}
	public void setLadnDnns(List<String> ladnDnns) {
		this.ladnDnns = ladnDnns;
	}
	public List<String> getNfTypes() {
		return nfTypes;
	}
	public void setNfTypes(List<String> nfTypes) {
		this.nfTypes = nfTypes;
	}
	public List<List<List<List<String>>>> getVisitedAreas() {
		return visitedAreas;
	}
	public void setVisitedAreas(List<List<List<List<String>>>> visitedAreas) {
		this.visitedAreas = visitedAreas;
	}
	public List<List<List<String>>> getNsiIdInfos() {
		return nsiIdInfos;
	}
	public void setNsiIdInfos(List<List<List<String>>> nsiIdInfos) {
		this.nsiIdInfos = nsiIdInfos;
	}
	public List<Integer> getNsiLevelThrds() {
		return nsiLevelThrds;
	}
	public void setNsiLevelThrds(List<Integer> nsiLevelThrds) {
		this.nsiLevelThrds = nsiLevelThrds;
	}
	public List<List<String>> getQosFlowRetThds() {
		return qosFlowRetThds;
	}
	public void setQosFlowRetThds(List<List<String>> qosFlowRetThds) {
		this.qosFlowRetThds = qosFlowRetThds;
	}
	public List<String> getRanUeThrouThds() {
		return ranUeThrouThds;
	}
	public void setRanUeThrouThds(List<String> ranUeThrouThds) {
		this.ranUeThrouThds = ranUeThrouThds;
	}
	public List<List<String>> getSnssaia() {
		return snssaia;
	}
	public void setSnssaia(List<List<String>> snssaia) {
		this.snssaia = snssaia;
	}
	public List<List<String>> getCongThresholds() {
		return congThresholds;
	}
	public void setCongThresholds(List<List<String>> congThresholds) {
		this.congThresholds = congThresholds;
	}
	public List<List<String>> getNwPerfRequs() {
		return nwPerfRequs;
	}
	public void setNwPerfRequs(List<List<String>> nwPerfRequs) {
		this.nwPerfRequs = nwPerfRequs;
	}
	public List<List<String>> getBwRequs() {
		return bwRequs;
	}
	public void setBwRequs(List<List<String>> bwRequs) {
		this.bwRequs = bwRequs;
	}
	public List<List<String>> getExcepRequs() {
		return excepRequs;
	}
	public void setExcepRequs(List<List<String>> excepRequs) {
		this.excepRequs = excepRequs;
	}
	public List<List<List<String>>> getRatFreqs() {
		return ratFreqs;
	}
	public void setRatFreqs(List<List<List<String>>> ratFreqs) {
		this.ratFreqs = ratFreqs;
	}
	public List<String> getListOfAnaSubsets() {
		return listOfAnaSubsets;
	}
	public void setListOfAnaSubsets(List<String> listOfAnaSubsets) {
		this.listOfAnaSubsets = listOfAnaSubsets;
	}
	public List<List<List<List<String>>>> getDisperReqs() {
		return disperReqs;
	}
	public void setDisperReqs(List<List<List<List<String>>>> disperReqs) {
		this.disperReqs = disperReqs;
	}
	public List<List<String>> getRedTransReqs() {
		return redTransReqs;
	}
	public void setRedTransReqs(List<List<String>> redTransReqs) {
		this.redTransReqs = redTransReqs;
	}
	public List<List<List<String>>> getWlanReqs() {
		return wlanReqs;
	}
	public void setWlanReqs(List<List<List<String>>> wlanReqs) {
		this.wlanReqs = wlanReqs;
	}
	public List<List<String>> getAppServerAddrs() {
		return appServerAddrs;
	}
	public void setAppServerAddrs(List<List<String>> appServerAddrs) {
		this.appServerAddrs = appServerAddrs;
	}
	public List<List<List<List<String>>>> getDnPerfReqs() {
		return dnPerfReqs;
	}
	public void setDnPerfReqs(List<List<List<List<String>>>> dnPerfReqs) {
		this.dnPerfReqs = dnPerfReqs;
	}
	public List<List<List<String>>> getNetworkArea() {
		return networkArea;
	}
	public void setNetworkArea(List<List<List<String>>> networkArea) {
		this.networkArea = networkArea;
	}
	public List<String> getQosRequ() {
		return qosRequ;
	}
	public void setQosRequ(List<String> qosRequ) {
		this.qosRequ = qosRequ;
	}
	public List<List<List<List<List<List<String>>>>>> getExptUeBehav() {
		return exptUeBehav;
	}
	public void setExptUeBehav(List<List<List<List<List<List<String>>>>>> exptUeBehav) {
		this.exptUeBehav = exptUeBehav;
	}
	public List<String> getUpfInfo() {
		return upfInfo;
	}
	public void setUpfInfo(List<String> upfInfo) {
		this.upfInfo = upfInfo;
	}

	public Integer getRepetitionPeriod() {
		return repetitionPeriod;
	}

	public void setRepetitionPeriod(Integer repetitionPeriod) {
		this.repetitionPeriod = repetitionPeriod;
	}

	public Boolean getShowExtraRepReq() {
		return showExtraRepReq;
	}

	public void setShowExtraRepReq(Boolean showExtraRepReq) {
		this.showExtraRepReq = showExtraRepReq;
	}

	public Boolean getShowTgtUe() {
		return showTgtUe;
	}

	public void setShowTgtUe(Boolean showTgtUe) {
		this.showTgtUe = showTgtUe;
	}

	public Boolean getShowLoadLevelThreshold() {
		return showLoadLevelThreshold;
	}

	public void setShowLoadLevelThreshold(Boolean showLoadLevelThreshold) {
		this.showLoadLevelThreshold = showLoadLevelThreshold;
	}

	public Boolean getShowSnssais() {
		return showSnssais;
	}

	public void setShowSnssais(Boolean showSnssais) {
		this.showSnssais = showSnssais;
	}

	public Boolean getShowAnySlice() {
		return showAnySlice;
	}

	public void setShowAnySlice(Boolean showAnySlice) {
		this.showAnySlice = showAnySlice;
	}

	public Boolean getShowNsiIdInfos() {
		return showNsiIdInfos;
	}

	public void setShowNsiIdInfos(Boolean showNsiIdInfos) {
		this.showNsiIdInfos = showNsiIdInfos;
	}

	public Boolean getShowNsiLevelThrds() {
		return showNsiLevelThrds;
	}

	public void setShowNsiLevelThrds(Boolean showNsiLevelThrds) {
		this.showNsiLevelThrds = showNsiLevelThrds;
	}

	public Boolean getShowListOfAnaSubsets() {
		return showListOfAnaSubsets;
	}

	public void setShowListOfAnaSubsets(Boolean showListOfAnaSubsets) {
		this.showListOfAnaSubsets = showListOfAnaSubsets;
	}

	public Boolean getShowSupis() {
		return showSupis;
	}

	public void setShowSupis(Boolean showSupis) {
		this.showSupis = showSupis;
	}

	public Boolean getShowNetworkArea() {
		return showNetworkArea;
	}

	public void setShowNetworkArea(Boolean showNetworkArea) {
		this.showNetworkArea = showNetworkArea;
	}

	public Boolean getShowNfLoadLvlThds() {
		return showNfLoadLvlThds;
	}

	public void setShowNfLoadLvlThds(Boolean showNfLoadLvlThds) {
		this.showNfLoadLvlThds = showNfLoadLvlThds;
	}

	public Boolean getShowNfInstanceIds() {
		return showNfInstanceIds;
	}

	public void setShowNfInstanceIds(Boolean showNfInstanceIds) {
		this.showNfInstanceIds = showNfInstanceIds;
	}

	public Boolean getShowNfSetIds() {
		return showNfSetIds;
	}

	public void setShowNfSetIds(Boolean showNfSetIds) {
		this.showNfSetIds = showNfSetIds;
	}

	public Boolean getShowMatchingDir() {
		return showMatchingDir;
	}

	public void setShowMatchingDir(Boolean showMatchingDir) {
		this.showMatchingDir = showMatchingDir;
	}

	public Boolean getShowIntGroupIds() {
		return showIntGroupIds;
	}

	public void setShowIntGroupIds(Boolean showIntGroupIds) {
		this.showIntGroupIds = showIntGroupIds;
	}

	public Boolean getShowBwRequs() {
		return showBwRequs;
	}

	public void setShowBwRequs(Boolean showBwRequs) {
		this.showBwRequs = showBwRequs;
	}

	public Boolean getShowRatFreqs() {
		return showRatFreqs;
	}

	public void setShowRatFreqs(Boolean showRatFreqs) {
		this.showRatFreqs = showRatFreqs;
	}

	public Boolean getShowAppServerAddrs() {
		return showAppServerAddrs;
	}

	public void setShowAppServerAddrs(Boolean showAppServerAddrs) {
		this.showAppServerAddrs = showAppServerAddrs;
	}

	public Boolean getShowUpfInfo() {
		return showUpfInfo;
	}

	public void setShowUpfInfo(Boolean showUpfInfo) {
		this.showUpfInfo = showUpfInfo;
	}

	public Boolean getShowLadnDnns() {
		return showLadnDnns;
	}

	public void setShowLadnDnns(Boolean showLadnDnns) {
		this.showLadnDnns = showLadnDnns;
	}

	public Boolean getShowVisitedAreas() {
		return showVisitedAreas;
	}

	public void setShowVisitedAreas(Boolean showVisitedAreas) {
		this.showVisitedAreas = showVisitedAreas;
	}

	public Boolean getShowQosFlowRetThds() {
		return showQosFlowRetThds;
	}

	public void setShowQosFlowRetThds(Boolean showQosFlowRetThds) {
		this.showQosFlowRetThds = showQosFlowRetThds;
	}

	public Boolean getShowRanUeThrouThds() {
		return showRanUeThrouThds;
	}

	public void setShowRanUeThrouThds(Boolean showRanUeThrouThds) {
		this.showRanUeThrouThds = showRanUeThrouThds;
	}

	public Boolean getShowExptAnaType() {
		return showExptAnaType;
	}

	public void setShowExptAnaType(Boolean showExptAnaType) {
		this.showExptAnaType = showExptAnaType;
	}

	public Boolean getShowExptUeBehav() {
		return showExptUeBehav;
	}

	public void setShowExptUeBehav(Boolean showExptUeBehav) {
		this.showExptUeBehav = showExptUeBehav;
	}

	public Boolean getShowGpsis() {
		return showGpsis;
	}

	public void setShowGpsis(Boolean showGpsis) {
		this.showGpsis = showGpsis;
	}

	public Boolean getShowCongThresholds() {
		return showCongThresholds;
	}

	public void setShowCongThresholds(Boolean showCongThresholds) {
		this.showCongThresholds = showCongThresholds;
	}

	public Boolean getShowMaxTopAppUlNbr() {
		return showMaxTopAppUlNbr;
	}

	public void setShowMaxTopAppUlNbr(Boolean showMaxTopAppUlNbr) {
		this.showMaxTopAppUlNbr = showMaxTopAppUlNbr;
	}

	public Boolean getShowMaxTopAppDlNbr() {
		return showMaxTopAppDlNbr;
	}

	public void setShowMaxTopAppDlNbr(Boolean showMaxTopAppDlNbr) {
		this.showMaxTopAppDlNbr = showMaxTopAppDlNbr;
	}

	public Boolean getShowDisperReqs() {
		return showDisperReqs;
	}

	public void setShowDisperReqs(Boolean showDisperReqs) {
		this.showDisperReqs = showDisperReqs;
	}

	public Boolean getShowRedTransReqs() {
		return showRedTransReqs;
	}

	public void setShowRedTransReqs(Boolean showRedTransReqs) {
		this.showRedTransReqs = showRedTransReqs;
	}

	public Boolean getShowWlanReqs() {
		return showWlanReqs;
	}

	public void setShowWlanReqs(Boolean showWlanReqs) {
		this.showWlanReqs = showWlanReqs;
	}

	public Boolean getShowDnais() {
		return showDnais;
	}

	public void setShowDnais(Boolean showDnais) {
		this.showDnais = showDnais;
	}

	public Boolean getShowDnPerfReqs() {
		return showDnPerfReqs;
	}

	public void setShowDnPerfReqs(Boolean showDnPerfReqs) {
		this.showDnPerfReqs = showDnPerfReqs;
	}
}
