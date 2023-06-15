package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.Ecgi;
import io.nwdaf.eventsubscription.model.GlobalRanNodeId;
import io.nwdaf.eventsubscription.model.Ncgi;
import io.nwdaf.eventsubscription.model.Tai;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Describes a network area information in which the NF service consumer requests the number of UEs.
 */
@Schema(description = "Describes a network area information in which the NF service consumer requests the number of UEs.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class NetworkAreaInfo1   {
  @JsonProperty("ecgis")
  @Valid
  private List<Ecgi> ecgis = null;

  @JsonProperty("ncgis")
  @Valid
  private List<Ncgi> ncgis = null;

  @JsonProperty("gRanNodeIds")
  @Valid
  private List<GlobalRanNodeId> gRanNodeIds = null;

  @JsonProperty("tais")
  @Valid
  private List<Tai> tais = null;

  public NetworkAreaInfo1 ecgis(List<Ecgi> ecgis) {
    this.ecgis = ecgis;
    return this;
  }

  public NetworkAreaInfo1 addEcgisItem(Ecgi ecgisItem) {
    if (this.ecgis == null) {
      this.ecgis = new ArrayList<Ecgi>();
    }
    this.ecgis.add(ecgisItem);
    return this;
  }

  /**
   * Contains a list of E-UTRA cell identities.
   * @return ecgis
   **/
  @Schema(description = "Contains a list of E-UTRA cell identities.")
      @Valid
  @Size(min=1)   public List<Ecgi> getEcgis() {
    return ecgis;
  }

  public void setEcgis(List<Ecgi> ecgis) {
    this.ecgis = ecgis;
  }

  public NetworkAreaInfo1 ncgis(List<Ncgi> ncgis) {
    this.ncgis = ncgis;
    return this;
  }

  public NetworkAreaInfo1 addNcgisItem(Ncgi ncgisItem) {
    if (this.ncgis == null) {
      this.ncgis = new ArrayList<Ncgi>();
    }
    this.ncgis.add(ncgisItem);
    return this;
  }

  /**
   * Contains a list of NR cell identities.
   * @return ncgis
   **/
  @Schema(description = "Contains a list of NR cell identities.")
      @Valid
  @Size(min=1)   public List<Ncgi> getNcgis() {
    return ncgis;
  }

  public void setNcgis(List<Ncgi> ncgis) {
    this.ncgis = ncgis;
  }

  public NetworkAreaInfo1 gRanNodeIds(List<GlobalRanNodeId> gRanNodeIds) {
    this.gRanNodeIds = gRanNodeIds;
    return this;
  }

  public NetworkAreaInfo1 addGRanNodeIdsItem(GlobalRanNodeId gRanNodeIdsItem) {
    if (this.gRanNodeIds == null) {
      this.gRanNodeIds = new ArrayList<GlobalRanNodeId>();
    }
    this.gRanNodeIds.add(gRanNodeIdsItem);
    return this;
  }

  /**
   * Contains a list of NG RAN nodes.
   * @return gRanNodeIds
   **/
  @Schema(description = "Contains a list of NG RAN nodes.")
      @Valid
  @Size(min=1)   public List<GlobalRanNodeId> getGRanNodeIds() {
    return gRanNodeIds;
  }

  public void setGRanNodeIds(List<GlobalRanNodeId> gRanNodeIds) {
    this.gRanNodeIds = gRanNodeIds;
  }

  public NetworkAreaInfo1 tais(List<Tai> tais) {
    this.tais = tais;
    return this;
  }

  public NetworkAreaInfo1 addTaisItem(Tai taisItem) {
    if (this.tais == null) {
      this.tais = new ArrayList<Tai>();
    }
    this.tais.add(taisItem);
    return this;
  }

  /**
   * Contains a list of tracking area identities.
   * @return tais
   **/
  @Schema(description = "Contains a list of tracking area identities.")
      @Valid
  @Size(min=1)   public List<Tai> getTais() {
    return tais;
  }

  public void setTais(List<Tai> tais) {
    this.tais = tais;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkAreaInfo1 networkAreaInfo1 = (NetworkAreaInfo1) o;
    return Objects.equals(this.ecgis, networkAreaInfo1.ecgis) &&
        Objects.equals(this.ncgis, networkAreaInfo1.ncgis) &&
        Objects.equals(this.gRanNodeIds, networkAreaInfo1.gRanNodeIds) &&
        Objects.equals(this.tais, networkAreaInfo1.tais);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ecgis, ncgis, gRanNodeIds, tais);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NetworkAreaInfo1 {\n");
    
    sb.append("    ecgis: ").append(toIndentedString(ecgis)).append("\n");
    sb.append("    ncgis: ").append(toIndentedString(ncgis)).append("\n");
    sb.append("    gRanNodeIds: ").append(toIndentedString(gRanNodeIds)).append("\n");
    sb.append("    tais: ").append(toIndentedString(tais)).append("\n");
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
