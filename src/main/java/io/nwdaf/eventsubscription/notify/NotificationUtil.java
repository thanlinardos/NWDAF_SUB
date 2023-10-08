package io.nwdaf.eventsubscription.notify;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.customModel.DiscoverMessage;
import io.nwdaf.eventsubscription.customModel.WakeUpMessage;
import io.nwdaf.eventsubscription.datacollection.dummy.DummyDataProducerListener;
import io.nwdaf.eventsubscription.datacollection.dummy.DummyDataProducerPublisher;
import io.nwdaf.eventsubscription.datacollection.prometheus.DataCollectionListener;
import io.nwdaf.eventsubscription.datacollection.prometheus.DataCollectionPublisher;
import io.nwdaf.eventsubscription.kafka.KafkaConsumer;
import io.nwdaf.eventsubscription.kafka.KafkaProducer;
import io.nwdaf.eventsubscription.kafka.datacollection.dummy.KafkaDummyDataListener;
import io.nwdaf.eventsubscription.kafka.datacollection.dummy.KafkaDummyDataPublisher;
import io.nwdaf.eventsubscription.kafka.datacollection.prometheus.KafkaDataCollectionListener;
import io.nwdaf.eventsubscription.kafka.datacollection.prometheus.KafkaDataCollectionPublisher;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.utilities.ConvertUtil;
import io.nwdaf.eventsubscription.utilities.OtherUtil;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.MetricsService;

public class NotificationUtil {
	private static Logger logger = LoggerFactory.getLogger(NotificationUtil.class);

    //check if subscription needs to be served and return the repetition period (0 for threshold)
	public static Integer needsServing(NnwdafEventsSubscription body, Integer eventIndex) {
		NotificationMethodEnum notificationMethod=null;
		Integer repetitionPeriod=null;
		logger.hashCode();
		if(body.getEventSubscriptions().get(eventIndex)!=null) {
			if(body.getEventSubscriptions().get(eventIndex).getNotificationMethod()!=null &&body.getEventSubscriptions().get(eventIndex).getNotificationMethod().getNotifMethod()!=null) {
				notificationMethod = body.getEventSubscriptions().get(eventIndex).getNotificationMethod().getNotifMethod();
				if(notificationMethod.equals(NotificationMethodEnum.PERIODIC)) {
					repetitionPeriod = body.getEventSubscriptions().get(eventIndex).getRepetitionPeriod();
				}
			}
		}
		
		if(body.getEvtReq()!=null) {
			if(body.getEvtReq().getNotifFlag()!=null&&CheckUtil.safeCheckObjectsEquals(body.getEvtReq().getNotifFlag().getNotifFlag(), NotificationFlagEnum.DEACTIVATE)) {
				return null;
			}
			if(!CheckUtil.safeCheckObjectsEquals(body.getEvtReq().getNotifMethod().getNotifMethod(), null)) {
				notificationMethod = body.getEvtReq().getNotifMethod().getNotifMethod();
				if(body.getEvtReq().getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
					repetitionPeriod = body.getEvtReq().getRepPeriod();
				}
			}
		}
		if(CheckUtil.safeCheckObjectsEquals(notificationMethod,NotificationMethodEnum.THRESHOLD)) {
			return 0;
		}
		return repetitionPeriod;
	}

