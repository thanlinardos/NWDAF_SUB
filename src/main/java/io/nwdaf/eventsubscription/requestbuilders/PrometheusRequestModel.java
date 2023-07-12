package io.nwdaf.eventsubscription.requestbuilders;

import java.time.OffsetDateTime;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a prometheus request query resource.")
@Validated
public class PrometheusRequestModel {
	
	@JsonProperty("query")
	private String query = null;
	
	@JsonProperty("time")
	private OffsetDateTime time = null;
	
	@Schema(description = "")
	  
	@Valid	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	@Schema(description = "")
	  
	@Valid
	public OffsetDateTime getTime() {
		return time;
	}
	
	public void setTime(OffsetDateTime time) {
		this.time = time;
	}
	
	  @Override
	  public boolean equals(java.lang.Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    PrometheusRequestModel req = (PrometheusRequestModel) o;
	    return Objects.equals(this.query, req.query) &&
	        Objects.equals(this.time, req.time);
	  }

}
