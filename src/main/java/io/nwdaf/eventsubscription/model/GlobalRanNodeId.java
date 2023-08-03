package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * One of the six attributes n3IwfId, gNbIdm, ngeNbId, wagfId, tngfId, eNbId shall be present. 
 */
@Schema(description = "One of the six attributes n3IwfId, gNbIdm, ngeNbId, wagfId, tngfId, eNbId shall be present. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")

public class GlobalRanNodeId  implements OneOfGlobalRanNodeId {
  @JsonProperty("plmnId")
  private PlmnId plmnId = null;

  @JsonProperty("n3IwfId")
  private String n3IwfId = null;

  @JsonProperty("gNbId")
  private GNbId gNbId = null;

  @JsonProperty("ngeNbId")
  private String ngeNbId = null;

  @JsonProperty("wagfId")
  private String wagfId = null;

  @JsonProperty("tngfId")
  private String tngfId = null;

  @JsonProperty("nid")
  private String nid = null;

  @JsonProperty("eNbId")
  private String eNbId = null;

  public GlobalRanNodeId plmnId(PlmnId plmnId) {
    this.plmnId = plmnId;
    return this;
  }

  /**
   * Get plmnId
   * @return plmnId
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public PlmnId getPlmnId() {
    return plmnId;
  }

  public void setPlmnId(PlmnId plmnId) {
    this.plmnId = plmnId;
  }

  public GlobalRanNodeId n3IwfId(String n3IwfId) {
    this.n3IwfId = n3IwfId;
    return this;
  }

  /**
   * This represents the identifier of the N3IWF ID as specified in clause 9.3.1.57 of  3GPP TS 38.413 in hexadecimal representation. Each character in the string shall take a value  of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The most significant  character representing the 4 most significant bits of the N3IWF ID shall appear first in the  string, and the character representing the 4 least significant bit of the N3IWF ID shall  appear last in the string.  
   * @return n3IwfId
   **/
  @Schema(description = "This represents the identifier of the N3IWF ID as specified in clause 9.3.1.57 of  3GPP TS 38.413 in hexadecimal representation. Each character in the string shall take a value  of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The most significant  character representing the 4 most significant bits of the N3IWF ID shall appear first in the  string, and the character representing the 4 least significant bit of the N3IWF ID shall  appear last in the string.  ")
  
  @Pattern(regexp="^[A-Fa-f0-9]+$")   public String getN3IwfId() {
    return n3IwfId;
  }

  public void setN3IwfId(String n3IwfId) {
    this.n3IwfId = n3IwfId;
  }

  public GlobalRanNodeId gNbId(GNbId gNbId) {
    this.gNbId = gNbId;
    return this;
  }

  /**
   * Get gNbId
   * @return gNbId
   **/
  @Schema(description = "")
  
    @Valid
    public GNbId getGNbId() {
    return gNbId;
  }

  public void setGNbId(GNbId gNbId) {
    this.gNbId = gNbId;
  }

  public GlobalRanNodeId ngeNbId(String ngeNbId) {
    this.ngeNbId = ngeNbId;
    return this;
  }

  /**
   * This represents the identifier of the ng-eNB ID as specified in clause 9.3.1.8 of  3GPP TS 38.413. The value of the ng-eNB ID shall be encoded in hexadecimal representation.  Each character in the string shall take a value of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and  shall represent 4 bits. The padding 0 shall be added to make multiple nibbles, so the most  significant character representing the padding 0 if required together with the 4 most  significant bits of the ng-eNB ID shall appear first in the string, and the character  representing the 4 least significant bit of the ng-eNB ID (to form a nibble) shall appear last  in the string.  
   * @return ngeNbId
   **/
  @Schema(example = "SMacroNGeNB-34B89", description = "This represents the identifier of the ng-eNB ID as specified in clause 9.3.1.8 of  3GPP TS 38.413. The value of the ng-eNB ID shall be encoded in hexadecimal representation.  Each character in the string shall take a value of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and  shall represent 4 bits. The padding 0 shall be added to make multiple nibbles, so the most  significant character representing the padding 0 if required together with the 4 most  significant bits of the ng-eNB ID shall appear first in the string, and the character  representing the 4 least significant bit of the ng-eNB ID (to form a nibble) shall appear last  in the string.  ")
  
  @Pattern(regexp="^(MacroNGeNB-[A-Fa-f0-9]{5}|LMacroNGeNB-[A-Fa-f0-9]{6}|SMacroNGeNB-[A-Fa-f0-9]{5})$")   public String getNgeNbId() {
    return ngeNbId;
  }

  public void setNgeNbId(String ngeNbId) {
    this.ngeNbId = ngeNbId;
  }

  public GlobalRanNodeId wagfId(String wagfId) {
    this.wagfId = wagfId;
    return this;
  }

  /**
   * This represents the identifier of the W-AGF ID as specified in clause 9.3.1.162 of  3GPP TS 38.413 in hexadecimal representation. Each character in the string shall take a value  of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The most significant  character representing the 4 most significant bits of the W-AGF ID shall appear first in the  string, and the character representing the 4 least significant bit of the W-AGF ID shall  appear last in the string.  
   * @return wagfId
   **/
  @Schema(description = "This represents the identifier of the W-AGF ID as specified in clause 9.3.1.162 of  3GPP TS 38.413 in hexadecimal representation. Each character in the string shall take a value  of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The most significant  character representing the 4 most significant bits of the W-AGF ID shall appear first in the  string, and the character representing the 4 least significant bit of the W-AGF ID shall  appear last in the string.  ")
  
  @Pattern(regexp="^[A-Fa-f0-9]+$")   public String getWagfId() {
    return wagfId;
  }

