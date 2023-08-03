package io.nwdaf.eventsubscription.repository.eventnotification.entities;

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
@Table(name="nnwdaf_notification")
public class NnwdafNotificationTable implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	@JsonProperty("id")
	private Long id;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name="notif",columnDefinition = "jsonb")
	@JsonProperty("notif")
	private Map<String, Object> notif;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Map<String, Object> getNotif() {
		return notif;
	}
	
	public void setNotif(Map<String, Object> notif) {
		this.notif = notif;
	}
}
