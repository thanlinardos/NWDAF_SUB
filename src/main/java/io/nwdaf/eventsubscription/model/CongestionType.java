package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - USER_PLANE: The congestion analytics type is User Plane.  - CONTROL_PLANE: The congestion analytics type is Control Plane. - USER_AND_CONTROL_PLANE: The congestion analytics type is User Plane and Control Plane. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface CongestionType {

}
