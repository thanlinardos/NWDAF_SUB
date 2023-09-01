package io.nwdaf.eventsubscription;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.requestbuilders.ParserUtil;
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
		
		switch(eType) {
		case NF_LOAD:
			List<NfLoadLevelInformation> nfloadlevels = new ArrayList<>();
			// choose the querry filter: nfinstanceids take priority over nfsetids
			if(eventSub.getNfInstanceIds()!=null){
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
			try{
				// repetition period is used also as the granularity for the querry
				if(repPeriod!=null){
					nfloadlevels = metricsService.findAllInLastIntervalByFilterAndOffset(params,no_secs,repPeriod);
				}
				// no repetition period assumes default granularity & period
				else{
					nfloadlevels = metricsService.findAllInLastIntervalByFilter(params,no_secs);
				}
			} catch(Exception e){
				System.out.println("Can't find metrics from database");
				if(e.getCause()!=null && e.getCause().getClass()!=null){
					System.out.println("exception:"+e.getCause().getClass().getName());
				}
				else{
					System.out.println(e);
				}
				return null;
			}
			if(nfloadlevels==null || nfloadlevels.size()==0) {
				return null;
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
