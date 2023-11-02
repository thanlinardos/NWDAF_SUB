package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nwdaf.eventsubscription.model.NfLoadLevelInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="nf_load_metrics")
@Getter
@Setter
@NoArgsConstructor
public class NfLoadLevelInformationTable implements Serializable{
	
	@EmbeddedId
	private NfLoadLevelId Id;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name="data",columnDefinition = "jsonb")
	@JsonProperty("data")
	private Map<String, Object> data = new HashMap<>();
	
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
	
	public NfLoadLevelInformationTable(NfLoadLevelInformation body) {
		this.setData(body.toMap());
		this.setTime(body.getTimeStamp());
		this.setNfInstanceId(body.getNfInstanceId());
		this.setNfSetId(body.getNfSetId());
		this.setAreaOfInterestId(body.getAreaOfInterestId());
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
	
	public UUID getNfInstanceId() {
		return Id.nfInstanceId;
	}

	public void setNfInstanceId(UUID nfInstanceId) {
		this.Id.nfInstanceId = nfInstanceId;
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

		@Override
		public int hashCode() {
			return Objects.hash(time,nfInstanceId);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			NfLoadLevelId nfLoadLevelId = (NfLoadLevelId) o;
			return Objects.equals(this.time,nfLoadLevelId.time) &&
				Objects.equals(this.nfInstanceId, nfLoadLevelId.nfInstanceId);
		}
	}
}
