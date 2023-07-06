package io.nwdaf.eventsubscription.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.util.Pair;

public class RequestEventModel {
	private String event;
	private String notifMethod;
	private List<String>optionals = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null));
	private List<String> anaMeta;
	private List<String> anaMetaInd;
	private List<String> dataStatProps;
	private List<String> accPerSubset;
	private List<String>args = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null,null,null));
	private List<List<String>>nfLoadLvlThds;
	private List<String> supis;
	private List<String> intGroupIds;
	private List<String> nfInstanceIds;
	private List<String> nfSetIds;
	private List<String> appIds;
	private List<String> dnns;
	private List<String> dnais;
	private List<String> ladnDnns;
	private List<String> nfTypes;
	private List<List<List<List<String>>>> visitedAreas;
	private List<List<List<String>>> nsiIdInfos;
	private List<Integer> nsiLevelThrds;
	private List<List<String>> qosFlowRetThds;
	private List<String> ranUeThrouThds;
	private List<List<String>> snssaia;
	private List<List<String>> congThresholds;
	private List<List<String>> nwPerfRequs;
	private List<List<String>> bwRequs;
	private List<List<String>> excepRequs;
	private List<List<List<String>>> ratFreqs;
	private List<String> listOfAnaSubsets;
	private List<List<List<List<String>>>> disperReqs;
	private List<List<String>> redTransReqs;
	private List<List<List<String>>> wlanReqs;
	private List<List<String>> appServerAddrs;
	private List<List<List<List<String>>>> dnPerfReqs;
	private List<List<List<String>>> networkArea;
	private List<String> qosRequ;
	private List<List<List<List<List<List<String>>>>>> exptUeBehav;
	private List<String> upfInfo;
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getNotifMethod() {
		return notifMethod;
	}
	public void setNotifMethod(String notifMethod) {
		this.notifMethod = notifMethod;
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
}
