package io.nwdaf.eventsubscription.responsebuilders;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a prometheus response resource.")
@Validated
public class PrometheusResponseModel {
	@JsonProperty("status")
	private String status = null;
	@JsonProperty("data")
	private PrometheusDataModel data = null;
	public PrometheusDataModel getData() {
		return data;
	}
	public void setData(PrometheusDataModel data) {
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
