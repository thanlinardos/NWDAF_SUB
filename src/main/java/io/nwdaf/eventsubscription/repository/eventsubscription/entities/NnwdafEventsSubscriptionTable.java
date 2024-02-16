package io.nwdaf.eventsubscription.repository.eventsubscription.entities;

import java.io.Serializable;
import java.util.Map;

import io.nwdaf.eventsubscription.repository.TableEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "nnwdaf_events_subscription")
public class NnwdafEventsSubscriptionTable implements Serializable, TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sub", columnDefinition = "jsonb")
    @JsonProperty("sub")
    private Map<String, Object> data;

}