  public void setWagfId(String wagfId) {
    this.wagfId = wagfId;
  }

  public GlobalRanNodeId tngfId(String tngfId) {
    this.tngfId = tngfId;
    return this;
  }

  /**
   * This represents the identifier of the TNGF ID as specified in clause 9.3.1.161 of  3GPP TS 38.413  in hexadecimal representation. Each character in the string shall take a value of \"0\" to \"9\", \"a\"  to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The most significant character representing the  4 most significant bits of the TNGF ID shall appear first in the string, and the character  representing the 4 least significant bit of the TNGF ID shall appear last in the string.  
   * @return tngfId
   **/
  @Schema(description = "This represents the identifier of the TNGF ID as specified in clause 9.3.1.161 of  3GPP TS 38.413  in hexadecimal representation. Each character in the string shall take a value of \"0\" to \"9\", \"a\"  to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The most significant character representing the  4 most significant bits of the TNGF ID shall appear first in the string, and the character  representing the 4 least significant bit of the TNGF ID shall appear last in the string.  ")
  
  @Pattern(regexp="^[A-Fa-f0-9]+$")   public String getTngfId() {
    return tngfId;
  }

  public void setTngfId(String tngfId) {
    this.tngfId = tngfId;
  }

  public GlobalRanNodeId nid(String nid) {
    this.nid = nid;
    return this;
  }

  /**
   * This represents the Network Identifier, which together with a PLMN ID is used to identify an SNPN (see 3GPP TS 23.003 and 3GPP TS 23.501 clause 5.30.2.1).  
   * @return nid
   **/
  @Schema(description = "This represents the Network Identifier, which together with a PLMN ID is used to identify an SNPN (see 3GPP TS 23.003 and 3GPP TS 23.501 clause 5.30.2.1).  ")
  
  @Pattern(regexp="^[A-Fa-f0-9]{11}$")   public String getNid() {
    return nid;
  }

  public void setNid(String nid) {
    this.nid = nid;
  }

  public GlobalRanNodeId eNbId(String eNbId) {
    this.eNbId = eNbId;
    return this;
  }

  /**
   * This represents the identifier of the eNB ID as specified in clause 9.2.1.37 of  3GPP TS 36.413. The string shall be formatted with the following pattern  '^('MacroeNB-[A-Fa-f0-9]{5}|LMacroeNB-[A-Fa-f0-9]{6}|SMacroeNB-[A-Fa-f0-9]{5} |HomeeNB-[A-Fa-f0-9]{7})$'. The value of the eNB ID shall be encoded in hexadecimal representation. Each character in the  string shall take a value of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits.  The padding 0 shall be added to make multiple nibbles, so the most significant character  representing the padding 0 if required together with the 4 most significant bits of the eNB ID  shall appear first in the string, and the character representing the 4 least significant bit  of the eNB ID (to form a nibble) shall appear last in the string. 
   * @return eNbId
   **/
  @Schema(description = "This represents the identifier of the eNB ID as specified in clause 9.2.1.37 of  3GPP TS 36.413. The string shall be formatted with the following pattern  '^('MacroeNB-[A-Fa-f0-9]{5}|LMacroeNB-[A-Fa-f0-9]{6}|SMacroeNB-[A-Fa-f0-9]{5} |HomeeNB-[A-Fa-f0-9]{7})$'. The value of the eNB ID shall be encoded in hexadecimal representation. Each character in the  string shall take a value of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits.  The padding 0 shall be added to make multiple nibbles, so the most significant character  representing the padding 0 if required together with the 4 most significant bits of the eNB ID  shall appear first in the string, and the character representing the 4 least significant bit  of the eNB ID (to form a nibble) shall appear last in the string. ")
  
  @Pattern(regexp="^(MacroeNB-[A-Fa-f0-9]{5}|LMacroeNB-[A-Fa-f0-9]{6}|SMacroeNB-[A-Fa-f0-9]{5}|HomeeNB-[A-Fa-f0-9]{7})$")   public String getENbId() {
    return eNbId;
  }

  public void setENbId(String eNbId) {
    this.eNbId = eNbId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GlobalRanNodeId globalRanNodeId = (GlobalRanNodeId) o;
    return Objects.equals(this.plmnId, globalRanNodeId.plmnId) &&
        Objects.equals(this.n3IwfId, globalRanNodeId.n3IwfId) &&
        Objects.equals(this.gNbId, globalRanNodeId.gNbId) &&
        Objects.equals(this.ngeNbId, globalRanNodeId.ngeNbId) &&
        Objects.equals(this.wagfId, globalRanNodeId.wagfId) &&
        Objects.equals(this.tngfId, globalRanNodeId.tngfId) &&
        Objects.equals(this.nid, globalRanNodeId.nid) &&
        Objects.equals(this.eNbId, globalRanNodeId.eNbId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(plmnId, n3IwfId, gNbId, ngeNbId, wagfId, tngfId, nid, eNbId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GlobalRanNodeId {\n");
    
    sb.append("    plmnId: ").append(toIndentedString(plmnId)).append("\n");
    sb.append("    n3IwfId: ").append(toIndentedString(n3IwfId)).append("\n");
    sb.append("    gNbId: ").append(toIndentedString(gNbId)).append("\n");
    sb.append("    ngeNbId: ").append(toIndentedString(ngeNbId)).append("\n");
    sb.append("    wagfId: ").append(toIndentedString(wagfId)).append("\n");
    sb.append("    tngfId: ").append(toIndentedString(tngfId)).append("\n");
    sb.append("    nid: ").append(toIndentedString(nid)).append("\n");
    sb.append("    eNbId: ").append(toIndentedString(eNbId)).append("\n");
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
