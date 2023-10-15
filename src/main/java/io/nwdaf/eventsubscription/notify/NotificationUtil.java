package io.nwdaf.eventsubscription.notify;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import io.nwdaf.eventsubscription.model.FailureEventInfo;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.UeMobility;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.NwdafFailureCode;
import io.nwdaf.eventsubscription.model.NwdafFailureCode.NwdafFailureCodeEnum;
import io.nwdaf.eventsubscription.notify.responsetypes.AggregateChecksForAOIResponse;
import io.nwdaf.eventsubscription.notify.responsetypes.GetGlobalNotifMethodAndRepPeriodResponse;
import io.nwdaf.eventsubscription.notify.responsetypes.GetNotifMethodAndRepPeriodsResponse;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.utilities.Constants;
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
		if(eventSub.getExtraReportReq()==null){
			return new Integer[]{null,0};
		}
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
						} catch(IOException e){
							logger.error("IOException: Couldn't read discover message");
						} catch(InterruptedException e){
							logger.error("InterruptedException: Couldn't take msg from discover queue");
							break;
						}
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
	// check if for this event subscription the requested area of interest 
	// is inside (or equals to) the service area of this NWDAF instance
	// the checks are for when the serializer initializes the lists inside aoi object with null
	public static AggregateChecksForAOIResponse aggregateChecksForAOI(NetworkAreaInfo requestNetworkArea){
		NetworkAreaInfo matchingArea = null;
		Boolean insideServiceArea = false;
		Boolean failed_notif = false;
		NwdafFailureCodeEnum failCode = null;
		if(requestNetworkArea!=null && requestNetworkArea.getId() != null){
				matchingArea = Constants.ExampleAOIsMap.get(requestNetworkArea.getId());
				insideServiceArea = Constants.ServingAreaOfInterest.containsArea(matchingArea);
		}
		if(requestNetworkArea!=null && (CheckUtil.safeCheckNetworkAreaNotEmpty(requestNetworkArea) || requestNetworkArea.getId() != null)){
			if(!(CheckUtil.safeCheckNetworkAreaNotEmpty(requestNetworkArea) && Constants.ServingAreaOfInterest.containsArea(requestNetworkArea))
				&& (matchingArea == null || !insideServiceArea)){
				failed_notif = true;
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				System.out.println("not inside serving aoi");
			}
			else{
				// check if its lists equal one of the known AOIs -> set the id
				if(Constants.ExampleAOIsToUUIDsMap.containsKey(requestNetworkArea)){
					requestNetworkArea.id(Constants.ExampleAOIsToUUIDsMap.get(requestNetworkArea));
					System.out.println("same as aoi with id: "+requestNetworkArea.getId());
				}
				// check if id equals to a known AOI -> set the area lists
				else if(requestNetworkArea.getId() != null && Constants.ExampleAOIsMap.containsKey(requestNetworkArea.getId())){
					NetworkAreaInfo matchingAOI = Constants.ExampleAOIsMap.get(requestNetworkArea.getId());
					requestNetworkArea.ecgis(matchingAOI.getEcgis()).ncgis(matchingAOI.getNcgis())
							.gRanNodeIds(matchingAOI.getGRanNodeIds()).tais(matchingAOI.getTais());
				}
				// if new AOI create id if it doesnt have one and add it to the known AOIs
				else{
					if(requestNetworkArea.getId() == null){
						requestNetworkArea.id(UUID.randomUUID());
					}
					Constants.ExampleAOIsMap.put(requestNetworkArea.getId(), requestNetworkArea);
					Constants.ExampleAOIsToUUIDsMap.put(requestNetworkArea, requestNetworkArea.getId());
				}
				// aggregate known areas that are inside of this area of interest
				for (Map.Entry<UUID,NetworkAreaInfo> entry : Constants.ExampleAOIsMap.entrySet()){
					UUID key = entry.getKey(); NetworkAreaInfo aoi = entry.getValue();
					if(requestNetworkArea.containsArea(aoi) || key.equals(requestNetworkArea.getId())){
						requestNetworkArea.addContainedAreaIdsItem(key);
					}
				}
				requestNetworkArea.setContainedAreaIds(ParserUtil.removeDuplicates(requestNetworkArea.getContainedAreaIds()));
			}
		}
		return AggregateChecksForAOIResponse.builder()
			.failCode(failCode)
			.failed_notif(failed_notif)
			.insideServiceArea(insideServiceArea)
			.matchingArea(matchingArea)
			.build();
	}

	// negotiate the supported features with the client
	public static List<Integer> negotiateSupportedFeatures(NnwdafEventsSubscription body){
		List<Integer> negotiatedFeaturesList = new ArrayList<>();
		if(body.getSupportedFeatures()!=null && !body.getSupportedFeatures().equals("")) {
			if(!Constants.supportedFeatures.equals(body.getSupportedFeatures()) && 
				CheckUtil.listInside(Constants.supportedFeaturesList, ConvertUtil.convertFeaturesToList(body.getSupportedFeatures()))) {
				negotiatedFeaturesList = ConvertUtil.convertFeaturesToList(body.getSupportedFeatures());
			}
			else{
				body.setSupportedFeatures(Constants.supportedFeatures);
				negotiatedFeaturesList = Constants.supportedFeaturesList;
			}
		}
		else {
			body.setSupportedFeatures(Constants.supportedFeatures);
			negotiatedFeaturesList = Constants.supportedFeaturesList;
		}
		return negotiatedFeaturesList;
	}
	// get global notification method and period if they exist
	public static GetGlobalNotifMethodAndRepPeriodResponse getGlobalNotifMethodAndRepPeriod(ReportingInformation evtReq){
		Integer repetionPeriod = null;
		NotificationMethodEnum notificationMethod = null;
		Boolean muted=false;
		Boolean immRep=false;
		if(evtReq!=null) {
			if(evtReq.getNotifMethod()!=null && evtReq.getNotifMethod().getNotifMethod()!=null) {
				notificationMethod = evtReq.getNotifMethod().getNotifMethod();
				if(evtReq.getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
					repetionPeriod = evtReq.getRepPeriod();
				}
				if(evtReq.getNotifFlag()!=null) {
					muted=evtReq.getNotifFlag().getNotifFlag().equals(NotificationFlagEnum.DEACTIVATE);
				}
			}
			if(evtReq.isImmRep()!=null) {
				immRep=evtReq.isImmRep();
			}
		}
		return GetGlobalNotifMethodAndRepPeriodResponse.builder()
			.immRep(immRep)
			.muted(muted)
			.notificationMethod(notificationMethod)
			.repetionPeriod(repetionPeriod)
			.build();
	}

	// get period and notification method for each event
	public static GetNotifMethodAndRepPeriodsResponse getNotifMethodAndRepPeriods(List<EventSubscription> eventSubscriptions, NotificationMethodEnum notificationMethod, Integer repetionPeriod){
		Integer no_valid_events = 0;
		List<Integer> invalid_events = new ArrayList<>();
		Map<Integer,NotificationMethodEnum> eventIndexToNotifMethodMap = new HashMap<>();
		Map<Integer,Integer> eventIndexToRepPeriodMap = new HashMap<>();
		no_valid_events = eventSubscriptions.size();
		for(int i=0;i<eventSubscriptions.size();i++) {
			EventSubscription e = eventSubscriptions.get(i);
			if(e==null || e.getEvent()==null || e.getEvent().getEvent()==null) {
				no_valid_events--;
				invalid_events.add(i);
				continue;
			}
			if(notificationMethod==null) {
				if(e.getNotificationMethod()!=null) {
					eventIndexToNotifMethodMap.put(i, e.getNotificationMethod().getNotifMethod());
				}
				else {
					eventIndexToNotifMethodMap.put(i,NotificationMethodEnum.THRESHOLD);
				}
					
			}
			else {
				eventIndexToNotifMethodMap.put(i, notificationMethod);
			}
			if(eventIndexToNotifMethodMap.get(i)!=null) {
				if(eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC)) {
					if(repetionPeriod==null) {
						eventIndexToRepPeriodMap.put(i,e.getRepetitionPeriod());
					}
					else{
						eventIndexToRepPeriodMap.put(i,repetionPeriod);
					}
							
				}
			}
			e = OtherUtil.setShapes(e);
			eventSubscriptions.set(i, e);			
		}
		return GetNotifMethodAndRepPeriodsResponse.builder()
			.eventIndexToNotifMethodMap(eventIndexToNotifMethodMap)
			.eventIndexToRepPeriodMap(eventIndexToRepPeriodMap)
			.invalid_events(invalid_events)
			.no_valid_events(no_valid_events)
			.build();
	}

	// check which subscriptions can be served
	public static List<Boolean> checkCanServeSubscriptions(Integer no_valid_events, NnwdafEventsSubscription body,
		Map<Integer,NotificationMethodEnum> eventIndexToNotifMethodMap, Map<Integer,Integer> eventIndexToRepPeriodMap,
		DataCollectionPublisher dataCollectionPublisher, DummyDataProducerPublisher dummyDataProducerPublisher,
		KafkaDummyDataPublisher kafkaDummyDataPublisher, KafkaDataCollectionPublisher kafkaDataCollectionPublisher,
		KafkaProducer kafkaProducer, ObjectMapper objectMapper, MetricsService metricsService, Boolean immRep, Long id){
		List<Boolean> canServeSubscription = new ArrayList<>();
		for(int i=0;i<no_valid_events;i++) {
			canServeSubscription.add(false);
		}
		for(int i=0;i<no_valid_events;i++) {
			EventSubscription eventSubscription = body.getEventSubscriptions().get(i);
			NwdafEventEnum eType = eventSubscription.getEvent().getEvent();
			Boolean periodic = false;
			if(eventIndexToRepPeriodMap.get(i)!=null) {
				periodic = eventIndexToNotifMethodMap.get(i).equals(NotificationMethodEnum.PERIODIC);
			}
			Boolean failed_notif = false;
			NwdafFailureCodeEnum failCode = null;
			//check if eventType is supported
			if(!Constants.supportedEvents.contains(eType)) {
				failed_notif=true;
				failCode=NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			}
			//check if period is valid (between 1sec and 10mins) and if not add failureReport
			if(periodic && (eventIndexToRepPeriodMap.get(i)<Constants.MIN_PERIOD_SECONDS || eventIndexToRepPeriodMap.get(i)>Constants.MAX_PERIOD_SECONDS)) {
				failed_notif = true;
				failCode = NwdafFailureCodeEnum.OTHER;
			}
			AggregateChecksForAOIResponse aggregateChecksForAOIResponse = NotificationUtil.aggregateChecksForAOI(eventSubscription.getNetworkArea());
			failed_notif = aggregateChecksForAOIResponse.getFailed_notif();
			failCode = aggregateChecksForAOIResponse.getFailCode();
			// find past/future offset requested by client:
			Integer[] findOffset = NotificationUtil.findRequestedDataOffset(eventSubscription);
			Integer no_secs = findOffset[0];
			// both future and past predictions not allowed
			if(findOffset[1]==2){
				body.addFailEventReportsItem(new FailureEventInfo().event(eventSubscription.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.BOTH_STAT_PRED_NOT_ALLOWED)));
				continue;
			}
			// future data not implemented
			if(findOffset[1]==1){
				body.addFailEventReportsItem(new FailureEventInfo().event(eventSubscription.getEvent()).failureCode(new NwdafFailureCode().failureCode(NwdafFailureCodeEnum.OTHER)));
				continue;
			}
			//check whether data is available to be gathered
			NnwdafEventsSubscriptionNotification notification = new NotificationBuilder().build(id);
			boolean isDataAvailable=false;
			Integer expectedWaitTime=null;
			try {
				// wakeUpDataProducer("kafka_local_dummy", eType);
				// wakeUpDataProducer("kafka_local_prom", eType);
				Integer[] wakeUpResult = NotificationUtil.wakeUpDataProducer("kafka", eType, no_secs,dataCollectionPublisher,dummyDataProducerPublisher,
					kafkaDummyDataPublisher,kafkaDataCollectionPublisher,kafkaProducer,objectMapper);
				isDataAvailable = wakeUpResult[0]==1;
				expectedWaitTime = wakeUpResult[1];
			} catch(IOException e){
				failed_notif=true;
				logger.error("Couldn't connect to kafka topic WAKE_UP.", e);
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			} catch(InterruptedException e){
				failed_notif=true;
				logger.error("Thread failed to wait for datacollection to start for event: "+eType,e);
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			}
			if(!isDataAvailable){
				failed_notif=true;
				logger.info("Informed by collector(s) that data is not available");
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			}
			if(!failed_notif){
				try {
					notification=NotificationUtil.getNotification(body, i, notification, metricsService);
					if(body.getEvtReq()!=null && immRep && notification!=null){
						body.addEventNotificationsItem(notification.getEventNotifications().get(i));
					}
				} catch (JsonProcessingException e) {
					failed_notif=true;
					logger.error("Failed to collect data for event: "+eType,e);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				} catch(Exception e) {
					failed_notif=true;
					logger.error("Failed to collect data for event(timescaledb error): "+eType,e);
					failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
				}
			}
			if(notification==null) {
				logger.error("Failed to collect data for event: "+eType);
				failCode = NwdafFailureCodeEnum.UNAVAILABLE_DATA;
			}
			// add failureEventInfo
			if(notification==null || failed_notif) {
    			body.addFailEventReportsItem(new FailureEventInfo().event(eventSubscription.getEvent()).failureCode(new NwdafFailureCode().failureCode(failCode)));
			}
			else {
				canServeSubscription.set(i, true);
				if(immRep) {
					body.addEventNotificationsItem(notification.getEventNotifications().get(0)
							.rvWaitTime(expectedWaitTime));
				}
			}
			logger.info("notifMethod="+eventIndexToNotifMethodMap.get(i)+", repPeriod="+eventIndexToRepPeriodMap.get(i));
		}
		return canServeSubscription;
	}
}