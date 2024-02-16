package io.nwdaf.eventsubscription.controller;

import javax.validation.Valid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;

import io.nwdaf.eventsubscription.api.TransfersApi;
import io.nwdaf.eventsubscription.model.AnalyticsSubscriptionsTransfer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(name = "nnwdaf-eventsubscription.notifier", havingValue = "true")
@RestController
@CrossOrigin
public class TransfersController implements TransfersApi {

    @Override
    public ResponseEntity<Void> createNWDAFEventSubscriptionTransfer(@Valid AnalyticsSubscriptionsTransfer body) {
        throw new UnsupportedOperationException("/nnwdaf-eventsubscription/v1/transfers: Not supported yet.");
    }

    @Override
    public ResponseEntity<Void> updateNWDAFEventSubscriptionTransfer(String transferId,
                                                                     @Valid AnalyticsSubscriptionsTransfer body) {
        throw new UnsupportedOperationException("/nnwdaf-eventsubscription/v1/transfers/{transferId}: (PUT) Not supported yet.");
    }

    @Override
    public ResponseEntity<Void> deleteNWDAFEventSubscriptionTransfer(String transferId) {
        throw new UnsupportedOperationException("/nnwdaf-eventsubscription/v1/transfers/{transferId}: (DELETE) Not supported yet.");
    }

}
