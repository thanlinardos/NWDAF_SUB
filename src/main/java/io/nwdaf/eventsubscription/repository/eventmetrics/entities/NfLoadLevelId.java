package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;

public class NfLoadLevelId implements Serializable {
	
	@JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
	@Column(name="time",columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private OffsetDateTime time;
	
	@JdbcTypeCode(SqlTypes.UUID)
	@Column(name="nfInstanceId",columnDefinition="UUID")
	private UUID nfInstanceId;

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