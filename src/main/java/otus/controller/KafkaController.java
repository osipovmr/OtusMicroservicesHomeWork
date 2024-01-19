package otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/kafka")
    public ResponseEntity<Void> kafka(@RequestParam String message) {
        kafkaTemplate.send("otus", message);
        System.out.println("Send message to Kafka " + message);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
