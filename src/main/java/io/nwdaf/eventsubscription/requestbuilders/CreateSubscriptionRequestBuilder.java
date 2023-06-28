package io.nwdaf.eventsubscription.requestbuilders;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.nwdaf.eventsubscription.model.Accuracy;
import io.nwdaf.eventsubscription.model.Accuracy.AccuracyEnum;
import io.nwdaf.eventsubscription.model.AnalyticsMetadata;
import io.nwdaf.eventsubscription.model.AnalyticsMetadata.AnalyticsMetadataEnum;
import io.nwdaf.eventsubscription.model.DatasetStatisticalProperty.DatasetStatisticalPropertyEnum;
import io.nwdaf.eventsubscription.model.AnalyticsMetadataIndication;
import io.nwdaf.eventsubscription.model.ConsumerNfInformation;
import io.nwdaf.eventsubscription.model.DatasetStatisticalProperty;
import io.nwdaf.eventsubscription.model.EventReportingRequirement;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.TargetUeInformation;
import io.nwdaf.eventsubscription.model.ThresholdLevel;
import io.nwdaf.eventsubscription.model.TimeWindow;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.OutputStrategy;
import io.nwdaf.eventsubscription.model.OutputStrategy.OutputStrategyEnum;
import io.nwdaf.eventsubscription.model.PartitioningCriteria;
import io.nwdaf.eventsubscription.model.PartitioningCriteria.PartitioningCriteriaEnum;
import io.nwdaf.eventsubscription.model.PlmnId;
import io.nwdaf.eventsubscription.model.PrevSubInfo;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.Tai;
import io.nwdaf.eventsubscription.model.UeAnalyticsContextDescriptor;

public class CreateSubscriptionRequestBuilder {
	
	@Autowired
	private Environment env;
	
	private ObjectMapper objectMapper;
	
	public String SubscriptionToString(NnwdafEventsSubscription sub) throws JsonProcessingException {
		objectMapper = new ObjectMapper();
		ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
		String body = ow.writeValueAsString(sub);
		return body;
	}
	public NnwdafEventsSubscription InitSubscriptionRequest() {
		NnwdafEventsSubscription bodyObject = new NnwdafEventsSubscription();
		bodyObject.notificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		
		return bodyObject;
	}

