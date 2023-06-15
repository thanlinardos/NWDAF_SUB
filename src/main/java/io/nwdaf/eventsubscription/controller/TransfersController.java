package io.nwdaf.eventsubscription.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import io.nwdaf.eventsubscription.api.TransfersApi;
import io.nwdaf.eventsubscription.model.AnalyticsSubscriptionsTransfer;

public class TransfersController implements TransfersApi{

	@Override
	public ResponseEntity<Void> createNWDAFEventSubscriptionTransfer(@Valid AnalyticsSubscriptionsTransfer body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Void> deleteNWDAFEventSubscriptionTransfer(String transferId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Void> updateNWDAFEventSubscriptionTransfer(String transferId,
			@Valid AnalyticsSubscriptionsTransfer body) {
		// TODO Auto-generated method stub
		return null;
	}

}
