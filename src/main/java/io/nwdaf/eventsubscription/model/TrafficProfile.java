package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - SINGLE_TRANS_UL: Uplink single packet transmission. - SINGLE_TRANS_DL: Downlink single packet transmission. - DUAL_TRANS_UL_FIRST: Dual packet transmission, firstly uplink packet transmission   with subsequent downlink packet transmission. - DUAL_TRANS_DL_FIRST: Dual packet transmission, firstly downlink packet transmission   with subsequent uplink packet transmission.  
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface TrafficProfile {

}
