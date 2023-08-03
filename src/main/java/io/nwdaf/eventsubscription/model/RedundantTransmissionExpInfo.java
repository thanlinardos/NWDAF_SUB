package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * The redundant transmission experience related information. When subscribed event is  \&quot;RED_TRANS_EXP\&quot;, the \&quot;redTransInfos\&quot; attribute shall be included. 
 */
@Schema(description = "The redundant transmission experience related information. When subscribed event is  \"RED_TRANS_EXP\", the \"redTransInfos\" attribute shall be included. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class RedundantTransmissionExpInfo   {
  @JsonProperty("spatialValidCon")
  private NetworkAreaInfo spatialValidCon = null;

  @JsonProperty("dnn")
  private String dnn = null;

  @JsonProperty("redTransExps")
  @Valid
  private List<RedundantTransmissionExpPerTS> redTransExps = new ArrayList<RedundantTransmissionExpPerTS>();

  public RedundantTransmissionExpInfo spatialValidCon(NetworkAreaInfo spatialValidCon) {
    this.spatialValidCon = spatialValidCon;
    return this;
  }

  /**
   * Get spatialValidCon
   * @return spatialValidCon
   **/
  @Schema(description = "")
  
    @Valid
    public NetworkAreaInfo getSpatialValidCon() {
    return spatialValidCon;
  }

  public void setSpatialValidCon(NetworkAreaInfo spatialValidCon) {
    this.spatialValidCon = spatialValidCon;
  }

  public RedundantTransmissionExpInfo dnn(String dnn) {
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

  public RedundantTransmissionExpInfo redTransExps(List<RedundantTransmissionExpPerTS> redTransExps) {
    this.redTransExps = redTransExps;
    return this;
  }

  public RedundantTransmissionExpInfo addRedTransExpsItem(RedundantTransmissionExpPerTS redTransExpsItem) {
    this.redTransExps.add(redTransExpsItem);
    return this;
  }

  /**
   * Get redTransExps
   * @return redTransExps
   **/
  @Schema(required = true, description = "")
      @NotNull
    @Valid
  @Size(min=1)   public List<RedundantTransmissionExpPerTS> getRedTransExps() {
    return redTransExps;
  }

  public void setRedTransExps(List<RedundantTransmissionExpPerTS> redTransExps) {
    this.redTransExps = redTransExps;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RedundantTransmissionExpInfo redundantTransmissionExpInfo = (RedundantTransmissionExpInfo) o;
    return Objects.equals(this.spatialValidCon, redundantTransmissionExpInfo.spatialValidCon) &&
        Objects.equals(this.dnn, redundantTransmissionExpInfo.dnn) &&
        Objects.equals(this.redTransExps, redundantTransmissionExpInfo.redTransExps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialValidCon, dnn, redTransExps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RedundantTransmissionExpInfo {\n");
    
    sb.append("    spatialValidCon: ").append(toIndentedString(spatialValidCon)).append("\n");
    sb.append("    dnn: ").append(toIndentedString(dnn)).append("\n");
    sb.append("    redTransExps: ").append(toIndentedString(redTransExps)).append("\n");
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
