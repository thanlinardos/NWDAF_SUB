package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - ACTIVATE: The event notification is activated. - DEACTIVATE: The event notification is deactivated and shall be muted. The available    event(s) shall be stored. - RETRIEVAL: The event notification shall be sent to the NF service consumer(s),   after that, is muted again.  
*/
@Schema(description = "The event notification flag.")
@Validated
public class NotificationFlag {
  public enum NotificationFlagEnum {
    ACTIVATE("ACTIVATE"),
    DEACTIVATE("DEACTIVATE"),
    RETRIEVAL("RETRIEVAL");
    private String value;

    NotificationFlagEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NotificationFlagEnum fromValue(String text) {
      for (NotificationFlagEnum b : NotificationFlagEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("notifFlag")
  private NotificationFlagEnum notifFlag = null;

  public NotificationFlag notifFlag(NotificationFlagEnum notifFlag){
    this.notifFlag = notifFlag;
    return this;
  }

  /** Get notifFlag
  * @return notifFlag
  **/
  @Schema(description="notifFlag")

  public NotificationFlagEnum getNotifFlag(){
    return notifFlag;
  }

  public void setNotifFlag(NotificationFlagEnum notifFlag){
    this.notifFlag = notifFlag;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationFlag notificationFlagObject = (NotificationFlag) o;
    return Objects.equals(this.notifFlag, notificationFlagObject.notifFlag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notifFlag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationFlag {\n");
    
    sb.append("    notifFlag: ").append(toIndentedString(notifFlag)).append("\n");
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
