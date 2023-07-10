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
 * Represents other DN performance analytics requirements.
 */
@Schema(description = "Represents other DN performance analytics requirements.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class DnPerformanceReq   {
  @JsonProperty("dnPerfOrderCriter")
  private DnPerfOrderingCriterion dnPerfOrderCriter = null;

  @JsonProperty("order")
  private MatchingDirection order = null;

  @JsonProperty("reportThresholds")
  @Valid
  private List<ThresholdLevel> reportThresholds = null;

  public DnPerformanceReq dnPerfOrderCriter(DnPerfOrderingCriterion dnPerfOrderCriter) {
    this.dnPerfOrderCriter = dnPerfOrderCriter;
    return this;
  }

  /**
   * Get dnPerfOrderCriter
   * @return dnPerfOrderCriter
   **/
  @Schema(description = "")
  
    @Valid
    public DnPerfOrderingCriterion getDnPerfOrderCriter() {
    return dnPerfOrderCriter;
  }

  public void setDnPerfOrderCriter(DnPerfOrderingCriterion dnPerfOrderCriter) {
    this.dnPerfOrderCriter = dnPerfOrderCriter;
  }

  public DnPerformanceReq order(MatchingDirection order) {
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

  public DnPerformanceReq reportThresholds(List<ThresholdLevel> reportThresholds) {
    this.reportThresholds = reportThresholds;
    return this;
  }

  public DnPerformanceReq addReportThresholdsItem(ThresholdLevel reportThresholdsItem) {
    if (this.reportThresholds == null) {
      this.reportThresholds = new ArrayList<ThresholdLevel>();
    }
    this.reportThresholds.add(reportThresholdsItem);
    return this;
  }

  /**
   * Get reportThresholds
   * @return reportThresholds
   **/
  @Schema(description = "")
      @Valid
  @Size(min=1)   public List<ThresholdLevel> getReportThresholds() {
    return reportThresholds;
  }

  public void setReportThresholds(List<ThresholdLevel> reportThresholds) {
    this.reportThresholds = reportThresholds;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DnPerformanceReq dnPerformanceReq = (DnPerformanceReq) o;
    return Objects.equals(this.dnPerfOrderCriter, dnPerformanceReq.dnPerfOrderCriter) &&
        Objects.equals(this.order, dnPerformanceReq.order) &&
        Objects.equals(this.reportThresholds, dnPerformanceReq.reportThresholds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dnPerfOrderCriter, order, reportThresholds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DnPerformanceReq {\n");
    
    sb.append("    dnPerfOrderCriter: ").append(toIndentedString(dnPerfOrderCriter)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
    sb.append("    reportThresholds: ").append(toIndentedString(reportThresholds)).append("\n");
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
