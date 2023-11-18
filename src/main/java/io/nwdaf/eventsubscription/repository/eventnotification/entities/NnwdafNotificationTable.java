package io.nwdaf.eventsubscription.repository.eventnotification.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "nnwdaf_notification")
@Getter
@Setter
@NoArgsConstructor
public class NnwdafNotificationTable implements Serializable {

    @EmbeddedId
    private NwdafNotificationId Id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "notif", columnDefinition = "jsonb")
    @JsonProperty("notif")
    private Map<String, Object> notif;

    public OffsetDateTime getTime() {
        return Id.time;
    }

    public NnwdafNotificationTable(Map<String, Object> body, OffsetDateTime timeStamp) {
        this.setTime(timeStamp);
        this.setId(UUID.randomUUID());
        this.notif = body;
    }

    public void setTime(OffsetDateTime time) {
        if (this.Id == null) {
            this.Id = new NwdafNotificationId(time, null);
        } else {
            this.Id.time = time;
        }
    }

    public UUID getId() {
        return Id.id;
    }

    public void setId(UUID id) {
        this.Id.id = id;
    }

    @Embeddable
    @Getter
    @Setter
    static class NwdafNotificationId implements Serializable {

        @JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
        @Column(name = "time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
        private OffsetDateTime time;

        @JdbcTypeCode(SqlTypes.UUID)
        @Column(name = "id", columnDefinition = "UUID")
        private UUID id;

        public NwdafNotificationId() {

        }

        public NwdafNotificationId(OffsetDateTime time, UUID id) {
            this.setTime(time);
            this.setId(id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(time, id);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            NwdafNotificationId nwdafNotificationId = (NwdafNotificationId) o;
            return Objects.equals(this.time, nwdafNotificationId.time) &&
                    Objects.equals(this.id, nwdafNotificationId.id);
        }
    }
}
