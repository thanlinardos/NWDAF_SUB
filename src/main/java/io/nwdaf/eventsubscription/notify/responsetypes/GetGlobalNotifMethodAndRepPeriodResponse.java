package io.nwdaf.eventsubscription.notify.responsetypes;

import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class GetGlobalNotifMethodAndRepPeriodResponse {
    @Builder.Default
    private Integer repetionPeriod = null;
    @Builder.Default
    private NotificationMethodEnum notificationMethod = null;
    @Builder.Default
    private Boolean muted=false;
    @Builder.Default
    private Boolean immRep=false;
}
