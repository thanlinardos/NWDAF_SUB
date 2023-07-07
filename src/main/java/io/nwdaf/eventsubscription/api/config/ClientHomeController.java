package io.nwdaf.eventsubscription.api.config;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.nwdaf.eventsubscription.ClientApplication;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.requestbuilders.CreateSubscriptionRequestBuilder;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ClientHomeController {
	@Autowired
	private Environment env;
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping(value="/client/form")
    public String get(ModelMap model){
    	RequestSubscriptionModel object = new RequestSubscriptionModel();
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        model.addAttribute("nnwdafEventsSubscription",object);
        model.addAttribute("serverTime", OffsetDateTime.now());
        return "form";

    }
    @PostMapping(value="/client/form")
    public String post(RequestSubscriptionModel object, ModelMap map) throws JsonProcessingException{
        map.put("result", object);
        object.setAllLists();
        map.addAttribute("serverTime", OffsetDateTime.now());
        map.addAttribute("nnwdafEventsSubscription",object);
        CreateSubscriptionRequestBuilder subBuilder = new CreateSubscriptionRequestBuilder();
        NnwdafEventsSubscription sub = subBuilder.SubscriptionRequestBuilder(env.getProperty("nnwdaf-eventsubscription.client.dev-url"),
        object.getOptionals(),object.getPartitionCriteria(),object.getNfAnaEvents(),object.getUeAnaEvents(),object.getTaiList());
        
        for(int i=0;i<object.getEventList().size();i++) {
        	RequestEventModel e = object.getEventList().get(i);
        	e.setAllLists();
        	sub = subBuilder.AddEventToSubscription(sub, e.getEvent(), e.getNotificationMethod(), e.getOptionals(), e.getAnaMeta(), e.getAnaMetaInd(), e.getDataStatProps(),
        			e.getAccPerSubset(), e.getArgs(), e.getNfLoadLvlThds(), e.getSupis(), e.getIntGroupIds(), e.getNfInstanceIds(), e.getNfSetIds(), e.getAppIds(), e.getDnns(), e.getDnais(),
        			e.getLadnDnns(), e.getNfTypes(), e.getVisitedAreas(), e.getNsiIdInfos(), e.getNsiLevelThrds(), e.getQosFlowRetThds(), e.getRanUeThrouThds(), e.getSnssaia(), e.getCongThresholds(), e.getNwPerfRequs(), e.getBwRequs(),
        			e.getExcepRequs(), e.getRatFreqs(), e.getListOfAnaSubsets(), e.getDisperReqs(), e.getRedTransReqs(), e.getWlanReqs(), e.getAppServerAddrs(), e.getDnPerfReqs(), e.getNetworkArea(), e.getQosRequ(), e.getExptUeBehav(),e.getUpfInfo());
        }
        
        HttpEntity<NnwdafEventsSubscription> req = new HttpEntity<>(sub);
        ResponseEntity<NnwdafEventsSubscription> res = restTemplate.postForEntity(
        		env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions",
        		req, NnwdafEventsSubscription.class);
        System.out.println("Location:"+res.getHeaders().getFirst("Location"));
        String[] arr = res.getHeaders().getFirst("Location").split("/");
        object.setId(Integer.parseInt(arr[arr.length-1]));
        ClientApplication.getLogger().info(res.getBody().toString());
        map.addAttribute("location",res.getHeaders().getFirst("Location"));
        return "form";
    }
    
    @RequestMapping(value="/client/form", params={"addRow"})
    public String addRow(RequestSubscriptionModel object, ModelMap map) {
    	object.addPartitionCriteria(null);
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowtaiList"})
    public String addRowtaiList(RequestSubscriptionModel object, ModelMap map) {
    	object.addTaiList(null);
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowueAnaEvents"})
    public String addRowueAnaEvents(RequestSubscriptionModel object, ModelMap map) {
    	object.addUeAnaEvents(null);
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowueAnaEventsItem"})
    public String addRowueAnaEventsItem(RequestSubscriptionModel object, ModelMap map,final HttpServletRequest req) {
    	final Integer rowId = Integer.valueOf(req.getParameter("addRowueAnaEventsItem"));
    	object.addUeAnaEventsItem(rowId);
    	ClientApplication.getLogger().info(object.getUeAnaEvents().toString());
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"addRownfAnaEvents"})
    public String addRownfAnaEvents(RequestSubscriptionModel object, ModelMap map) {
    	object.addNfAnaEvents(null);
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    
    @RequestMapping(value="/client/form", params={"removeRow"})
    public String removeRow(
    		RequestSubscriptionModel object, ModelMap map, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRownfAnaEvents"));
        object.removeNfAnaEvents(rowId.intValue());
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRownfAnaEvents"})
    public String removeRownfAnaEvents(
    		RequestSubscriptionModel object, ModelMap map, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRownfAnaEvents"));
        object.removeNfAnaEvents(rowId.intValue());
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRowtaiList"})
    public String removeRowtaiList(
    		RequestSubscriptionModel object, ModelMap map, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRowtaiList"));
        object.removeTaiList(rowId.intValue());
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRowueAnaEvents"})
    public String removeRowueAnaEvents(
    		RequestSubscriptionModel object, ModelMap map, 
    		final HttpServletRequest req) {
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRowueAnaEvents"));
    	object.removeUeAnaEvents(rowId.intValue());
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRowueAnaEventsItem"})
    public String removeRowueAnaEventsItem(
    		RequestSubscriptionModel object, ModelMap map, 
    		final HttpServletRequest req) {
    	ClientApplication.getLogger().info(req.getParameter("removeRowueAnaEventsItem"));
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRowueAnaEventsItem").split(",")[0]);
    	final Integer eventId = Integer.valueOf(req.getParameter("removeRowueAnaEventsItem").split(",")[1]);
    	if(object.getUeAnaEvents().get(rowId).size()>1) {
    		object.removeUeAnaEvents(rowId.intValue(),eventId.intValue());
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"update"})
    public String update(
    		RequestSubscriptionModel object, ModelMap map, 
            final HttpServletRequest req) {
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    
    //Event mappings
    @RequestMapping(value="/client/form", params={"addEventRow"})
    public String addEventRow(RequestSubscriptionModel object, ModelMap map) {
    	object.addEventList(null);
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }

}
