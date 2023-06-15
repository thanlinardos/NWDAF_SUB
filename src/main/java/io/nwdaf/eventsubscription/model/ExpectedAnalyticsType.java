package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - MOBILITY: Mobility related abnormal behaviour analytics is expected by the consumer. - COMMUN: Communication related abnormal behaviour analytics is expected by the consumer. - MOBILITY_AND_COMMUN: Both mobility and communication related abnormal behaviour analytics is expected by the consumer. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface ExpectedAnalyticsType {

}
