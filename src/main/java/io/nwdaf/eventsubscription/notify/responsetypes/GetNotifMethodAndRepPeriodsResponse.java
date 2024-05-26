package io.nwdaf.eventsubscription.notify.responsetypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class GetNotifMethodAndRepPeriodsResponse {
    @Builder.Default
    private Map<Integer,NotificationMethodEnum> eventIndexToNotifMethodMap = new HashMap<>();
    @Builder.Default
    private Map<Integer,Integer> eventIndexToRepPeriodMap = new HashMap<>();
    @Builder.Default
    private Integer noValidEvents = 0;
    @Builder.Default
    private List<Integer> invalidEvents = new ArrayList<>();
}
