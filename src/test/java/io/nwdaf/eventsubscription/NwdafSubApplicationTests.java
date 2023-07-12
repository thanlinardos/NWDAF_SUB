package io.nwdaf.eventsubscription;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.nwdaf.eventsubscription.controller.SubscriptionsController;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationMethod;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.TargetUeInformation;
import io.nwdaf.eventsubscription.model.ThresholdLevel;
import io.nwdaf.eventsubscription.service.SubscriptionsService;

@RunWith(SpringRunner.class)
//@SpringBootTest
@WebMvcTest(SubscriptionsController.class)
class NwdafSubApplicationTests {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionsService service;
    
    @Autowired
	private Environment env;
    
    private ObjectMapper objectMapper;
    
	@Test
	void testCreateSubscription() throws Exception{
		objectMapper = new ObjectMapper();
		ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
		String apiRoot = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url");
		NnwdafEventsSubscription bodyObject = new NnwdafEventsSubscription();
		EventSubscription eventSub = new EventSubscription();	
		
		eventSub.event(new NwdafEvent().event(NwdafEventEnum.fromValue("NF_LOAD")));
		eventSub.notificationMethod(new NotificationMethod().notifMethod(NotificationMethodEnum.fromValue("THRESHOLD")));
		eventSub.tgtUe(new TargetUeInformation().anyUe(true));
		eventSub.addNfLoadLvlThdsItem(new ThresholdLevel().nfLoadLevel(60));
		bodyObject.notificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		bodyObject.addEventSubscriptionsItem(eventSub);
		
		String body = ow.writeValueAsString(bodyObject);
		System.out.println("Request body:");
		System.out.println(body);
		
		MvcResult result = this.mockMvc.perform(post(apiRoot+"/nwdaf-eventsubscription/v1/subscriptions")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andExpect(status().isOk())
			.andReturn();
		MockHttpServletResponse response = result.getResponse();
		System.out.println("Response:");
		System.out.println("Location:"+response.getHeader("Location"));
		System.out.println(response.getContentAsString());
	}

}
