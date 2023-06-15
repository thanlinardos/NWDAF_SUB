package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Possible values are: - NUM_OF_SAMPLES: Number of data samples used for the generation of the output analytics. - DATA_WINDOW: Data time window of the data samples. - DATA_STAT_PROPS: Dataset statistical properties of the data used to generate the analytics. - STRATEGY: Output strategy used for the reporting of the analytics. - ACCURACY: Level of accuracy reached for the analytics. 
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
})
public interface AnalyticsMetadata {

}
