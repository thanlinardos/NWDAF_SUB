package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - UNEXPECTED_UE_LOCATION: Unexpected UE location - UNEXPECTED_LONG_LIVE_FLOW: Unexpected long-live rate flows - UNEXPECTED_LARGE_RATE_FLOW: Unexpected large rate flows - UNEXPECTED_WAKEUP: Unexpected wakeup - SUSPICION_OF_DDOS_ATTACK: Suspicion of DDoS attack - WRONG_DESTINATION_ADDRESS: Wrong destination address - TOO_FREQUENT_SERVICE_ACCESS: Too frequent Service Access - UNEXPECTED_RADIO_LINK_FAILURES: Unexpected radio link failures - PING_PONG_ACROSS_CELLS: Ping-ponging across neighbouring cells 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface ExceptionId {

}
