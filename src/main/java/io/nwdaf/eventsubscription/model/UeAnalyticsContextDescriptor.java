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
 * Contains information about available UE related analytics contexts.
 */
@Schema(description = "Contains information about available UE related analytics contexts.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class UeAnalyticsContextDescriptor   {
  @JsonProperty("supi")
  private String supi = null;

  @JsonProperty("anaTypes")
  @Valid
  private List<NwdafEvent> anaTypes = new ArrayList<NwdafEvent>();

  public UeAnalyticsContextDescriptor supi(String supi) {
    this.supi = supi;
    return this;
  }

  /**
   * String identifying a Supi that shall contain either an IMSI, a network specific identifier, a Global Cable Identifier (GCI) or a Global Line Identifier (GLI) as specified in clause  2.2A of 3GPP TS 23.003. It shall be formatted as follows  - for an IMSI \"imsi-<imsi>\", where <imsi> shall be formatted according to clause 2.2    of 3GPP TS 23.003 that describes an IMSI.  - for a network specific identifier \"nai-<nai>, where <nai> shall be formatted    according to clause 28.7.2 of 3GPP TS 23.003 that describes an NAI.  - for a GCI \"gci-<gci>\", where <gci> shall be formatted according to clause 28.15.2    of 3GPP TS 23.003.  - for a GLI \"gli-<gli>\", where <gli> shall be formatted according to clause 28.16.2 of    3GPP TS 23.003.To enable that the value is used as part of an URI, the string shall    only contain characters allowed according to the \"lower-with-hyphen\" naming convention    defined in 3GPP TS 29.501. 
   * @return supi
   **/
  @Schema(required = true, description = "String identifying a Supi that shall contain either an IMSI, a network specific identifier, a Global Cable Identifier (GCI) or a Global Line Identifier (GLI) as specified in clause  2.2A of 3GPP TS 23.003. It shall be formatted as follows  - for an IMSI \"imsi-<imsi>\", where <imsi> shall be formatted according to clause 2.2    of 3GPP TS 23.003 that describes an IMSI.  - for a network specific identifier \"nai-<nai>, where <nai> shall be formatted    according to clause 28.7.2 of 3GPP TS 23.003 that describes an NAI.  - for a GCI \"gci-<gci>\", where <gci> shall be formatted according to clause 28.15.2    of 3GPP TS 23.003.  - for a GLI \"gli-<gli>\", where <gli> shall be formatted according to clause 28.16.2 of    3GPP TS 23.003.To enable that the value is used as part of an URI, the string shall    only contain characters allowed according to the \"lower-with-hyphen\" naming convention    defined in 3GPP TS 29.501. ")
      @NotNull

  @Pattern(regexp="^(imsi-[0-9]{5,15}|nai-.+|gci-.+|gli-.+|.+)$")   public String getSupi() {
    return supi;
  }

  public void setSupi(String supi) {
    this.supi = supi;
  }

  public UeAnalyticsContextDescriptor anaTypes(List<NwdafEvent> anaTypes) {
    this.anaTypes = anaTypes;
    return this;
  }

  public UeAnalyticsContextDescriptor addAnaTypesItem(NwdafEvent anaTypesItem) {
    this.anaTypes.add(anaTypesItem);
    return this;
  }

  /**
   * List of analytics types for which UE related analytics contexts can be retrieved. 
   * @return anaTypes
   **/
  @Schema(required = true, description = "List of analytics types for which UE related analytics contexts can be retrieved. ")
      @NotNull
    @Valid
  @Size(min=1)   public List<NwdafEvent> getAnaTypes() {
    return anaTypes;
  }

  public void setAnaTypes(List<NwdafEvent> anaTypes) {
    this.anaTypes = anaTypes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UeAnalyticsContextDescriptor ueAnalyticsContextDescriptor = (UeAnalyticsContextDescriptor) o;
    return Objects.equals(this.supi, ueAnalyticsContextDescriptor.supi) &&
        Objects.equals(this.anaTypes, ueAnalyticsContextDescriptor.anaTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(supi, anaTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UeAnalyticsContextDescriptor {\n");
    
    sb.append("    supi: ").append(toIndentedString(supi)).append("\n");
    sb.append("    anaTypes: ").append(toIndentedString(anaTypes)).append("\n");
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
