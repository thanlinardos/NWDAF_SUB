package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - UDP: User Datagram Protocol. - TCP: Transmission Control Protocol.  
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface TransportProtocol {

}
