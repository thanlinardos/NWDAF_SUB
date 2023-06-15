package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - TIME_SLOT_START: Indicates the order of time slot start. - NUMBER_OF_UES: Indicates the order of number of UEs. - RSSI: Indicates the order of RSSI. - RTT: Indicates the order of RTT. - TRAFFIC_INFO: Indicates the order of Traffic information. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface WlanOrderingCriterion {

}
