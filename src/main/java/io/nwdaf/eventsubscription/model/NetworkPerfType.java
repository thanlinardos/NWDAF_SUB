package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - GNB_ACTIVE_RATIO: Indicates that the network performance requirement is gNodeB active (i.e. up and running) rate. Indicates the ratio of gNB active (i.e. up and running) number to the total number of gNB - GNB_COMPUTING_USAGE: Indicates gNodeB computing resource usage. - GNB_MEMORY_USAGE: Indicates gNodeB memory usage. - GNB_DISK_USAGE: Indicates gNodeB disk usage. - NUM_OF_UE: Indicates number of UEs. - SESS_SUCC_RATIO: Indicates ratio of successful setup of PDU sessions to total PDU session setup attempts. - HO_SUCC_RATIO: Indicates Ratio of successful handovers to the total handover attempts.  
*/
@Schema(description = "Represents the network performance types.")
@Validated
public class NetworkPerfType {
  public enum NetworkPerfTypeEnum {
    GNB_ACTIVE_RATIO("GNB_ACTIVE_RATIO"),
    GNB_COMPUTING_USAGE("GNB_COMPUTING_USAGE"),
    GNB_MEMORY_USAGE("GNB_MEMORY_USAGE"),
    GNB_DISK_USAGE("GNB_DISK_USAGE"),
    NUM_OF_UE("NUM_OF_UE"),
    SESS_SUCC_RATIO("SESS_SUCC_RATIO"),
    HO_SUCC_RATIO("HO_SUCC_RATIO");
    private String value;

    NetworkPerfTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NetworkPerfTypeEnum fromValue(String text) {
      for (NetworkPerfTypeEnum b : NetworkPerfTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("nwPerfType")
  private NetworkPerfTypeEnum nwPerfType = null;

  public NetworkPerfType nwPerfType(NetworkPerfTypeEnum nwPerfType){
    this.nwPerfType = nwPerfType;
    return this;
  }

  /** Get nwPerfType
  * @return nwPerfType
  **/
  @Schema(description="nwPerfType")

  public NetworkPerfTypeEnum getnwPerfType(){
    return nwPerfType;
  }

  public void setnwPerfType(NetworkPerfTypeEnum nwPerfType){
    this.nwPerfType = nwPerfType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkPerfType networkPerfTypeObject = (NetworkPerfType) o;
    return Objects.equals(this.nwPerfType, networkPerfTypeObject.nwPerfType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nwPerfType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NetworkPerfType {\n");
    
    sb.append("    nwPerfType: ").append(toIndentedString(nwPerfType)).append("\n");
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