	// check if client wants past/future data by setting no_secs parameter
	public static Integer[] findRequestedDataOffset(EventSubscription eventSub){
		Integer no_secs = null;
		Integer isFuture = 0;
		if(eventSub.getExtraReportReq().getEndTs()!=null && eventSub.getExtraReportReq().getStartTs()!=null){
			if(eventSub.getExtraReportReq().getEndTs().getSecond() < eventSub.getExtraReportReq().getStartTs().getSecond()){
				return new Integer[]{null, 0};
			}
			// future data
			if(eventSub.getExtraReportReq().getEndTs().getSecond() > OffsetDateTime.now().getSecond()){
				// both stat predictions not allowed
				if(eventSub.getExtraReportReq().getStartTs().getSecond() < OffsetDateTime.now().getSecond()){
					return new Integer[]{null, 2};
				}
				isFuture = 1;
			}
			no_secs = eventSub.getExtraReportReq().getEndTs().getSecond()-eventSub.getExtraReportReq().getStartTs().getSecond();
		}
		else{
			if(eventSub.getExtraReportReq().getStartTs()!=null){
				// if no endTs is given , get the duration by subtracting from current time
				no_secs = OffsetDateTime.now().getSecond()-eventSub.getExtraReportReq().getStartTs().getSecond();
			}
			// if startTs & endTs aren't given , check offsetPeriod for a duration value: 
			else{
				if(eventSub.getExtraReportReq().getOffsetPeriod()!=null){
					if(eventSub.getExtraReportReq().getOffsetPeriod()<0){
						no_secs = (-1) * eventSub.getExtraReportReq().getOffsetPeriod();
					}
					// future data
					else if(eventSub.getExtraReportReq().getOffsetPeriod()>0){
						isFuture = 1;
						no_secs = eventSub.getExtraReportReq().getOffsetPeriod();
					}
				}
			}
		}
		return new Integer[]{no_secs, isFuture};
	}
    // get the notification for the event subscription by querrying the given service
    public static NnwdafEventsSubscriptionNotification getNotification(NnwdafEventsSubscription sub,Integer index,NnwdafEventsSubscriptionNotification notification, MetricsService metricsService) throws JsonMappingException, JsonProcessingException, Exception {
		OffsetDateTime now = OffsetDateTime.now();
		EventSubscription eventSub = sub.getEventSubscriptions().get(index);
		NwdafEventEnum eType = eventSub.getEvent().getEvent();
		NotificationBuilder notifBuilder = new NotificationBuilder();
		ObjectWriter ow = new ObjectMapper().writer();
		String params=null;
		String columns="";
		Integer[] findOffset = findRequestedDataOffset(eventSub);
		Integer no_secs = findOffset[0];
		Integer isFuture = findOffset[1];
		Integer repPeriod = needsServing(sub, index);
		// future data not implemented | subscription doesn't need serving:
		if(isFuture == 1 || repPeriod == null){
			return null;
		}
		switch(eType) {
		case NF_LOAD:
			List<NfLoadLevelInformation> nfloadlevels = new ArrayList<>();
			// choose the querry filter: nfinstanceids take priority over nfsetids and supis over all
			// Supis filter (checks if the supis list on each nfinstance contains any of the supis in the sub request)
			if(!eventSub.getTgtUe().isAnyUe() && CheckUtil.safeCheckListNotEmpty(eventSub.getTgtUe().getSupis())){
				params = ParserUtil.parseQuerryFilterContains(eventSub.getTgtUe().getSupis(),"supis");
			}
			else if(CheckUtil.safeCheckListNotEmpty(eventSub.getNfInstanceIds())){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseListToFilterList(eventSub.getNfInstanceIds(), "nfInstanceId"));
			}
			else if(CheckUtil.safeCheckListNotEmpty(eventSub.getNfSetIds())){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseListToFilterList(eventSub.getNfSetIds(), "nfSetId"));
			}
			// AOI filter
			else if(eventSub.getNetworkArea()!=null && CheckUtil.safeCheckListNotEmpty(eventSub.getNetworkArea().getContainedAreaIds()) && (CheckUtil.safeCheckListNotEmpty(eventSub.getNetworkArea().getEcgis()) ||
					CheckUtil.safeCheckListNotEmpty(eventSub.getNetworkArea().getNcgis()) || 
					CheckUtil.safeCheckListNotEmpty(eventSub.getNetworkArea().getGRanNodeIds()) || 
					CheckUtil.safeCheckListNotEmpty(eventSub.getNetworkArea().getTais())
			)){
				// aggregate container areas for the aoi inside the sub object as valid filters
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseListToFilterList(eventSub.getNetworkArea().getContainedAreaIds(), "areaOfInterestId"));
			}
			// Network Slice Instances filter
			else if(CheckUtil.safeCheckListNotEmpty(eventSub.getSnssaia())){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseListToFilterList(ParserUtil.parseObjectListToFilterList(ConvertUtil.convertObjectWriterList(eventSub.getSnssaia(),ow)),"snssai"));
			}
			// NfTypes filter
			else if(CheckUtil.safeCheckListNotEmpty(eventSub.getNfTypes())){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseListToFilterList(ParserUtil.parseObjectListToFilterList(ConvertUtil.convertObjectWriterList(eventSub.getNfTypes(), ow)),"nfType"));
			}
			// if given analyticsubsets list in the sub request, select only the appropriate columns in the request
			columns = "";
			columns = OtherUtil.setNfloadPostgresColumns(columns, eventSub.getListOfAnaSubsets());
			
			try{
				// repetition period is used also as the granularity (offset) for the querry
				nfloadlevels = metricsService.findAllInLastIntervalByFilterAndOffset(params, no_secs, repPeriod, columns);
			} catch(Exception e){
				NwdafSubApplication.getLogger().error("Can't find nf load metrics from database", e);
				return null;
			}
			if(nfloadlevels==null || nfloadlevels.size()==0) {
				return null;
			}
			// if given analyticsubsets list in the sub request, set the non-included info to null
			nfloadlevels = OtherUtil.setNfloadNonIncludedInfoNull(nfloadlevels, eventSub.getListOfAnaSubsets());
			notification = notifBuilder.addEvent(notification, NwdafEventEnum.NF_LOAD, null, null, now, null, null, null, nfloadlevels);	
			break;
		case UE_MOBILITY:
			List<UeMobility> ueMobilities = new ArrayList<>();
			params = null;
			columns = "";
			try{
				ueMobilities = metricsService.findAllUeMobilityInLastIntervalByFilterAndOffset(params, no_secs, repPeriod, columns);
			} catch(Exception e){
				NwdafSubApplication.getLogger().error("Can't ue mobility find metrics from database", e);
				return null;
			}
			if(ueMobilities==null || ueMobilities.size()==0) {
				return null;
			}
			notification = notifBuilder.addEvent(notification, NwdafEventEnum.UE_MOBILITY, null, null, now, null, null, null, ueMobilities);
			break;
		default:
			break;
		}
		// notifCorrId field is used as the eventSubscription index, so that the client can group the notifications it receives
		notification.setNotifCorrId(index.toString());
		return notification;
	}
	
	public static Integer[] wakeUpDataProducer(String choise, NwdafEventEnum requestedEvent, Integer requestedOffset,
		DataCollectionPublisher dataCollectionPublisher,DummyDataProducerPublisher dummyDataProducerPublisher,
		KafkaDummyDataPublisher kafkaDummyDataPublisher,KafkaDataCollectionPublisher kafkaDataCollectionPublisher,
		KafkaProducer kafkaProducer, ObjectMapper objectMapper) throws InterruptedException, IOException{
		switch(choise){
			case "prom":
				// check if it needs to wake up data collector
				if(DataCollectionListener.getNo_dataCollectionEventListeners()==0){
					dataCollectionPublisher.publishDataCollection("");
					//wait for data collection to start
					while((!DataCollectionListener.getStartedSavingData()) && DataCollectionListener.getNo_dataCollectionEventListeners()>0){
						Thread.sleep(50);
					}
					Thread.sleep(50);
				}
				break;
			case "dummy":
				// check if it needs to wake up dummy data producer
				if(DummyDataProducerListener.getNo_dummyDataProducerEventListeners()==0){
					dummyDataProducerPublisher.publishDataCollection("dummy data production");
					//wait for data publishing to start
					while((!DummyDataProducerListener.getStartedSavingData()) && DummyDataProducerListener.getNo_dummyDataProducerEventListeners()>0){
						Thread.sleep(50);
					}
					Thread.sleep(50);
				}
				break;
			case "kafka_local_dummy":
				// check if it needs to wake up kafka dummy data sender
				if(KafkaDummyDataListener.getNo_kafkaDummyDataListeners()==0){
					kafkaDummyDataPublisher.publishDataCollection("kafka dummy data production");
					//wait for data sending & saving to start
					while(  !KafkaDummyDataListener.getStartedSendingData() &&
							KafkaDummyDataListener.getNo_kafkaDummyDataListeners()>0 && 
							KafkaConsumer.isListening &&
							!KafkaConsumer.startedSavingData	
						){
						Thread.sleep(50);
					}
					Thread.sleep(50);
				}
				break;
			case "kafka_local_prom":
				// check if it needs to wake up kafka prom data collector
				if(KafkaDataCollectionListener.getNo_dataCollectionEventListeners()==0){
					kafkaDataCollectionPublisher.publishDataCollection("kafka prom data production");
					//wait for data sending & saving to start
					while(  !KafkaDataCollectionListener.getStartedSendingData() &&
							KafkaDataCollectionListener.getNo_dataCollectionEventListeners()>0 && 
							KafkaConsumer.isListening &&
							!KafkaConsumer.startedSavingData	
						){
						Thread.sleep(50);
					}
					Thread.sleep(50);
				}
				break;
			case "kafka":
				// start the kafka consumer listener
				if(!KafkaConsumer.isListening){
					KafkaConsumer.startListening();
					Thread.sleep(50);
				}
				// hit up data producers through kafka topic "WAKE_UP"
				kafkaProducer.sendMessage(WakeUpMessage.builder().requestedEvent(requestedEvent).requestedOffset(requestedOffset).build().toString(), "WAKE_UP");
				// wait for data sending & saving to start
				long maxWait = 200;
				long wait_time = 0;
				boolean responded = false;
				List<DiscoverMessage> discoverMessages = new ArrayList<>();
				while(wait_time<maxWait){
					while(KafkaConsumer.discoverMessageQueue.size()>0){
						try{
						discoverMessages.add(objectMapper.reader().readValue(KafkaConsumer.discoverMessageQueue.take(),DiscoverMessage.class));
						} catch(IOException e){}
						responded = true;
					}
					if(responded){
						// remove duplicate messages
						discoverMessages = new ArrayList<>(new LinkedHashSet<>(discoverMessages));
						break;
					}
					Thread.sleep(50);
					wait_time += 50;
				}
				int isDataAvailable = 0;
				int expectedWaitTime = 0;
				for(DiscoverMessage msg : discoverMessages){
					System.out.println("discover msg: "+msg);
					if(msg.getHasData() && msg.getAvailableOffset()!=null && msg.getRequestedOffset()!=null){
						if(msg.getAvailableOffset()>=msg.getRequestedOffset()){
							isDataAvailable = 1;
							break;
						}
						else{
							expectedWaitTime = msg.getRequestedOffset() - msg.getAvailableOffset();
						}
					}
					if(!msg.getHasData() && msg.getAvailableOffset()!=null && msg.getRequestedOffset()!=null && msg.getExpectedWaitTime()!=null){
						expectedWaitTime = msg.getExpectedWaitTime() + msg.getRequestedOffset();
					}
				}
				wait_time = 0;
				if(isDataAvailable==1){
					while(KafkaConsumer.isListening && !KafkaConsumer.startedSavingData && wait_time<maxWait){
						Thread.sleep(50);
						wait_time += 50;
					}
				}
				return new Integer[]{isDataAvailable, expectedWaitTime};
			default:
				break;	
		}
		return new Integer[]{null, null};		
	}
}
