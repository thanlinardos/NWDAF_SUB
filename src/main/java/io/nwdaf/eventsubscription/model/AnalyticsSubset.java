package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - NUM_OF_UE_REG: The number of UE registered. This value is only applicable to NSI_LOAD_LEVEL event. - NUM_OF_PDU_SESS_ESTBL: The number of PDU sessions established. This value is only applicable to NSI_LOAD_LEVEL event. - RES_USAGE: The current usage of the virtual resources assigned to the NF instances belonging to a particular network slice instance. This value is only applicable to NSI_LOAD_LEVEL event. - NUM_OF_EXCEED_RES_USAGE_LOAD_LEVEL_THR: The number of times the resource usage threshold of the network slice instance is reached or exceeded if a threshold value is provided by the consumer. This value is only applicable to NSI_LOAD_LEVEL event. - PERIOD_OF_EXCEED_RES_USAGE_LOAD_LEVEL_THR: The time interval between each time the threshold being met or exceeded on the network slice (instance). This value is only applicable to NSI_LOAD_LEVEL event. - EXCEED_LOAD_LEVEL_THR_IND: Whether the Load Level Threshold is met or exceeded by the statistics value. This value is only applicable to NSI_LOAD_LEVEL event. - LIST_OF_TOP_APP_UL: The list of applications that contribute the most to the traffic in the UL direction. This value is only applicable to USER_DATA_CONGESTION event. - LIST_OF_TOP_APP_DL: The list of applications that contribute the most to the traffic in the DL direction. This value is only applicable to USER_DATA_CONGESTION event. - NF_STATUS: The availability status of the NF on the Analytics target period, expressed as a percentage of time per status value (registered, suspended, undiscoverable). This value is only applicable to NF_LOAD event. - NF_RESOURCE_USAGE: The average usage of assigned resources (CPU, memory, storage). This value is only applicable to NF_LOAD event. - NF_LOAD: The average load of the NF instance over the Analytics target period. This value is only applicable to NF_LOAD event. - NF_PEAK_LOAD: The maximum load of the NF instance over the Analytics target period. This value is only applicable to NF_LOAD event. - NF_LOAD_AVG_IN_AOI: The average load of the NF instances over the area of interest. This value is only applicable to NF_LOAD event. - DISPER_AMOUNT: Indicates the dispersion amount of the reported data volume or transaction dispersion type. This value is only applicable to DISPERSION event. - DISPER_CLASS: Indicates the dispersion mobility class: fixed, camper, traveller upon set its usage threshold, and/or the top-heavy class upon set its percentile rating threshold. This value is only applicable to DISPERSION event. - RANKING: Data/transaction usage ranking high (i.e.value 1), medium (2) or low (3). This value is only applicable to DISPERSION event. - PERCENTILE_RANKING: Percentile ranking of the target UE in the Cumulative Distribution Function of data usage for the population of all UEs. This value is only applicable to DISPERSION event. - RSSI: Indicated the RSSI in the unit of dBm. This value is only applicable to WLAN_PERFORMANCE event. - RTT: Indicates the RTT in the unit of millisecond. This value is only applicable to WLAN_PERFORMANCE event. - TRAFFIC_INFO: Traffic information including UL/DL data rate and/or Traffic volume. This value is only applicable to WLAN_PERFORMANCE event. - NUMBER_OF_UES: Number of UEs observed for the SSID. This value is only applicable to WLAN_PERFORMANCE event. - APP_LIST_FOR_UE_COMM: The analytics of the application list used by UE. This value is only applicable to UE_COMM event. - N4_SESS_INACT_TIMER_FOR_UE_COMM: The N4 Session inactivity timer. This value is only applicable to UE_COMM event. - AVG_TRAFFIC_RATE: Indicates average traffic rate. This value is only applicable to DN_PERFORMANCE event. - MAX_TRAFFIC_RATE: Indicates maximum traffic rate. This value is only applicable to DN_PERFORMANCE event. - AVG_PACKET_DELAY: Indicates average Packet Delay. This value is only applicable to DN_PERFORMANCE event. - MAX_PACKET_DELAY: Indicates maximum Packet Delay. This value is only applicable to DN_PERFORMANCE event. - AVG_PACKET_LOSS_RATE: Indicates average Loss Rate. This value is only applicable to DN_PERFORMANCE event. - UE_LOCATION: Indicates UE location information. This value is only applicable to SERVICE_EXPERIENCE event. - LIST_OF_HIGH_EXP_UE: Indicates list of high experienced UE. This value is only applicable to SM_CONGESTION event. - LIST_OF_MEDIUM_EXP_UE: Indicates list of medium experienced UE. This value is only applicable to SM_CONGESTION event. - LIST_OF_LOW_EXP_UE: Indicates list of low experienced UE. This value is only applicable to SM_CONGESTION event. - AVG_UL_PKT_DROP_RATE: Indicates average uplink packet drop rate on GTP-U path on N3. This value is only applicable to RED_TRANS_EXP event. - VAR_UL_PKT_DROP_RATE: Indicates variance of uplink packet drop rate on GTP-U path on N3. This value is only applicable to RED_TRANS_EXP event. - AVG_DL_PKT_DROP_RATE: Indicates average downlink packet drop rate on GTP-U path on N3. This value is only applicable to RED_TRANS_EXP event. - VAR_DL_PKT_DROP_RATE: Indicates variance of downlink packet drop rate on GTP-U path on N3. This value is only applicable to RED_TRANS_EXP event. - AVG_UL_PKT_DELAY: Indicates average uplink packet delay round trip on GTP-U path on N3. This value is only applicable to RED_TRANS_EXP event. - VAR_UL_PKT_DELAY: Indicates variance uplink packet delay round trip on GTP-U path on N3. This value is only applicable to RED_TRANS_EXP event. - AVG_DL_PKT_DELAY: Indicates average downlink packet delay round trip on GTP-U path on N3. This value is only applicable to RED_TRANS_EXP event. - VAR_DL_PKT_DELAY: Indicates variance downlink packet delay round trip on GTP-U path on N3. This value is only applicable to RED_TRANS_EXP event. 
*/
@Schema(description="Analytics subset used to indicate the content of the analytics")
@Validated
public class AnalyticsSubset {
  public enum AnalyticsSubsetEnum {
    NUM_OF_UE_REG("NUM_OF_UE_REG"),
    NUM_OF_PDU_SESS_ESTBL("NUM_OF_PDU_SESS_ESTBL"),
    RES_USAGE("RES_USAGE"),
    NUM_OF_EXCEED_RES_USAGE_LOAD_LEVEL_THR("NUM_OF_EXCEED_RES_USAGE_LOAD_LEVEL_THR"),
    PERIOD_OF_EXCEED_RES_USAGE_LOAD_LEVEL_THR("PERIOD_OF_EXCEED_RES_USAGE_LOAD_LEVEL_THR"),
    EXCEED_LOAD_LEVEL_THR_IND("EXCEED_LOAD_LEVEL_THR_IND"),
    LIST_OF_TOP_APP_UL("LIST_OF_TOP_APP_UL"),
    LIST_OF_TOP_APP_DL("LIST_OF_TOP_APP_DL"),
    NF_STATUS("NF_STATUS"),
    NF_RESOURCE_USAGE("NF_RESOURCE_USAGE"),
    NF_LOAD("NF_LOAD"),
    NF_PEAK_LOAD("NF_PEAK_LOAD"),
    NF_LOAD_AVG_IN_AOI("NF_LOAD_AVG_IN_AOI"),
    DISPER_AMOUNT("DISPER_AMOUNT"),
    DISPER_CLASS("DISPER_CLASS"),
    RANKING("RANKING"),
    PERCENTILE_RANKING("PERCENTILE_RANKING"),
    RSSI("RSSI"),
    RTT("RTT"),
    TRAFFIC_INFO("TRAFFIC_INFO"),
    NUMBER_OF_UES("NUMBER_OF_UES"),
    APP_LIST_FOR_UE_COMM("APP_LIST_FOR_UE_COMM"),
    N4_SESS_INACT_TIMER_FOR_UE_COMM("N4_SESS_INACT_TIMER_FOR_UE_COMM"),
    AVG_TRAFFIC_RATE("AVG_TRAFFIC_RATE"),
    MAX_TRAFFIC_RATE("MAX_TRAFFIC_RATE"),
    AVG_PACKET_DELAY("AVG_PACKET_DELAY"),
    MAX_PACKET_DELAY("MAX_PACKET_DELAY"),
    AVG_PACKET_LOSS_RATE("AVG_PACKET_LOSS_RATE"),
    UE_LOCATION("UE_LOCATION"),
    LIST_OF_HIGH_EXP_UE("LIST_OF_HIGH_EXP_UE"),
    LIST_OF_MEDIUM_EXP_UE("LIST_OF_MEDIUM_EXP_UE"),
    LIST_OF_LOW_EXP_UE("LIST_OF_LOW_EXP_UE"),
    AVG_UL_PKT_DROP_RATE("AVG_UL_PKT_DROP_RATE"),
    VAR_UL_PKT_DROP_RATE("VAR_UL_PKT_DROP_RATE"),
    AVG_DL_PKT_DROP_RATE("AVG_DL_PKT_DROP_RATE"),
    VAR_DL_PKT_DROP_RATE("VAR_DL_PKT_DROP_RATE"),
    AVG_UL_PKT_DELAY("AVG_UL_PKT_DELAY"),
    VAR_UL_PKT_DELAY("VAR_UL_PKT_DELAY"),
    AVG_DL_PKT_DELAY("AVG_DL_PKT_DELAY"),
    VAR_DL_PKT_DELAY("VAR_DL_PKT_DELAY");

    private String value;

    AnalyticsSubsetEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AnalyticsSubsetEnum fromValue(String text) {
      for (AnalyticsSubsetEnum b : AnalyticsSubsetEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("anaSubset")
  private AnalyticsSubsetEnum anaSubset = null;

  public AnalyticsSubset anaSubset(AnalyticsSubsetEnum anaSubset){
    this.anaSubset = anaSubset;
    return this;
  }

  /** Get anaSubset
  * @return anaSubset
  **/
  @Schema(description="anaSubset")

  public AnalyticsSubsetEnum getAnaSubset(){
    return anaSubset;
  }

  public void setAnaSubset(AnalyticsSubsetEnum anaSubset){
    this.anaSubset = anaSubset;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsSubset AnalyticsSubsetObject = (AnalyticsSubset) o;
    return Objects.equals(this.anaSubset, AnalyticsSubsetObject.anaSubset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anaSubset);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsSubset {\n");
    
    sb.append("    anaSubset: ").append(toIndentedString(anaSubset)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
