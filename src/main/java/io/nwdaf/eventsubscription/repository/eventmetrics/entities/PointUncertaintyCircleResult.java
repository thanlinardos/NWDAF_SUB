package io.nwdaf.eventsubscription.repository.eventmetrics.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @ToString
public class PointUncertaintyCircleResult {
    private Double latitude;
    private Double longitude;
    private Double uncertainty;
}
