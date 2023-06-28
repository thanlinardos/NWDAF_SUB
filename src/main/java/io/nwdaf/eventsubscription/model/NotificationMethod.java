package io.nwdaf.eventsubscription.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
/**
* Possible values are: - PERIODIC: The subscribe of NWDAF Event is periodically. The periodic of the notification is identified by repetitionPeriod defined in clause 5.1.6.2.3.   - THRESHOLD: The subscribe of NWDAF Event is upon threshold exceeded. 
*/
@Schema(description = "Represents the notification methods that can be subscribed.")
@Validated
public class NotificationMethod {
  public enum NotificationMethodEnum {
    PERIODIC("PERIODIC"),
    THRESHOLD("THRESHOLD");
    private String value;

    NotificationMethodEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NotificationMethodEnum fromValue(String text) {
      for (NotificationMethodEnum b : NotificationMethodEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("notifMethod")
  private NotificationMethodEnum notifMethod = null;

  public NotificationMethod notifMethod(NotificationMethodEnum notifMethod){
    this.notifMethod = notifMethod;
    return this;
  }

  /** Get notifMethod
  * @return notifMethod
  **/
  @Schema(description="notifMethod")

  public NotificationMethodEnum getNotifMethod(){
    return notifMethod;
  }

  public void setNotifMethod(NotificationMethodEnum notifMethod){
    this.notifMethod = notifMethod;
  }

   @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationMethod notificationMethodObject = (NotificationMethod) o;
    return Objects.equals(this.notifMethod, notificationMethodObject.notifMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notifMethod);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationMethod {\n");
    
    sb.append("    notifMethod: ").append(toIndentedString(notifMethod)).append("\n");
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
