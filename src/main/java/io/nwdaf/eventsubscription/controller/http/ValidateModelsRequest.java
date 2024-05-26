package io.nwdaf.eventsubscription.controller.http;

import io.nwdaf.eventsubscription.utilities.Regex;

import java.util.Optional;

import static io.nwdaf.eventsubscription.utilities.Constants.MAX_PERIOD_SECONDS;
import static io.nwdaf.eventsubscription.utilities.Constants.MIN_PERIOD_SECONDS;

public class ValidateModelsRequest {

    public static void validateUePathParameters(Optional<String> groupId, int pastOffset, int endPastOffset, int periodValue) {
        if (pastOffset < 0 || endPastOffset < 0 || periodValue < MIN_PERIOD_SECONDS || periodValue > MAX_PERIOD_SECONDS)
            throw new IllegalArgumentException(
                    "Invalid pastOffset = " + pastOffset
                            + " or endPastOffset = " + endPastOffset
                            + " or period = " + periodValue);
        if (groupId.isPresent() && !groupId.get().matches(Regex.group_id))
            throw new IllegalArgumentException("Invalid groupId = " + groupId);
    }
}
