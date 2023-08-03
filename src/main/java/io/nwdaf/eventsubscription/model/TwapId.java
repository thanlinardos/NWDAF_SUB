package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Contain the TWAP Identifier as defined in clause 4.2.8.5.3 of 3GPP TS 23.501 or the WLAN location information as defined in clause 4.5.7.2.8 of 3GPP TS 23.402. 
 */
@Schema(description = "Contain the TWAP Identifier as defined in clause 4.2.8.5.3 of 3GPP TS 23.501 or the WLAN location information as defined in clause 4.5.7.2.8 of 3GPP TS 23.402. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class TwapId   {
  @JsonProperty("ssId")
  private String ssId = null;

  @JsonProperty("bssId")
  private String bssId = null;

  @JsonProperty("civicAddress")
  @Schema(type = "string", format = "byte",description = "string with format 'bytes' as defined in OpenAPI")
  private byte[] civicAddress = null;

  public TwapId ssId(String ssId) {
    this.ssId = ssId;
    return this;
  }

  /**
   * This IE shall contain the SSID of the access point to which the UE is attached, that is received over NGAP, see IEEE Std 802.11-2012.  
   * @return ssId
   **/
  @Schema(required = true, description = "This IE shall contain the SSID of the access point to which the UE is attached, that is received over NGAP, see IEEE Std 802.11-2012.  ")
      @NotNull

    public String getSsId() {
    return ssId;
  }

  public void setSsId(String ssId) {
    this.ssId = ssId;
  }

  public TwapId bssId(String bssId) {
    this.bssId = bssId;
    return this;
  }

  /**
   * When present, it shall contain the BSSID of the access point to which the UE is attached, for trusted WLAN access, see IEEE Std 802.11-2012.  
   * @return bssId
   **/
  @Schema(description = "When present, it shall contain the BSSID of the access point to which the UE is attached, for trusted WLAN access, see IEEE Std 802.11-2012.  ")
  
    public String getBssId() {
    return bssId;
  }

  public void setBssId(String bssId) {
    this.bssId = bssId;
  }

  public TwapId civicAddress(String civicAddressString) {
    this.civicAddress = Base64.decodeBase64(civicAddressString);
    return this;
  }

  /**
   * string with format 'bytes' as defined in OpenAPI
   * @return civicAddress
   **/
  
    public byte[] getCivicAddress() {
    return civicAddress;
  }

  public void setCivicAddress(String civicAddressString) {
    this.civicAddress = Base64.decodeBase64(civicAddressString);
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TwapId twapId = (TwapId) o;
    return Objects.equals(this.ssId, twapId.ssId) &&
        Objects.equals(this.bssId, twapId.bssId) &&
        Objects.equals(this.civicAddress, twapId.civicAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ssId, bssId, civicAddress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TwapId {\n");
    
    sb.append("    ssId: ").append(toIndentedString(ssId)).append("\n");
    sb.append("    bssId: ").append(toIndentedString(bssId)).append("\n");
    sb.append("    civicAddress: ").append(toIndentedString(civicAddress)).append("\n");
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
