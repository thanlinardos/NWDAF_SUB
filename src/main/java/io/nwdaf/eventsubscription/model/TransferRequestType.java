package io.nwdaf.eventsubscription.model;


import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - PREPARE: Indicates that the request is for analytics subscription transfer preparation. - TRANSFER: Indicates that the request is for analytics subscription transfer execution. 
*/
@Schema(description = "Indicates whether the request is either for analytics subscription transfer preperation or execution.")
@Validated
public class TransferRequestType {
  public enum TransferRequestTypeEnum {
    PREPARE("PREPARE"),
    TRANSFER("TRANSFER");
    private String value;

    TransferRequestTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TransferRequestTypeEnum fromValue(String text) {
      for (TransferRequestTypeEnum b : TransferRequestTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("transferRequestType")
  private TransferRequestTypeEnum transferRequestType = null;

  public TransferRequestType transferRequestType(TransferRequestTypeEnum transferRequestType){
    this.transferRequestType = transferRequestType;
    return this;
  }

  /** Get transferRequestType
  * @return transferRequestType
  **/
  @Schema(description="transferRequestType")

  public TransferRequestTypeEnum getTransferRequestType(){
    return transferRequestType;
  }

  public void setTransferRequestType(TransferRequestTypeEnum transferRequestType){
    this.transferRequestType = transferRequestType;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferRequestType transferRequestTypeObject = (TransferRequestType) o;
    return Objects.equals(this.transferRequestType, transferRequestTypeObject.transferRequestType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transferRequestType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferRequestType {\n");
    
    sb.append("    transferRequestType: ").append(toIndentedString(transferRequestType)).append("\n");
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
