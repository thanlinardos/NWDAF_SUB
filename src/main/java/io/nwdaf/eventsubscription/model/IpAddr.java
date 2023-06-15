package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contains an IP adresse.
 */
@Schema(description = "Contains an IP adresse.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class IpAddr  implements OneOfIpAddr {
  @JsonProperty("ipv4Addr")
  private String ipv4Addr = null;

  @JsonProperty("ipv6Addr")
  private String ipv6Addr = null;

  @JsonProperty("ipv6Prefix")
  private String ipv6Prefix = null;

  public IpAddr ipv4Addr(String ipv4Addr) {
    this.ipv4Addr = ipv4Addr;
    return this;
  }

  /**
   * String identifying a IPv4 address formatted in the 'dotted decimal' notation as defined in RFC 1166. 
   * @return ipv4Addr
   **/
  @Schema(example = "198.51.100.1", description = "String identifying a IPv4 address formatted in the 'dotted decimal' notation as defined in RFC 1166. ")
  
  @Pattern(regexp="^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$")   public String getIpv4Addr() {
    return ipv4Addr;
  }

  public void setIpv4Addr(String ipv4Addr) {
    this.ipv4Addr = ipv4Addr;
  }

  public IpAddr ipv6Addr(String ipv6Addr) {
    this.ipv6Addr = ipv6Addr;
    return this;
  }

  /**
   * Get ipv6Addr
   * @return ipv6Addr
   **/
  @Schema(description = "")
  
    public String getIpv6Addr() {
    return ipv6Addr;
  }

  public void setIpv6Addr(String ipv6Addr) {
    this.ipv6Addr = ipv6Addr;
  }

  public IpAddr ipv6Prefix(String ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
    return this;
  }

  /**
   * Get ipv6Prefix
   * @return ipv6Prefix
   **/
  @Schema(description = "")
  
    public String getIpv6Prefix() {
    return ipv6Prefix;
  }

  public void setIpv6Prefix(String ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IpAddr ipAddr = (IpAddr) o;
    return Objects.equals(this.ipv4Addr, ipAddr.ipv4Addr) &&
        Objects.equals(this.ipv6Addr, ipAddr.ipv6Addr) &&
        Objects.equals(this.ipv6Prefix, ipAddr.ipv6Prefix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ipv4Addr, ipv6Addr, ipv6Prefix);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IpAddr {\n");
    
    sb.append("    ipv4Addr: ").append(toIndentedString(ipv4Addr)).append("\n");
    sb.append("    ipv6Addr: ").append(toIndentedString(ipv6Addr)).append("\n");
    sb.append("    ipv6Prefix: ").append(toIndentedString(ipv6Prefix)).append("\n");
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
