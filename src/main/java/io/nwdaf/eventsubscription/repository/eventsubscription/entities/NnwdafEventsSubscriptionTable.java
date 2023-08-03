package io.nwdaf.eventsubscription.repository.eventsubscription.entities;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="nnwdaf_events_subscription")
public class NnwdafEventsSubscriptionTable implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	@JsonProperty("id")
	private Long id;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name="sub",columnDefinition = "jsonb")
	@JsonProperty("sub")
	private Map<String, Object> sub;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Map<String, Object> getSub() {
		return sub;
	}
	
	public void setSub(Map<String, Object> sub) {
		this.sub = sub;
	}
}
