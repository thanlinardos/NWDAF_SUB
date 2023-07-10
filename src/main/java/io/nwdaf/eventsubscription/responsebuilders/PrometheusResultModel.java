package io.nwdaf.eventsubscription.responsebuilders;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrometheusResultModel {
	@JsonProperty("metric")
	private PrometheusMetricModel metric = null;
	
	@JsonProperty("value")
	private List<String> value = new ArrayList<>();
	
	private OffsetDateTime timeStamp = null;
	
	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		if(this.timeStamp==null && value.size()>0) {
			this.timeStamp = OffsetDateTime.parse(value.get(0));
		}
		this.value = value;
	}

	public OffsetDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(OffsetDateTime timeStamp) {
		if(timeStamp==null && value.size()>0) {
			timeStamp = OffsetDateTime.parse(value.get(0));
		}
		this.timeStamp = timeStamp;
	}
	public PrometheusMetricModel getMetric() {
		return metric;
	}
	public void setMetric(PrometheusMetricModel metric) {
		this.metric = metric;
	}
}
