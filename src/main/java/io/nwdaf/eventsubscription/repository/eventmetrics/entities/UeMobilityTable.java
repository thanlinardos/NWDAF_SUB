package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.nwdaf.eventsubscription.model.UeMobility;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static io.nwdaf.eventsubscription.utilities.OtherUtil.fillUeMobilityWithGeographicalInfo;

@Entity
@Table(name="ue_mobility_metrics")
@Getter
@Setter
@NoArgsConstructor
public class UeMobilityTable{
	
	@EmbeddedId
	private UeMobilityId Id;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name="data",columnDefinition = "jsonb")
	@JsonProperty("data")
	private Map<String, Object> data;

	@Column(name="intGroupId")
	private String intGroupId;
	
	public UeMobilityTable(UeMobility data) {
		fillUeMobilityWithGeographicalInfo(data);
		this.data = data.toMap();
		this.setTime(data.getTs());
		this.setSupi(data.getSupi());
		this.intGroupId = data.getIntGroupId();
		// this.areaOfInterestIds = data.getAreaOfInterestIds();
	}

	public OffsetDateTime getTime() {
		return Id.time;
	}

	public void setTime(OffsetDateTime time) {
		if (this.Id == null) {
			this.Id = new UeMobilityId(time, null);
		} else {
			this.Id.time = time;
		}
	}

	public String getSupi() {
		return Id.supi;
	}

	public void setSupi(String supi) {
		this.Id.supi = supi;
	}

	@Embeddable @Getter @Setter
	static class UeMobilityId implements Serializable {

		@JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
		@Column(name = "time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
		private OffsetDateTime time;

		@Column(name="supi")
		private String supi;

		public UeMobilityId() {

		}

		public UeMobilityId(OffsetDateTime time, String supi) {
			this.time = time;
			this.supi = supi;
		}

		@Override
		public int hashCode() {
			return Objects.hash(time, supi);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			UeMobilityId ueMobilityId = (UeMobilityId) o;
			return Objects.equals(this.time, ueMobilityId.time) &&
					Objects.equals(this.supi, ueMobilityId.supi);
		}
	}
}
