package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contains information about an ML models.
 */
@Schema(description = "Contains information about an ML models.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class MLModelInfo  implements OneOfMLModelInfo {
  @JsonProperty("mlFileAddrs")
  @Valid
  private List<MLModelAddr> mlFileAddrs = null;

  @JsonProperty("modelProvId")
  private UUID modelProvId = null;

  @JsonProperty("modelProvSetId")
  private String modelProvSetId = null;

  public MLModelInfo mlFileAddrs(List<MLModelAddr> mlFileAddrs) {
    this.mlFileAddrs = mlFileAddrs;
    return this;
  }

  public MLModelInfo addMlFileAddrsItem(MLModelAddr mlFileAddrsItem) {
    if (this.mlFileAddrs == null) {
      this.mlFileAddrs = new ArrayList<MLModelAddr>();
    }
    this.mlFileAddrs.add(mlFileAddrsItem);
    return this;
  }

  /**
   * Get mlFileAddrs
   * @return mlFileAddrs
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<MLModelAddr> getMlFileAddrs() {
    return mlFileAddrs;
  }

  public void setMlFileAddrs(List<MLModelAddr> mlFileAddrs) {
    this.mlFileAddrs = mlFileAddrs;
  }

  public MLModelInfo modelProvId(UUID modelProvId) {
    this.modelProvId = modelProvId;
    return this;
  }

  /**
   * String uniquely identifying a NF instance. The format of the NF Instance ID shall be a  Universally Unique Identifier (UUID) version 4, as described in IETF RFC 4122.  
   * @return modelProvId
   **/
  @Schema(description = "String uniquely identifying a NF instance. The format of the NF Instance ID shall be a  Universally Unique Identifier (UUID) version 4, as described in IETF RFC 4122.  ")
  
    @Valid
    public UUID getModelProvId() {
    return modelProvId;
  }

  public void setModelProvId(UUID modelProvId) {
    this.modelProvId = modelProvId;
  }

  public MLModelInfo modelProvSetId(String modelProvSetId) {
    this.modelProvSetId = modelProvSetId;
    return this;
  }

  /**
   * NF Set Identifier (see clause 28.12 of 3GPP TS 23.003), formatted as the following string \"set<Set ID>.<nftype>set.5gc.mnc<MNC>.mcc<MCC>\", or  \"set<SetID>.<NFType>set.5gc.nid<NID>.mnc<MNC>.mcc<MCC>\" with  <MCC> encoded as defined in clause 5.4.2 (\"Mcc\" data type definition)  <MNC> encoding the Mobile Network Code part of the PLMN, comprising 3 digits.    If there are only 2 significant digits in the MNC, one \"0\" digit shall be inserted    at the left side to fill the 3 digits coding of MNC.  Pattern: '^[0-9]{3}$' <NFType> encoded as a value defined in Table 6.1.6.3.3-1 of 3GPP TS 29.510 but    with lower case characters <Set ID> encoded as a string of characters consisting of    alphabetic characters (A-Z and a-z), digits (0-9) and/or the hyphen (-) and that    shall end with either an alphabetic character or a digit.  
   * @return modelProvSetId
   **/
  @Schema(description = "NF Set Identifier (see clause 28.12 of 3GPP TS 23.003), formatted as the following string \"set<Set ID>.<nftype>set.5gc.mnc<MNC>.mcc<MCC>\", or  \"set<SetID>.<NFType>set.5gc.nid<NID>.mnc<MNC>.mcc<MCC>\" with  <MCC> encoded as defined in clause 5.4.2 (\"Mcc\" data type definition)  <MNC> encoding the Mobile Network Code part of the PLMN, comprising 3 digits.    If there are only 2 significant digits in the MNC, one \"0\" digit shall be inserted    at the left side to fill the 3 digits coding of MNC.  Pattern: '^[0-9]{3}$' <NFType> encoded as a value defined in Table 6.1.6.3.3-1 of 3GPP TS 29.510 but    with lower case characters <Set ID> encoded as a string of characters consisting of    alphabetic characters (A-Z and a-z), digits (0-9) and/or the hyphen (-) and that    shall end with either an alphabetic character or a digit.  ")
  
    public String getModelProvSetId() {
    return modelProvSetId;
  }

  public void setModelProvSetId(String modelProvSetId) {
    this.modelProvSetId = modelProvSetId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MLModelInfo mlModelInfo = (MLModelInfo) o;
    return Objects.equals(this.mlFileAddrs, mlModelInfo.mlFileAddrs) &&
        Objects.equals(this.modelProvId, mlModelInfo.modelProvId) &&
        Objects.equals(this.modelProvSetId, mlModelInfo.modelProvSetId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mlFileAddrs, modelProvId, modelProvSetId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MLModelInfo {\n");
    
    sb.append("    mlFileAddrs: ").append(toIndentedString(mlFileAddrs)).append("\n");
    sb.append("    modelProvId: ").append(toIndentedString(modelProvId)).append("\n");
    sb.append("    modelProvSetId: ").append(toIndentedString(modelProvSetId)).append("\n");
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
