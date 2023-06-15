package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Addresses of ML model files.
 */
@Schema(description = "Addresses of ML model files.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class MLModelAddr  implements OneOfMLModelAddr {
  @JsonProperty("mLModelUrl")
  private String mLModelUrl = null;

  @JsonProperty("mlFileFqdn")
  private String mlFileFqdn = null;

  public MLModelAddr mLModelUrl(String mLModelUrl) {
    this.mLModelUrl = mLModelUrl;
    return this;
  }

  /**
   * String providing an URI formatted according to RFC 3986.
   * @return mLModelUrl
   **/
  @Schema(description = "String providing an URI formatted according to RFC 3986.")
  
    public String getMLModelUrl() {
    return mLModelUrl;
  }

  public void setMLModelUrl(String mLModelUrl) {
    this.mLModelUrl = mLModelUrl;
  }

  public MLModelAddr mlFileFqdn(String mlFileFqdn) {
    this.mlFileFqdn = mlFileFqdn;
    return this;
  }

  /**
   * The FQDN of the ML Model file.
   * @return mlFileFqdn
   **/
  @Schema(description = "The FQDN of the ML Model file.")
  
    public String getMlFileFqdn() {
    return mlFileFqdn;
  }

  public void setMlFileFqdn(String mlFileFqdn) {
    this.mlFileFqdn = mlFileFqdn;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MLModelAddr mlModelAddr = (MLModelAddr) o;
    return Objects.equals(this.mLModelUrl, mlModelAddr.mLModelUrl) &&
        Objects.equals(this.mlFileFqdn, mlModelAddr.mlFileFqdn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mLModelUrl, mlFileFqdn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MLModelAddr {\n");
    
    sb.append("    mLModelUrl: ").append(toIndentedString(mLModelUrl)).append("\n");
    sb.append("    mlFileFqdn: ").append(toIndentedString(mlFileFqdn)).append("\n");
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
