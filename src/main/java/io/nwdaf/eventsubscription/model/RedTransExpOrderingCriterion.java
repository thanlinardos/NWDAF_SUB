package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - TIME_SLOT_START: Indicates the order of time slot start. - RED_TRANS_EXP: Indicates the order of Redundant Transmission Experience. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface RedTransExpOrderingCriterion {

}
