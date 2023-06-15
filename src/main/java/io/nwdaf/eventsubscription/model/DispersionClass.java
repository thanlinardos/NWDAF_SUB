package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - FIXED: Dispersion class as fixed UE its data or transaction usage at a location or a slice, is higher than its class threshold set for its all data or transaction usage. - CAMPER: Dispersion class as camper UE, its data or transaction usage at a location or a slice, is higher than its class threshold and lower than the fixed class threshold set for its all data or transaction usage.. - TRAVELLER: Dispersion class as traveller UE, its data or transaction usage at a location or a slice, is lower than the camper class threshold set for its all data or transaction usage. - TOP_HEAVY: Dispersion class as Top_Heavy UE, who&#x27;s dispersion percentile rating at a location or a slice, is higher than its class threshold. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface DispersionClass {

}
