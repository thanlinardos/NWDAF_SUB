package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - \&quot;TAC\&quot;: Type Allocation Code - \&quot;SUBPLMN\&quot;: Subscriber PLMN ID - \&quot;GEOAREA\&quot;: Geographical area, i.e. list(s) of TAI(s) - \&quot;SNSSAI\&quot;: S-NSSAI - \&quot;DNN\&quot;: DNN 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface PartitioningCriteria {

}
