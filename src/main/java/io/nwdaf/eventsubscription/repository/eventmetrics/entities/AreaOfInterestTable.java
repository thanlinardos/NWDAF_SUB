package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name="area_of_interest")
@Getter
@Setter
@NoArgsConstructor
public class AreaOfInterestTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "uuid", columnDefinition = "UUID")
    private UUID uuid;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="data",columnDefinition = "jsonb")
    @JsonProperty("data")
    private Map<String, Object> data;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name= "intGroupId")
    @JsonProperty("intGroupId")
    private String intGroupId;

    public AreaOfInterestTable(NetworkAreaInfo data, String intGroupId) {
        this.data = data.toMap();
        this.uuid = data.getId();
        this.intGroupId = intGroupId;
    }
}
