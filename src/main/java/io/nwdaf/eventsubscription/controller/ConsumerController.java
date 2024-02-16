package io.nwdaf.eventsubscription.controller;

import io.nwdaf.eventsubscription.customModel.WakeUpMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

import static io.nwdaf.eventsubscription.notify.NotificationUtil.waitForDataProducer;

@Controller
@CrossOrigin
public class ConsumerController {

    @PostMapping("/consumer/waitForDataProducer")
    public ResponseEntity<Long[]> waitForDataProducerRequest(@RequestBody String wakeUpMessage) {
        WakeUpMessage message;
        if (wakeUpMessage != null) {
            message = WakeUpMessage.fromString(wakeUpMessage);
        } else {
            return ResponseEntity.badRequest().build();
        }
        try {
            Long[] result = waitForDataProducer(message);
            return ResponseEntity.ok().body(result);
        } catch (InterruptedException | IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
