package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.nwdaf.eventsubscription.model.UeCommunication;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ue_communication_metrics")
@Getter
@Setter
@NoArgsConstructor
public class UeCommunicationTable{

    @EmbeddedId
    private UeCommunicationId Id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="data",columnDefinition = "jsonb")
    @JsonProperty("data")
    private Map<String, Object> data;

    @Column(name="intGroupId")
    private String intGroupId;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name="areaOfInterestId", columnDefinition = "UUID")
    private UUID areaOfInterestId;

    public UeCommunicationTable(UeCommunication data) {
        this.data = data.toMap();
        this.setTime(data.getTs());
        this.setSupi(data.getSupi());
        this.intGroupId = data.getIntGroupId();
         this.areaOfInterestId = data.getAreaOfInterestId();
    }

    public OffsetDateTime getTime() {
        return Id.time;
    }

    public void setTime(OffsetDateTime time) {
        if (this.Id == null) {
            this.Id = new UeCommunicationId(time, null);
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
    static class UeCommunicationId implements Serializable {

        @JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
        @Column(name = "time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
        private OffsetDateTime time;

        @Column(name="supi")
        private String supi;

        public UeCommunicationId() {

        }

        public UeCommunicationId(OffsetDateTime time, String supi) {
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
            UeCommunicationId ueCommunicationId = (UeCommunicationId) o;
            return Objects.equals(this.time, ueCommunicationId.time) &&
                    Objects.equals(this.supi, ueCommunicationId.supi);
        }
    }
}
