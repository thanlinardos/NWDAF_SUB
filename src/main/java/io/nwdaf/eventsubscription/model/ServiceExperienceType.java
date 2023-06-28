package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - VOICE: Indicates that the service experience analytics is for voice service. - VIDEO: Indicates that the service experience analytics is for video service. - OTHER: Indicates that the service experience analytics is for other service. 
*/
@Schema(description = "Represents the type of Service Experience Analytics.")
@Validated
public class ServiceExperienceType {
  public enum ServiceExperienceTypeEnum {
    VOICE("VOICE"),
    VIDEO("VIDEO"),
    OTHER("OTHER");
    private String value;

    ServiceExperienceTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ServiceExperienceTypeEnum fromValue(String text) {
      for (ServiceExperienceTypeEnum b : ServiceExperienceTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("srvExpcType")
  private ServiceExperienceTypeEnum srvExpcType = null;

  public ServiceExperienceType srvExpcType(ServiceExperienceTypeEnum srvExpcType){
    this.srvExpcType = srvExpcType;
    return this;
  }

  /** Get srvExpcType
  * @return srvExpcType
  **/
  @Schema(description="srvExpcType")

  public ServiceExperienceTypeEnum getSrvExpcType(){
    return srvExpcType;
  }

  public void setSrvExpcType(ServiceExperienceTypeEnum srvExpcType){
    this.srvExpcType = srvExpcType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceExperienceType serviceExperienceTypeObject = (ServiceExperienceType) o;
    return Objects.equals(this.srvExpcType, serviceExperienceTypeObject.srvExpcType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(srvExpcType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceExperienceType {\n");
    
    sb.append("    srvExpcType: ").append(toIndentedString(srvExpcType)).append("\n");
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
