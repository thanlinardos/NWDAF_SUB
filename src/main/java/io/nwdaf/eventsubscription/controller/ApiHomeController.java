package io.nwdaf.eventsubscription.controller;

import io.nwdaf.eventsubscription.NwdafSubApplication;
import io.nwdaf.eventsubscription.kafka.KafkaConsumer;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.notify.NotifyListener;
import io.nwdaf.eventsubscription.notify.NotifyPublisher;
import io.nwdaf.eventsubscription.utilities.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ApiHomeController {

    public static final ConcurrentHashMap<NwdafEvent.NwdafEventEnum, String> eventConsumerRates = new ConcurrentHashMap<>() {{
        Constants.supportedEvents.forEach(e -> put(e, "0.0"));
    }};
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private final NotifyPublisher notifyPublisher;
    private static double prev_no_kb = NotifyListener.getNo_sent_kilobytes();
    private static long seconds = System.currentTimeMillis() / 1000L;

    public ApiHomeController(NotifyPublisher notifyPublisher) {
        this.notifyPublisher = notifyPublisher;
    }

    @GetMapping(value = "/")
    public RedirectView redirectWithUsingRedirectView(RedirectAttributes attributes) {
        return new RedirectView("swagger-ui/index.html");
    }

    @GetMapping(value = "/admin")
    public String getAdminView(ModelMap model) {
        model.addAttribute("NWDAF_INSTANCE_ID", NwdafSubApplication.NWDAF_INSTANCE_ID);
        model.addAttribute("ServingAreaOfInterest", NwdafSubApplication.ServingAreaOfInterest);
        return "admin";
    }

    @GetMapping(value = "/admin/notifyListeners")
    public String getNotifyListeners(ModelMap model) {

        model.addAttribute("noNotifyListeners", NotifyListener.getNo_notifEventListeners());
        model.addAttribute("no_served_subs", NotifyListener.getNo_served_subs());
        model.addAttribute("subIndexes", NotifyListener.getSubIndexes());
        model.addAttribute("client_delay", decimalFormat.format(NotifyListener.getClient_delay() / 1000.0));
        model.addAttribute("lastNotifTimes", NotifyListener.getLastNotifTimes());
        model.addAttribute("mapEventToNotification", NotifyListener.getMapEventToNotification());
        model.addAttribute("oldNotifTimes", NotifyListener.getOldNotifTimes());
        model.addAttribute("tsdb_req_delay", decimalFormat.format(NotifyListener.getTsdb_req_delay() / 1000.0));
        model.addAttribute("notif_save_delay", decimalFormat.format(NotifyListener.getNotif_save_delay() / 1000.0));
        model.addAttribute("sub_findall_delay", decimalFormat.format(NotifyListener.getSub_delay()));
        model.addAttribute("total_cycle_delay", decimalFormat.format(NotifyListener.getTotal()));
        model.addAttribute("no_sent_notifs", NotifyListener.getNo_sent_notifs());
        model.addAttribute("no_sent_kilobytes", NotifyListener.getNo_sent_kilobytes());
        model.addAttribute("total_sent_kilobytes", NotifyListener.getTotal_sent_kilobytes());
        model.addAttribute("total_sent_notifs", NotifyListener.getTotal_sent_notifs());
        model.addAttribute("counter", NotifyListener.getCounter()!=0?NotifyListener.getCounter():1);
        model.addAttribute("no_found_notifs", NotifyListener.getNo_found_notifs());
        model.addAttribute("avg_io_delay", NotifyListener.getAvg_io_delay());
        model.addAttribute("avg_program_delay", NotifyListener.getAvg_program_delay());
        model.addAttribute("avg_throughput",(NotifyListener.getNo_sent_kilobytes() - prev_no_kb) / (System.currentTimeMillis() / 1000L - seconds));
        seconds = System.currentTimeMillis() / 1000L;
        prev_no_kb = NotifyListener.getNo_sent_kilobytes();

        return "notifyListeners";
    }

    @GetMapping(value = "/admin/kafkaConsumers")
    public String getKafkaConsumers(ModelMap model) {

        model.addAttribute("eventConsumerStartedReceiving", KafkaConsumer.eventConsumerStartedReceiving);
        model.addAttribute("eventConsumerCounters", KafkaConsumer.eventConsumerCounters);
        long interval = KafkaConsumer.startTime != null ? Duration.between(KafkaConsumer.startTime, OffsetDateTime.now()).toSeconds() : 1L;
        Constants.supportedEvents.forEach(e -> eventConsumerRates.compute(e, (k, v) -> decimalFormat.format((double) KafkaConsumer.eventConsumerCounters.get(e) / (double) interval)));
        model.addAttribute("eventConsumerRates", eventConsumerRates);
        model.addAttribute("eventConsumerIsSyncing", KafkaConsumer.eventConsumerIsSyncing);
        model.addAttribute("latestWakeUpMessageEventMap", KafkaConsumer.latestWakeUpMessageEventMap);
        model.addAttribute("latestDiscoverMessageEventMap", KafkaConsumer.latestDiscoverMessageEventMap);
        model.addAttribute("isListening", KafkaConsumer.isListening.get());

        return "kafkaConsumers";
    }

    @GetMapping(value = "/admin/kafkaCollectors")
    public String getKafkaCollectors(ModelMap model) {

        model.addAttribute("eventCollectorIdSet", KafkaConsumer.eventCollectorIdSet);

        return "kafkaCollectors";
    }

    @GetMapping("/admin/toggleNotifyListener")
    @ResponseBody
    public ResponseEntity<Integer> toggleNotifyListener() {

        if(NotifyListener.getNo_notifEventListeners().get()>0) {
            NotifyListener.stop();
        } else {
            notifyPublisher.publishNotification("toggled from ui", 0L);
        }

        return ResponseEntity.ok(NotifyListener.getNo_notifEventListeners().get());
    }

    @GetMapping("/admin/getNoNotifyListeners")
    public Integer getNoNotifyListeners() {
        return NotifyListener.getNo_notifEventListeners().get();
    }

    @GetMapping("/admin/toggleKafkaConsumer")
    @ResponseBody
    public ResponseEntity<Boolean> toggleKafkaConsumer() {

        if(KafkaConsumer.isListening.get()) {
            KafkaConsumer.stopListening();
        } else {
            KafkaConsumer.startListening();
        }

        return ResponseEntity.ok(KafkaConsumer.isListening.get());
    }

    @GetMapping(value = "/admin/fragments/areaOfInterestTable")
    public String getAreaOfInterestTable(ModelMap map) {
        map.addAttribute("areaOfInterest", NwdafSubApplication.ServingAreaOfInterest);
        return "fragments/areaOfInterestTable";
    }
}
