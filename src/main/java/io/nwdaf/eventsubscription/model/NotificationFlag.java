package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - ACTIVATE: The event notification is activated. - DEACTIVATE: The event notification is deactivated and shall be muted. The available    event(s) shall be stored. - RETRIEVAL: The event notification shall be sent to the NF service consumer(s),   after that, is muted again.  
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface NotificationFlag {

}
