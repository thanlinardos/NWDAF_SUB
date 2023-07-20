package io.nwdaf.eventsubscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;

public class Constants {
	public static Integer MIN_PERIOD_SECONDS = 1;
	public static Integer MAX_PERIOD_SECONDS = 600;
	public static List<NwdafEventEnum> supportedEvents = new ArrayList<>(Arrays.asList(NwdafEventEnum.NF_LOAD));
}
