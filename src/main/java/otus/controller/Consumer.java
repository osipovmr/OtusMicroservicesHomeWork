package otus.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @KafkaListener(topics = "otus")
    public void listener(String message) {
        System.out.println("Kafka listener got message " + message);
    }
}
