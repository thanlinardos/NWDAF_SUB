package io.nwdaf.eventsubscription.notify.responsetypes;

import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NwdafFailureCode.NwdafFailureCodeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class AggregateChecksForAOIResponse {
    @Builder.Default
    private NetworkAreaInfo matchingArea = null;
    @Builder.Default
    private Boolean insideServiceArea = false;
    @Builder.Default
    private Boolean failed_notif = false;
    @Builder.Default
    private NwdafFailureCodeEnum failCode = null;
}
