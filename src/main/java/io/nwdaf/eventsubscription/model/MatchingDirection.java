package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - ASCENDING: Threshold is crossed in ascending direction. - DESCENDING: Threshold is crossed in descending direction. - CROSSED: Threshold is crossed either in ascending or descending direction. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface MatchingDirection {

}
