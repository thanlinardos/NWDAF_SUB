package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - VOICE: Indicates that the service experience analytics is for voice service. - VIDEO: Indicates that the service experience analytics is for video service. - OTHER: Indicates that the service experience analytics is for other service. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface ServiceExperienceType {

}
