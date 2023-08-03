package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* NF types known to NRF
*/
@Schema(description = "Represents the different types of Network Functions or Network Entities that can be found in the 5GC.")
@Validated
public class NFType {
  public enum NFTypeEnum {
    NRF("NRF"),
    UDM("UDM"),
    AMF("AMF"),
    SMF("SMF"),
    AUSF("AUSF"),
    NEF("NEF"),
    PCF("PCF"),
    SMSF("SMSF"),
    NSSF("NSSF"),
    UDR("UDR"),
    LMF("LMF"),
    GMLC("GMLC"),
    _5G_EIR("5G_EIR"),
    SEPP("SEPP"),
    UPF("UPF"),
    N3IWF("N3IWF"),
    AF("AF"),
    UDSF("UDSF"),
    BSF("BSF"),
    CHF("CHF"),
    NWDAF("NWDAF"),
    PCSCF("PCSCF"),
    CBCF("CBCF"),
    UCMF("UCMF"),
    HSS("HSS"),
    SOR_AF("SOR_AF"),
    SPAF("SPAF"),
    MME("MME"),
    SCSAS("SCSAS"),
    SCEF("SCEF"),
    SCP("SCP"),
    NSSAAF("NSSAAF"),
    ICSCF("ICSCF"),
    SCSCF("SCSCF"),
    DRA("DRA"),
    IMS_AS("IMS_AS"),
    AANF("AANF"),
    _5G_DDNMF("5G_DDNMF"),
    NSACF("NSACF"),
    MFAF("MFAF"),
    EASDF("EASDF"),
    DCCF("DCCF"),
    MB_SMF("MB_SMF"),
    TSCTSF("TSCTSF"),
    ADRF("ADRF"),
    GBA_BSF("GBA_BSF"),
    CEF("CEF"),
    MB_UPF("MB_UPF"),
    NSWOF("NSWOF"),
    PKMF("PKMF"),
    MNPF("MNPF"),
    SMS_GMSC("SMS_GMSC"),
    SMS_IWMSC("SMS_IWMSC"),
    MBSF("MBSF"),
    MBSTF("MBSTF"),
    PANF("PANF");
    private String value;

    NFTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NFTypeEnum fromValue(String text) {
      for (NFTypeEnum b : NFTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("nfType")
  private NFTypeEnum nfType = null;

  public NFType nfType(NFTypeEnum nfType){
    this.nfType = nfType;
    return this;
  }

  /** Get nfType
  * @return nfType
  **/
  @Schema(description="nfType")

  public NFTypeEnum getNfType(){
    return nfType;
  }

  public void setNfType(NFTypeEnum nfType){
    this.nfType = nfType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NFType nfTypeObject = (NFType) o;
    return Objects.equals(this.nfType, nfTypeObject.nfType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nfType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NFType {\n");
    
    sb.append("    nfType: ").append(toIndentedString(nfType)).append("\n");
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
