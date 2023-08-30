package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="nf_load_metrics")
@Getter
@Setter
public class NfLoadLevelInformationTable implements Serializable{
	
	@EmbeddedId
	private NfLoadLevelId Id;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name="data",columnDefinition = "jsonb")
	@JsonProperty("data")
	private Map<String, Object> data;
	
	@Column(name="nfSetId")
	private String nfSetId;
	
	@Column(name="nfCpuUsage")
	private Integer nfCpuUsage;
	
	@Column(name="nfMemoryUsage")
	private Integer nfMemoryUsage;
	
	@Column(name="nfStorageUsage")
	private Integer nfStorageUsage;
	
	@Column(name="nfLoadLevelAverage")
	private Integer nfLoadLevelAverage;
	
	@Column(name="nfLoadLevelpeak")
	private Integer nfLoadLevelpeak;
	
	@Column(name="nfLoadAvgInAoi")
	private Integer nfLoadAvgInAoi;

	@JdbcTypeCode(SqlTypes.UUID)
	@Column(name="areaOfInterestId",columnDefinition="UUID")
	private UUID areaOfInterestId;

	public UUID getAreaOfInterestId(){
		return this.areaOfInterestId;
	}

	public void setAreaOfInterestId(UUID aoiId){
		this.areaOfInterestId = aoiId;
	}
	
	public OffsetDateTime getTime() {
		return Id.time;
	}
	
	public void setTime(OffsetDateTime time) {
		if(this.Id==null) {
			this.Id = new NfLoadLevelId(time,null);
		}
		else {
			this.Id.time = time;
		}
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public UUID getNfInstanceId() {
		return Id.nfInstanceId;
	}
	public void setNfInstanceId(UUID nfInstanceId) {
		this.Id.nfInstanceId = nfInstanceId;
	}
	public String getNfSetId() {
		return nfSetId;
	}
	public void setNfSetId(String nfSetId) {
		this.nfSetId = nfSetId;
	}

	@Embeddable
	static class NfLoadLevelId implements Serializable {
		
		@JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
		@Column(name="time",columnDefinition= "TIMESTAMP WITH TIME ZONE")
		private OffsetDateTime time;
		
		@JdbcTypeCode(SqlTypes.UUID)
		@Column(name="nfInstanceId",columnDefinition="UUID")
		private UUID nfInstanceId;

		public NfLoadLevelId() {
			
		}
		
	    public NfLoadLevelId(OffsetDateTime time, UUID nfInstanceId) {
	        this.setTime(time);
	        this.setNfInstanceId(nfInstanceId);
	    }

		public OffsetDateTime getTime() {
			return time;
		}

		public void setTime(OffsetDateTime time) {
			this.time = time;
		}

		public UUID getNfInstanceId() {
			return nfInstanceId;
		}

		public void setNfInstanceId(UUID nfInstanceId) {
			this.nfInstanceId = nfInstanceId;
		}

		
	    
	}
}
