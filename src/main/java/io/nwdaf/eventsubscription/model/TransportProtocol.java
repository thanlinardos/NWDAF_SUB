package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - UDP: User Datagram Protocol. - TCP: Transmission Control Protocol.  
*/
@Schema(description = "Represents the transport protocol used.")
@Validated
public class TransportProtocol {
  public enum TransportProtocolEnum {
    UDP("UDP"),
    TCP("TCP");
    private String value;

    TransportProtocolEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TransportProtocolEnum fromValue(String text) {
      for (TransportProtocolEnum b : TransportProtocolEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("transportProtocol")
  private TransportProtocolEnum transportProtocol = null;

  public TransportProtocol transportProtocol(TransportProtocolEnum transportProtocol){
    this.transportProtocol = transportProtocol;
    return this;
  }

  /** Get transportProtocol
  * @return transportProtocol
  **/
  @Schema(description="transportProtocol")

  public TransportProtocolEnum getTransportProtocol(){
    return transportProtocol;
  }

  public void setTransportProtocol(TransportProtocolEnum transportProtocol){
    this.transportProtocol = transportProtocol;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransportProtocol transportProtocolObject = (TransportProtocol) o;
    return Objects.equals(this.transportProtocol, transportProtocolObject.transportProtocol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transportProtocol);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransportProtocol {\n");
    
    sb.append("    transportProtocol: ").append(toIndentedString(transportProtocol)).append("\n");
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
