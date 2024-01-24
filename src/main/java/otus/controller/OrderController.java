package otus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import otus.model.dto.OrderRequest;
import otus.model.entity.Order;
import otus.repository.OrderRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository repository;
    private final ObjectMapper objectMapper;

    @GetMapping("/status")
    public ResponseEntity<String> checkOrderStatus(@RequestParam UUID orderUUID) {
        Order order = repository.findById(orderUUID).orElseThrow();
        return new ResponseEntity<>(order.getStatus(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestHeader Map<String, String> headers, @RequestBody OrderRequest request) throws JsonProcessingException {
        if (!headers.containsKey("x-userid")) {
            return new ResponseEntity<>("AUTHORIZATION IS REQUIRED", HttpStatus.UNAUTHORIZED);
        }
        UUID orderUUID = UUID.fromString(headers.get("x-orderid"));
        Optional<Order> optionalExist = repository.findById(orderUUID);
        if (optionalExist.isPresent()) {
            return new ResponseEntity<>("Already exists", HttpStatus.CONFLICT);
        } else {
            Order newOrder = Order.builder()
                    .orderUUID(orderUUID)
                    .productUUID(request.getProductUUID())
                    .cost(request.getPrice() * request.getQuantity())
                    .quantity(request.getQuantity())
                    .userUUID(UUID.fromString(headers.get("x-userid")))
                    .userEmail(headers.get("x-email"))
                    .deliveryDate(request.getDeliveryDate())
                    .success(0)
                    .status("В обработке.")
                    .build();
            repository.save(newOrder);
            log.info("New request has been created");
            kafkaTemplate.send("newOrder", objectMapper.writeValueAsString(newOrder));
            log.info("Отправлено сообщение в Кафку о создании нового заказа.");
            return new ResponseEntity<>(newOrder.toString(), HttpStatus.OK);
        }
    }

    @KafkaListener(topics = "order200")
    public void listen200(String message) throws JsonProcessingException {
        log.info("Получено сообщение из топика 'order200' {}.", message);
        UUID orderUUID = UUID.fromString(message);
        Order order = repository.findById(orderUUID).orElseThrow();
        order.setSuccess(order.getSuccess() + 1);
        if (order.getSuccess() == 3) {
            order.setStatus("Заказ одобрен.");
            repository.save(order);
            kafkaTemplate.send("executeOrder", objectMapper.writeValueAsString(order));
        } else {
            repository.save(order);
        }
        kafkaTemplate.send("otus", message);
    }

    @KafkaListener(topics = "order500")
    public void listen500(String message) throws JsonProcessingException {
        log.info("Получено сообщение из топика 'order500' {}.", message);
        UUID orderUUID = UUID.fromString(message);
        Order order = repository.findById(orderUUID).orElseThrow();
        order.setStatus("Невозможно выполнить заказ.");
        repository.save(order);
        kafkaTemplate.send("cancelOrder", objectMapper.writeValueAsString(order));
    }
}
