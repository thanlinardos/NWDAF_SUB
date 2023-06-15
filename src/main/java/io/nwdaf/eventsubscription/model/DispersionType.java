package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are:   - DVDA: Data Volume Dispersion Analytics.   - TDA: Transactions Dispersion Analytics.   - DVDA_AND_TDA: Data Volume Dispersion Analytics and Transactions Dispersion Analytics. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface DispersionType {

}
