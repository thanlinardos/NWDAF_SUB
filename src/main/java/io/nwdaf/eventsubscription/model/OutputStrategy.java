package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - BINARY: Indicates that the analytics shall only be reported when the requested level of accuracy is reached within a cycle of periodic notification. - GRADIENT: Indicates that the analytics shall be reported according with the periodicity irrespective of whether the requested level of accuracy has been reached or not. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface OutputStrategy {

}
