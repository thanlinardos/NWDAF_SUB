package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents the Session Management congestion control experience information.
 */
@Schema(description = "Represents the Session Management congestion control experience information.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class SmcceInfo   {
  @JsonProperty("dnn")
  private String dnn = null;

  @JsonProperty("snssai")
  private Snssai snssai = null;

  @JsonProperty("smcceUeList")
  private SmcceUeList smcceUeList = null;

  public SmcceInfo dnn(String dnn) {
    this.dnn = dnn;
    return this;
  }

  /**
   * String representing a Data Network as defined in clause 9A of 3GPP TS 23.003;  it shall contain either a DNN Network Identifier, or a full DNN with both the Network  Identifier and Operator Identifier, as specified in 3GPP TS 23.003 clause 9.1.1 and 9.1.2. It shall be coded as string in which the labels are separated by dots  (e.g. \"Label1.Label2.Label3\"). 
   * @return dnn
   **/
  @Schema(description = "String representing a Data Network as defined in clause 9A of 3GPP TS 23.003;  it shall contain either a DNN Network Identifier, or a full DNN with both the Network  Identifier and Operator Identifier, as specified in 3GPP TS 23.003 clause 9.1.1 and 9.1.2. It shall be coded as string in which the labels are separated by dots  (e.g. \"Label1.Label2.Label3\"). ")
  
    public String getDnn() {
    return dnn;
  }

  public void setDnn(String dnn) {
    this.dnn = dnn;
  }

  public SmcceInfo snssai(Snssai snssai) {
    this.snssai = snssai;
    return this;
  }

  /**
   * Get snssai
   * @return snssai
   **/
  @Schema(description = "")
  
    @Valid
    public Snssai getSnssai() {
    return snssai;
  }

  public void setSnssai(Snssai snssai) {
    this.snssai = snssai;
  }

  public SmcceInfo smcceUeList(SmcceUeList smcceUeList) {
    this.smcceUeList = smcceUeList;
    return this;
  }

  /**
   * Get smcceUeList
   * @return smcceUeList
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public SmcceUeList getSmcceUeList() {
    return smcceUeList;
  }

  public void setSmcceUeList(SmcceUeList smcceUeList) {
    this.smcceUeList = smcceUeList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmcceInfo smcceInfo = (SmcceInfo) o;
    return Objects.equals(this.dnn, smcceInfo.dnn) &&
        Objects.equals(this.snssai, smcceInfo.snssai) &&
        Objects.equals(this.smcceUeList, smcceInfo.smcceUeList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dnn, snssai, smcceUeList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmcceInfo {\n");
    
    sb.append("    dnn: ").append(toIndentedString(dnn)).append("\n");
    sb.append("    snssai: ").append(toIndentedString(snssai)).append("\n");
    sb.append("    smcceUeList: ").append(toIndentedString(smcceUeList)).append("\n");
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
