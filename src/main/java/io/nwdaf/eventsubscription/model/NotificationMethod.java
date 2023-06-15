package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - PERIODIC: The subscribe of NWDAF Event is periodically. The periodic of the notification is identified by repetitionPeriod defined in clause 5.1.6.2.3.   - THRESHOLD: The subscribe of NWDAF Event is upon threshold exceeded. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface NotificationMethod {

}
