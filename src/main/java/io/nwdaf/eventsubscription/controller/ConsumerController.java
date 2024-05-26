package io.nwdaf.eventsubscription.controller;

import io.nwdaf.eventsubscription.customModel.WakeUpMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static io.nwdaf.eventsubscription.notify.NotificationUtil.waitForDataProducer;

@ConditionalOnProperty(name = "nnwdaf-eventsubscription.consume", havingValue = "true")
@Controller
@CrossOrigin
public class ConsumerController {

    @PostMapping("/consumer/waitForDataProducer")
    public ResponseEntity<Long[]> waitForDataProducerRequest(@RequestBody String wakeUpMessage) {
        if (wakeUpMessage == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Long[] result = waitForDataProducer(WakeUpMessage.fromString(wakeUpMessage));
            return ResponseEntity.ok().body(result);
        } catch (InterruptedException | IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
