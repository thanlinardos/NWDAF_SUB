package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - UP: Up trend of the exception level. - DOWN: Down trend of the exception level. - UNKNOW: Unknown trend of the exception level. - STABLE: Stable trend of the exception level. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface ExceptionTrend {

}
