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
 * Contains load level information applicable for one or several slices.
 */
@Schema(description = "Contains load level information applicable for one or several slices.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class SliceLoadLevelInformation   {
  @JsonProperty("loadLevelInformation")
  private Integer loadLevelInformation = null;

  @JsonProperty("snssais")
  @Valid
  private List<Snssai> snssais = new ArrayList<Snssai>();

  public SliceLoadLevelInformation loadLevelInformation(Integer loadLevelInformation) {
    this.loadLevelInformation = loadLevelInformation;
    return this;
  }

  /**
   * Load level information of the network slice and the optionally associated network slice  instance. 
   * @return loadLevelInformation
   **/
  @Schema(required = true, description = "Load level information of the network slice and the optionally associated network slice  instance. ")
      @NotNull

    public Integer getLoadLevelInformation() {
    return loadLevelInformation;
  }

  public void setLoadLevelInformation(Integer loadLevelInformation) {
    this.loadLevelInformation = loadLevelInformation;
  }

  public SliceLoadLevelInformation snssais(List<Snssai> snssais) {
    this.snssais = snssais;
    return this;
  }

  public SliceLoadLevelInformation addSnssaisItem(Snssai snssaisItem) {
    this.snssais.add(snssaisItem);
    return this;
  }

  /**
   * Identification(s) of network slice to which the subscription applies.
   * @return snssais
   **/
  @Schema(required = true, description = "Identification(s) of network slice to which the subscription applies.")
      @NotNull
    @Valid
  @Size(min=1)   public List<Snssai> getSnssais() {
    return snssais;
  }

  public void setSnssais(List<Snssai> snssais) {
    this.snssais = snssais;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SliceLoadLevelInformation sliceLoadLevelInformation = (SliceLoadLevelInformation) o;
    return Objects.equals(this.loadLevelInformation, sliceLoadLevelInformation.loadLevelInformation) &&
        Objects.equals(this.snssais, sliceLoadLevelInformation.snssais);
  }

  @Override
  public int hashCode() {
    return Objects.hash(loadLevelInformation, snssais);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SliceLoadLevelInformation {\n");
    
    sb.append("    loadLevelInformation: ").append(toIndentedString(loadLevelInformation)).append("\n");
    sb.append("    snssais: ").append(toIndentedString(snssais)).append("\n");
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
