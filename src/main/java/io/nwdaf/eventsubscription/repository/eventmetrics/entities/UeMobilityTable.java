package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.time.OffsetDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.nwdaf.eventsubscription.model.UeMobility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ue_mobility_metrics")
@Getter
@Setter
@NoArgsConstructor
public class UeMobilityTable{
	
	@Id
	@JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
	@Column(name="time",columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private OffsetDateTime time;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name="data",columnDefinition = "jsonb")
	@JsonProperty("data")
	private Map<String, Object> data;
	
	public UeMobilityTable(UeMobility data) {
		this.data = data.toMap();
		this.time = data.getTs();
	}
}