	public NnwdafEventsSubscription AddEventToSubscription(NnwdafEventsSubscription sub,String event,String notifMethod,List<String>optionals,List<String> anaMeta,List<String> anaMetaInd,List<String> dataStatProps,List<String> accPerSubset,List<String>args,List<Integer>thresholds, List<String> supis, List<String> intGroupIds, List<String> nfInstanceIds, List<String> nfSetIds) {
		EventSubscription eventSub = new EventSubscription();	
		eventSub.event(new NwdafEvent().event(NwdafEventEnum.fromValue(event)));
		eventSub.notificationMethod(new NotificationMethod().notifMethod(NotificationMethodEnum.fromValue(notifMethod)));
		EventReportingRequirement extraRepReq = new EventReportingRequirement();
		if(optionals.size()>0) {
			if(!optionals.get(0).equals("")) {
				extraRepReq.maxObjectNbr(Integer.parseInt(optionals.get(0)));
				
			}
			if(!optionals.get(1).equals("")) {
				extraRepReq.maxSupiNbr(Integer.parseInt(optionals.get(1)));
			}
			if(!optionals.get(2).equals("")) {
				extraRepReq.startTs(OffsetDateTime.parse(optionals.get(2)));
			}
			if(!optionals.get(3).equals("")) {
				extraRepReq.endTs(OffsetDateTime.parse(optionals.get(3)));
			}
			if(!optionals.get(4).equals("")) {
				extraRepReq.accuracy(new Accuracy().accuracy(AccuracyEnum.fromValue(optionals.get(4))));
			}
			if(!optionals.get(5).equals("")) {
				extraRepReq.timeAnaNeeded(OffsetDateTime.parse(optionals.get(5)));
			}
			if(!optionals.get(6).equals("")) {
				extraRepReq.offsetPeriod(Integer.parseInt(optionals.get(6)));
			}
		}
		if(anaMeta.size()>0) {
			List<AnalyticsMetadata> l = new ArrayList<AnalyticsMetadata>();
			for(int i =0;i<anaMeta.size();i++) {
				l.add(new AnalyticsMetadata().anaMeta(AnalyticsMetadataEnum.fromValue(anaMeta.get(i))));
			}
			extraRepReq.anaMeta(l);
		}
		if(anaMetaInd.size()>0) {
			AnalyticsMetadataIndication anametaind = new AnalyticsMetadataIndication();
			TimeWindow tw = new TimeWindow();
			if(!anaMetaInd.get(0).equals("")) {
				tw.startTime(OffsetDateTime.parse(anaMetaInd.get(0)));
			}
			if(!anaMetaInd.get(1).equals("")) {
				tw.stopTime(OffsetDateTime.parse(anaMetaInd.get(1)));
			}
			if((!anaMetaInd.get(0).equals(""))||(!anaMetaInd.get(1).equals(""))) {
				anametaind.dataWindow(tw);
			}
			
			if(dataStatProps.size()>0) {
				for(int i=0;i<dataStatProps.size();i++) {
					anametaind.addDataStatPropsItem(new DatasetStatisticalProperty().dataStatProps(DatasetStatisticalPropertyEnum.fromValue(dataStatProps.get(i))));
				}
			}
			if(!anaMetaInd.get(2).equals("")) {
				anametaind.strategy(new OutputStrategy().strategy(OutputStrategyEnum.fromValue(anaMetaInd.get(2))));
			}
			if(!anaMetaInd.get(3).equals("")) {
				anametaind.addAggrNwdafIdsItem(UUID.fromString(anaMetaInd.get(3)));
			}
			extraRepReq.anaMetaInd(anametaind);
		}
		if(accPerSubset.size()>0) {
			for(int i=0;i<accPerSubset.size();i++) {
				extraRepReq.addAccPerSubsetItem(new Accuracy().accuracy(AccuracyEnum.fromValue(accPerSubset.get(i))));
			}
		}
		if(optionals.size()>0 || anaMeta.size()>0 || anaMetaInd.size()>0 || accPerSubset.size()>0) {
			eventSub.extraReportReq(extraRepReq);
		}
		if(supis.size()>0||intGroupIds.size()>0||(!args.get(0).equals(""))) {
			eventSub.tgtUe(new TargetUeInformation().supis(supis).intGroupIds(intGroupIds).anyUe(args.get(0).equalsIgnoreCase("TRUE")));
		}
		
		if(thresholds.size()>0) {
			for(int i=0;i<thresholds.size();i++) {
				eventSub.addNfLoadLvlThdsItem(new ThresholdLevel().nfLoadLevel(thresholds.get(i)));
			}
			
		}
		
		
		sub.addEventSubscriptionsItem(eventSub);
		return sub;
	}
	public NnwdafEventsSubscription AddOptionalsToSubscription(NnwdafEventsSubscription sub,List<String> optionals,List<String> partitionCriteria, List<String> nfAnaEvents, List<Pair<String,List<String>>> ueAnaEvents, List<List<String>> taiList) {
		ReportingInformation evtReq = new ReportingInformation();
		PrevSubInfo prevSub = new PrevSubInfo();
		ConsumerNfInformation consNfInfo = new ConsumerNfInformation();
		if(!optionals.get(0).equals("")) {
			evtReq.immRep(optionals.get(0).equalsIgnoreCase("TRUE"));
		}
		if(!optionals.get(1).equals("")) {
			evtReq.notifMethod(new NotificationMethod().notifMethod(NotificationMethodEnum.fromValue(optionals.get(1))));
		}
		if(!optionals.get(2).equals("")) {
			evtReq.maxReportNbr(Integer.parseInt(optionals.get(2)));
		}
		if(!optionals.get(3).equals("")) {
			evtReq.monDur(OffsetDateTime.parse(optionals.get(3)));
		}
		if(!optionals.get(4).equals("")) {
			evtReq.repPeriod(Integer.parseInt(optionals.get(4)));
		}
		if(!optionals.get(5).equals("")) {
			evtReq.sampRatio(Integer.parseInt(optionals.get(5)));
		}
		if(!optionals.get(6).equals("")) {
			evtReq.grpRepTime(Integer.parseInt(optionals.get(6)));
		}
		if(!optionals.get(7).equals("")) {
			evtReq.notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.fromValue(optionals.get(7))));
		}
		if(partitionCriteria.size()>0) {
			for(int i=0;i<partitionCriteria.size();i++) {
				evtReq.addPartitionCriteriaItem(new PartitioningCriteria().partitionCriteria(PartitioningCriteriaEnum.fromValue(partitionCriteria.get(i))));
			}
		}
		if((!optionals.get(0).equals("")) ||(!optionals.get(1).equals(""))||(!optionals.get(2).equals("")) ||(!optionals.get(3).equals("")) ||(!optionals.get(4).equals("")) ||(!optionals.get(5).equals("")) ||(!optionals.get(6).equals("")) ||(!optionals.get(7).equals(""))){
			sub.evtReq(evtReq);
		}
		if(!optionals.get(8).equals("")){
			prevSub.producerId(UUID.fromString(optionals.get(8)));
		}
		if(!optionals.get(9).equals("")){
			prevSub.producerSetId(optionals.get(9));
		}
		if(!optionals.get(10).equals("")){
			prevSub.subscriptionId(optionals.get(10));
		}
		if(nfAnaEvents.size()>0) {
			for(int i=0;i<nfAnaEvents.size();i++) {
				prevSub.addNfAnaEventsItem(new NwdafEvent().event(NwdafEventEnum.fromValue(nfAnaEvents.get(i))));
			}
		}
		if(ueAnaEvents.size()>0) {
			for(int i=0;i<ueAnaEvents.size();i++) {
				UeAnalyticsContextDescriptor ueAnaEvent = new UeAnalyticsContextDescriptor();
				ueAnaEvent.supi(ueAnaEvents.get(i).getFirst());
				for(int j=0;j<ueAnaEvents.get(i).getSecond().size();j++) {
					ueAnaEvent.addAnaTypesItem(new NwdafEvent().event(NwdafEventEnum.fromValue(ueAnaEvents.get(i).getSecond().get(j))));
				}
				prevSub.addUeAnaEventsItem(ueAnaEvent);
			}
		}
		if(!optionals.get(8).equals("")||!optionals.get(9).equals("")||!optionals.get(10).equals("")||nfAnaEvents.size()>0||ueAnaEvents.size()>0) {
			sub.prevSub(prevSub);
		}
		if(!optionals.get(10).equals("")){
			sub.notifCorrId(optionals.get(10));
		}
		if(!optionals.get(11).equals("")){
			consNfInfo.nfId(UUID.fromString(optionals.get(11)));
		}
		if(!optionals.get(12).equals("")){
			consNfInfo.nfSetId(optionals.get(12));
		}
		if(taiList.size()>0) {
			for(int i=0;i<taiList.size();i++) {
				consNfInfo.addTaiListItem(new Tai().plmnId(new PlmnId().mcc(taiList.get(i).get(0)).mnc(taiList.get(i).get(1))).tac(taiList.get(i).get(2)).nid(taiList.get(i).get(3)));
			}
		}
		if((!optionals.get(11).equals(""))||(!optionals.get(12).equals(""))||taiList.size()>0) {
			sub.consNfInfo(consNfInfo);
		}
		return sub;
	}
	public NnwdafEventsSubscription SubscriptionRequestBuilder() throws JsonProcessingException {
		
		NnwdafEventsSubscription bodyObject = InitSubscriptionRequest();
		
		
		System.out.println("Request body:");
		System.out.println(SubscriptionToString(bodyObject));
		return bodyObject;
	}
}
