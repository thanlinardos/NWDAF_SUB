package io.nwdaf.eventsubscription;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.nwdaf.eventsubscription.model.AnalyticsSubset;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.AnalyticsSubset.AnalyticsSubsetEnum;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.responsebuilders.NotificationBuilder;
import io.nwdaf.eventsubscription.service.MetricsService;

public class NotificationUtil {

    //check if subscription needs to be served and return the repetition period (0 for threshold)
	public static Integer needsServing(NnwdafEventsSubscription body, Integer eventIndex) {
		NotificationMethodEnum notificationMethod=null;
		Integer repetitionPeriod=null;
		
		if(body.getEventSubscriptions().get(eventIndex)!=null) {
			if(body.getEventSubscriptions().get(eventIndex).getNotificationMethod()!=null) {
				notificationMethod = body.getEventSubscriptions().get(eventIndex).getNotificationMethod().getNotifMethod();
				if(notificationMethod.equals(NotificationMethodEnum.PERIODIC)) {
					repetitionPeriod = body.getEventSubscriptions().get(eventIndex).getRepetitionPeriod();
				}
			}
		}
		
		if(body.getEvtReq()!=null) {
			if(body.getEvtReq().getNotifFlag()!=null) {
				if(body.getEvtReq().getNotifFlag().getNotifFlag()!=null) {
					if(body.getEvtReq().getNotifFlag().getNotifFlag().equals(NotificationFlagEnum.DEACTIVATE)) {
						return null;
					}
				}
			}
			if(body.getEvtReq().getNotifMethod()!=null) {
				notificationMethod = body.getEvtReq().getNotifMethod().getNotifMethod();
				if(body.getEvtReq().getNotifMethod().getNotifMethod().equals(NotificationMethodEnum.PERIODIC)) {
					repetitionPeriod = body.getEvtReq().getRepPeriod();
				}
			}
		}
		if(notificationMethod==null) {
			return null;
		}
		if(notificationMethod.equals(NotificationMethodEnum.THRESHOLD)) {
			return 0;
		}
		return repetitionPeriod;
	}

    // get the notification for the event subscription by querrying the given service
    public static NnwdafEventsSubscriptionNotification getNotification(NnwdafEventsSubscription sub,Integer index,NnwdafEventsSubscriptionNotification notification, MetricsService metricsService) throws JsonMappingException, JsonProcessingException, Exception {
		OffsetDateTime now = OffsetDateTime.now();
		EventSubscription eventSub = sub.getEventSubscriptions().get(index);
		NwdafEventEnum eType = eventSub.getEvent().getEvent();
		NotificationBuilder notifBuilder = new NotificationBuilder();
		String params=null;
		Integer no_secs=null;
		Integer repPeriod=null;
		ObjectMapper mapper= new ObjectMapper();
		ObjectWriter ow = mapper.writer();
		// check if client wants past/future data by setting no_secs parameter
		if(eventSub.getExtraReportReq().getEndTs()!=null && eventSub.getExtraReportReq().getStartTs()!=null){
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
					else if(eventSub.getExtraReportReq().getOffsetPeriod()>0){
						//not implemented for future data
					}
				}
			}
		}
		repPeriod = needsServing(sub, index);
		if(repPeriod == null){
			return null;
		}
		switch(eType) {
		case NF_LOAD:
			List<NfLoadLevelInformation> nfloadlevels = new ArrayList<>();
			// choose the querry filter: nfinstanceids take priority over nfsetids and supis over all
			// Supis filter (checks if the supis list on each nfinstance contains any of the supis in the sub request)
			if(!eventSub.getTgtUe().isAnyUe() && eventSub.getTgtUe().getSupis()!=null){
				params = ParserUtil.parseQuerryFilterContains(eventSub.getTgtUe().getSupis(),"supis");
			}
			else if(eventSub.getNfInstanceIds()!=null){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseListToFilterList(eventSub.getNfInstanceIds(), "nfInstanceId"));
			}
			else if(eventSub.getNfSetIds()!=null){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseListToFilterList(eventSub.getNfInstanceIds(), "nfSetId"));
			}
			// AOI filter
			else if(eventSub.getNetworkArea()!=null){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseListToFilterList(Arrays.asList(eventSub.getNetworkArea().getId()), "areaOfInterestId"));
			}
			// Network Slice Instances filter
			else if(eventSub.getSnssaia()!=null){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseObjectListToFilterList(ParserUtil.convertObjectWriterList(eventSub.getSnssaia(),ow)));
			}
			// NfTypes filter
			else if(eventSub.getNfTypes()!=null){
				params = ParserUtil.parseQuerryFilter(ParserUtil.parseObjectListToFilterList(ParserUtil.convertObjectWriterList(eventSub.getNfTypes(), ow)));
			}
			// if given analyticsubsets list in the sub request, select only the appropriate columns in the request
			String columns = "";
			String columnFormat = Constants.nfloadPostgresColumnFormat;
			if(ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_RESOURCE_USAGE))){
				columns += String.format(columnFormat,"nfCpuUsage");
				columns += String.format(columnFormat,"nfMemoryUsage");
				columns += String.format(columnFormat,"nfStorageUsage");
			}
			if(ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_LOAD))){
				columns += String.format(columnFormat,"nfLoadLevelAverage");
			}
			if(ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_LOAD_AVG_IN_AOI))){
				columns += String.format(columnFormat,"nfLoadAvgInAoi");
			}
			if(ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_PEAK_LOAD))){
				columns += String.format(columnFormat,"nfLoadLevelpeak");
			}
			try{
				// repetition period is used also as the granularity for the querry
				nfloadlevels = metricsService.findAllInLastIntervalByFilterAndOffset(params,no_secs,repPeriod,columns);
			} catch(Exception e){
				NwdafSubApplication.getLogger().error("Can't find metrics from database", e);
				return null;
			}
			if(nfloadlevels==null || nfloadlevels.size()==0) {
				return null;
			}
			// if given analyticsubsets list in the sub request, set the non-included info to null
			if(!ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_RESOURCE_USAGE))){
				for(int i=0;i<nfloadlevels.size();i++){
					nfloadlevels.get(i).nfCpuUsage(null).nfMemoryUsage(null).nfStorageUsage(null);
				}
			}
			if(!ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_LOAD))){
				for(int i=0;i<nfloadlevels.size();i++){
					nfloadlevels.get(i).nfLoadLevelAverage(null);
				}
			}
			if(!ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_LOAD_AVG_IN_AOI))){
				for(int i=0;i<nfloadlevels.size();i++){
					nfloadlevels.get(i).nfLoadAvgInAoi(null);
				}
			}
			if(!ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_PEAK_LOAD))){
				for(int i=0;i<nfloadlevels.size();i++){
				nfloadlevels.get(i).nfLoadLevelpeak(null);
			}
			}
			if(!ParserUtil.safeParseContains(eventSub.getListOfAnaSubsets(),new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.NF_STATUS))){
				for(int i=0;i<nfloadlevels.size();i++){
					nfloadlevels.get(i).nfStatus(null);
				}
			}
			notification = notifBuilder.addEvent(notification, NwdafEventEnum.NF_LOAD, null, null, now, null, null, null, nfloadlevels);	
			// notifCorrId field is used as the eventSubscription index, so that the client can group the notifications it receives
			notification.setNotifCorrId(index.toString());
			break;
		default:
			break;
		}
		return notification;
	}
	
}
