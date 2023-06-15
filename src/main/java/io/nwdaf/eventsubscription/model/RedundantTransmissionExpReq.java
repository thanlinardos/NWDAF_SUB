package io.nwdaf.eventsubscription.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.nwdaf.eventsubscription.model.MatchingDirection;
import io.nwdaf.eventsubscription.model.RedTransExpOrderingCriterion;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Represents other redundant transmission experience analytics requirements.
 */
@Schema(description = "Represents other redundant transmission experience analytics requirements.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class RedundantTransmissionExpReq   {
  @JsonProperty("redTOrderCriter")
  private RedTransExpOrderingCriterion redTOrderCriter = null;

  @JsonProperty("order")
  private MatchingDirection order = null;

  public RedundantTransmissionExpReq redTOrderCriter(RedTransExpOrderingCriterion redTOrderCriter) {
    this.redTOrderCriter = redTOrderCriter;
    return this;
  }

  /**
   * Get redTOrderCriter
   * @return redTOrderCriter
   **/
  @Schema(description = "")
  
    @Valid
    public RedTransExpOrderingCriterion getRedTOrderCriter() {
    return redTOrderCriter;
  }

  public void setRedTOrderCriter(RedTransExpOrderingCriterion redTOrderCriter) {
    this.redTOrderCriter = redTOrderCriter;
  }

  public RedundantTransmissionExpReq order(MatchingDirection order) {
    this.order = order;
    return this;
  }

  /**
   * Get order
   * @return order
   **/
  @Schema(description = "")
  
    @Valid
    public MatchingDirection getOrder() {
    return order;
  }

  public void setOrder(MatchingDirection order) {
    this.order = order;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RedundantTransmissionExpReq redundantTransmissionExpReq = (RedundantTransmissionExpReq) o;
    return Objects.equals(this.redTOrderCriter, redundantTransmissionExpReq.redTOrderCriter) &&
        Objects.equals(this.order, redundantTransmissionExpReq.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(redTOrderCriter, order);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RedundantTransmissionExpReq {\n");
    
    sb.append("    redTOrderCriter: ").append(toIndentedString(redTOrderCriter)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
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
