package io.nwdaf.eventsubscription.controller.http;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.utilities.Constants;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ValidateSubscriptionRequest {
    public static void validateRequest(NnwdafEventsSubscription body, ServletRequestAttributes attributes, HttpServletRequestAdapter request) throws MissingContentLengthException, PayloadTooLargeException {
        if (attributes != null && attributes.getRequest().getContentLengthLong() == -1) {
            throw new MissingContentLengthException("Missing content length", null);
        }
        if (attributes != null && attributes.getRequest().getContentLengthLong() > Constants.max_bytes_per_subscription) {
            throw new PayloadTooLargeException("Payload too large", null, attributes.getRequest().getContentLengthLong());
        }

        if (body == null || !CheckUtil.safeCheckListNotEmpty(body.getEventSubscriptions())) {
            throw new HttpMessageNotReadableException("Invalid body for subscription", request);
        }

        if (body.getEventSubscriptions().size() > Constants.max_subs_per_process) {
            throw new HttpMessageNotReadableException("Too many subscriptions in one request", request);
        }
    }
}
