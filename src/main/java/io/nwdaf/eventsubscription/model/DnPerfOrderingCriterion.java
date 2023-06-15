package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are:   - AVERAGE_TRAFFIC_RATE: Indicates the average traffic rate.   - MAXIMUM_TRAFFIC_RATE: Indicates the maximum traffic rate.   - AVERAGE_PACKET_DELAY: Indicates the average packet delay.   - MAXIMUM_PACKET_DELAY: Indicates the maximum packet delay.   - AVERAGE_PACKET_LOSS_RATE: Indicates the average packet loss rate. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface DnPerfOrderingCriterion {

}
