package io.nwdaf.eventsubscription.api.config;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.nwdaf.eventsubscription.ClientApplication;
import io.nwdaf.eventsubscription.Regex;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;

public class RequestSubscriptionModel implements Serializable{
	@JsonProperty("id")
	@Id
	private Integer id;
	@JsonProperty("optionals")
	private List<String> optionals = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null,null,null,null));
	@JsonProperty("partitionCriteria")
	private List<String> partitionCriteria = new ArrayList<String>();
	@JsonProperty("nfAnaEvents")
	private List<String> nfAnaEvents = new ArrayList<String>();
	@JsonProperty("ueAnaEvents")
	private List<List<List<String>>> ueAnaEvents = new ArrayList<List<List<String>>>();
	@JsonProperty("taiList")
	private List<List<String>> taiList = new ArrayList<List<String>>();
	
	private final List<String> taiAttributes = new ArrayList<String>(Arrays.asList("mcc","mnc","tac","nid"));
	private final List<String> taiPatterns = new ArrayList<String>(Arrays.asList(Regex.mcc,Regex.mnc,Regex.tac,Regex.nid));
	
	private Boolean showEvtReq = false;
	private Boolean showPrevSub = false;
	private Boolean showConsNfInfo = false;
	
	private Boolean immRep;
	private NotificationMethodEnum notifMethod;
	private Integer maxReportNbr;
	private String monDur;
	private Integer repPeriod;
	private Integer sampRatio;
	private Integer grpRepTime;
	private String producerId;
	private String producerSetId;
	private String subscriptionId;
	private NotificationFlagEnum notifFlag;
	private String notifCorrId;
	private String nfId;
	private String nfSetId;
	
	private List<RequestEventModel> eventList = new ArrayList<RequestEventModel>();
	
	public void setAllLists() {
		if(immRep!=null) {
			if(immRep) {
				this.optionals.set(0,"TRUE");
			}
			else {
				this.optionals.set(0,"FALSE");
			}
		}
		if(notifMethod!=null) {
			this.optionals.set(1, notifMethod.toString());
		}
		if(maxReportNbr!=null) {
			this.optionals.set(2, String.valueOf(maxReportNbr));
		}
		if(monDur!=null && monDur!="") {
			monDur = monDur + ZonedDateTime.now().getOffset().getId();
			this.optionals.set(3,monDur);
		}
		if(repPeriod!=null) {
			this.optionals.set(4, String.valueOf(repPeriod));
		}
		if(sampRatio!=null) {
			this.optionals.set(5, String.valueOf(sampRatio));
		}
		if(grpRepTime!=null) {
			this.optionals.set(6, String.valueOf(grpRepTime));
		}
		if(notifFlag!=null) {
			this.optionals.set(7, notifFlag.toString());
		}
		if(producerId!=null) {
			if(Pattern.matches(Regex.uuid, producerId)) {
				this.optionals.set(8, producerId);
			}
		}
		if(producerSetId!=null) {
			if(Pattern.matches(Regex.nf_set_id_plmn, producerSetId) || Pattern.matches(Regex.nf_set_id_snpn, producerSetId)) {
				this.optionals.set(9, producerSetId);
			}
		}
		if(subscriptionId!=null) {
			this.optionals.set(10, subscriptionId);
		}
		if(notifCorrId!=null) {
			this.optionals.set(11, notifCorrId);
		}
		if(nfId!=null) {
			if(Pattern.matches(Regex.uuid,nfId)) {
				this.optionals.set(12, nfId);
			}
		}
		if(nfSetId!=null) {
			if(Pattern.matches(Regex.nf_set_id_plmn, nfSetId) || Pattern.matches(Regex.nf_set_id_snpn, nfSetId)) {
				this.optionals.set(13, nfSetId);
			}
		}
		for(int i=0;i<this.ueAnaEvents.size();i++) {
			if(this.ueAnaEvents.get(i).size()<2) {
				this.ueAnaEvents.get(i).add(new ArrayList<String>());
			}
		}
	}
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<String> getOptionals() {
		return optionals;
	}
	public void setOptionals(List<String> optionals) {
		this.optionals = optionals;
	}
	public void setOptionalsItem(String item,Integer index) {
		this.optionals.set(index, item);
	}
	
	public List<String> getPartitionCriteria() {
		return partitionCriteria;
	}
	public void setPartitionCriteria(List<String> partitionCriteria) {
		this.partitionCriteria = partitionCriteria;
	}
	public String getPartitionCriteria(Integer i) {
		return partitionCriteria.get(i);
	}
	public void addPartitionCriteria(String item) {
		this.partitionCriteria.add(item);
	}
	public void removePartitionCriteria(Integer i) {
		this.partitionCriteria.remove((int)i);
	}
	public void setPartitionCriteria(String item,Integer i) {
		this.partitionCriteria.set(i, item);
	}
	
	public List<String> getNfAnaEvents() {
		return nfAnaEvents;
	}
	public void setNfAnaEvents(List<String> nfAnaEvents) {
		this.nfAnaEvents = nfAnaEvents;
	}
	public void addNfAnaEvents(String item) {
		this.nfAnaEvents.add(item);
	}
	public void removeNfAnaEvents(Integer i) {
		this.nfAnaEvents.remove((int)i);
	}
	public void setNfAnaEvents(String item,Integer i) {
		this.nfAnaEvents.set(i, item);
	}
	
	public List<List<List<String>>> getUeAnaEvents() {
		return ueAnaEvents;
	}
	public void setUeAnaEvents(List<List<List<String>>> ueAnaEvents) {
		this.ueAnaEvents = ueAnaEvents;
	}
	public void addUeAnaEvents(List<List<String>> item) {
		if(item==null) {
			item = new ArrayList<List<String>>(Arrays.asList(new ArrayList<String>(),new ArrayList<String>()));
			if(item.get(0).size()==0) {
				item.get(0).add(null);
			}
			for(int i=0;i<this.ueAnaEvents.size();i++) {
				if(this.ueAnaEvents.get(i).get(0).get(0)=="") {
					this.ueAnaEvents.get(i).get(0).set(0,null);
				}
				if(this.ueAnaEvents.get(i).size()<2) {
					this.ueAnaEvents.get(i).add(new ArrayList<String>());
				}
				for(int j=0;j<this.ueAnaEvents.get(i).get(1).size();j++) {
					if(this.ueAnaEvents.get(i).get(1).get(j)=="") {
						this.ueAnaEvents.get(i).get(1).set(j,null);
					}
				}
				if(this.ueAnaEvents.get(i).get(1).size()==0) {
					this.ueAnaEvents.get(i).get(1).add(null);
				}
			}
		}
		this.ueAnaEvents.add(item);
	}
	public void removeUeAnaEvents(Integer i) {
		this.ueAnaEvents.remove((int)i);
	}
	public void setUeAnaEvents(List<List<String>> item,Integer i) {
		this.ueAnaEvents.set(i, item);
	}
	public List<String> getUeAnaEventsList(Integer i){
		return this.ueAnaEvents.get(i).get(1);
	}
	public String getUeAnaEvents(Integer i) {
		return this.ueAnaEvents.get(i).get(0).get(0);
	}
	public String getUeAnaEvents(Integer i,Integer j) {
		return this.ueAnaEvents.get(i).get(1).get(j);
	}
	public void setUeAnaEvents(String item,Integer i) {
		this.ueAnaEvents.get(i).get(0).set(0,item);
	}
	public void setUeAnaEvents(String item,Integer i,Integer j) {
		this.ueAnaEvents.get(i).get(1).set(j,item);
	}
	public void addUeAnaEventsItem(Integer rowId) {
		for(int i=0;i<this.ueAnaEvents.size();i++) {
			if(this.ueAnaEvents.get(i).size()<2) {
				this.ueAnaEvents.get(i).add(new ArrayList<String>());
			}
		}
		this.ueAnaEvents.get(rowId).get(1).add(null);
	}
	public void removeUeAnaEvents(Integer i,Integer j) {
		this.ueAnaEvents.get(i).remove((int)j);
	}
	public List<List<String>> getTaiList() {
		return taiList;
	}
	public void setTaiList(List<List<String>> taiList) {
		this.taiList = taiList;
	}
	public void addTaiList(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>();
			item.add(null);
			item.add(null);
			item.add(null);
			item.add(null);
		}
		this.taiList.add(item);
	}
	public void removeTaiList(Integer i) {
		this.taiList.remove((int)i);
	}
	public void setTaiList(List<String> item,Integer i) {
		if(item!=null) {
			this.taiList.set(i, item);
		}
		
	}
	public List<String> getTaiList(Integer i) {
		return taiList.get(i);
	}
	public void setTaiList(String item,Integer i,Integer j) {
		if(this.taiList.get(i)!=null) {
			if(Pattern.matches(taiPatterns.get(j), item)) {
				this.taiList.get(i).set(j, item);
			}
		}
	}
	public String getTaiList(Integer i,Integer j) {
		return taiList.get(i).get(j);
	}
	public Boolean getImmRep() {
		return immRep;
	}
	public void setImmRep(Boolean immRep) {
		this.immRep = immRep;
	}
	public NotificationMethodEnum getNotifMethod() {
		return notifMethod;
	}
	public void setNotifMethod(NotificationMethodEnum notifMethod) {
		this.notifMethod = notifMethod;
	}
	public Integer getMaxReportNbr() {
		return maxReportNbr;
	}
	public void setMaxReportNbr(Integer maxReportNbr) {
		this.maxReportNbr = maxReportNbr;
	}
	public String getMonDur() {
		return monDur;
	}
	public void setMonDur(String monDur) {
		this.monDur = monDur;
	}
	public Integer getRepPeriod() {
		return repPeriod;
	}
	public void setRepPeriod(Integer repPeriod) {
		this.repPeriod = repPeriod;
	}
	public Integer getSampRatio() {
		return sampRatio;
	}
	public void setSampRatio(Integer sampRatio) {
		this.sampRatio = sampRatio;
	}
	public Integer getGrpRepTime() {
		return grpRepTime;
	}
	public void setGrpRepTime(Integer grpRepTime) {
		this.grpRepTime = grpRepTime;
	}
	public NotificationFlagEnum getNotifFlag() {
		return notifFlag;
	}
	public void setNotifFlag(NotificationFlagEnum notifFlag) {
		this.notifFlag = notifFlag;
	}
	public Boolean getShowEvtReq() {
		return showEvtReq;
	}
	public void setShowEvtReq(Boolean showEvtReq) {
		this.showEvtReq = showEvtReq;
	}
	public String getProducerId() {
		return producerId;
	}
	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}
	public String getProducerSetId() {
		return producerSetId;
	}
	public void setProducerSetId(String producerSetId) {
		this.producerSetId = producerSetId;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public Boolean getShowPrevSub() {
		return showPrevSub;
	}
	public void setShowPrevSub(Boolean showPrevSub) {
		this.showPrevSub = showPrevSub;
	}
	public Boolean getShowConsNfInfo() {
		return showConsNfInfo;
	}
	public void setShowConsNfInfo(Boolean showConsNfInfo) {
		this.showConsNfInfo = showConsNfInfo;
	}
	public List<String> getTaiAttributes() {
		return taiAttributes;
	}
	public List<String> getTaiPatterns() {
		return taiPatterns;
	}
	public String getNotifCorrId() {
		return notifCorrId;
	}
	public void setNotifCorrId(String notifCorrId) {
		this.notifCorrId = notifCorrId;
	}
	public String getNfId() {
		return nfId;
	}
	public void setNfId(String nfId) {
		this.nfId = nfId;
	}
	public String getNfSetId() {
		return nfSetId;
	}
	public void setNfSetId(String nfSetId) {
		this.nfSetId = nfSetId;
	}
	public List<RequestEventModel> getEventList() {
		return eventList;
	}
	public void setEventList(List<RequestEventModel> eventList) {
		this.eventList = eventList;
	}
	
	public void addEventList(RequestEventModel event) {
		if(event==null) {
			event = new RequestEventModel();
		}
		this.eventList.add(event);
	}
	
}
