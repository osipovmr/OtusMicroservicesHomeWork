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
import otus.model.dto.ProcessOrderDto;
import otus.model.entity.Order;
import otus.repository.OrderRepository;

import java.util.HashMap;
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

    private final HashMap<UUID, Integer> map = new HashMap<>();

    private static String getErrorMessage(ProcessOrderDto dto) {
        return switch (dto.getServiceName()) {
            case "delivery" -> "Невозможно выполнить заказ, так как отсутствуют свободные курьеры для доставки.";
            case "storage" ->
                    "Невозможно выполнить заказ, так как отсутствуют необходимое количетсво товара на складе.";
            case "payment" -> "Невозможно выполнить заказ, так как недостаточно средств для оплаты.";
            default -> null;
        };
    }

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
                    .status("В обработке.")
                    .build();
            repository.save(newOrder);
            ProcessOrderDto dto = ProcessOrderDto.builder()
                    .orderUUID(newOrder.getOrderUUID())
                    .productUUID(newOrder.getProductUUID())
                    .cost(newOrder.getCost())
                    .quantity(newOrder.getQuantity())
                    .userEmail(newOrder.getUserEmail())
                    .userUUID(newOrder.getUserUUID())
                    .deliveryDate(newOrder.getDeliveryDate())
                    .isNew(true)
                    .build();
            log.info("New request has been created");
            kafkaTemplate.send("newOrder", objectMapper.writeValueAsString(dto));
            log.info("Отправлено сообщение в Кафку о создании нового заказа {}.", dto.getOrderUUID());
            map.put(orderUUID, 0);
            return new ResponseEntity<>(newOrder.toString(), HttpStatus.OK);
        }
    }

    @KafkaListener(topics = "processOrder")
    public void listen(String message) throws JsonProcessingException {
        log.info("Получено сообщение из топика 'processOrder' {}.", message);
        ProcessOrderDto dto = objectMapper.readValue(message, ProcessOrderDto.class);
        Order order = repository.findById(dto.getOrderUUID()).orElseThrow();
        if (!dto.isAllowReservation()) {
            String errorMessage = getErrorMessage(dto);
            order.setStatus(errorMessage);
            repository.save(order);
            dto.setNew(false);
            dto.setReservedByAllServices(false);
            dto.setMessage(errorMessage);
            map.remove(dto.getOrderUUID());
            kafkaTemplate.send("newOrder", objectMapper.writeValueAsString(dto));
            log.info("Отправлена команда на отмену резервирования для заказа {}.", message);
        } else {
            int count = map.getOrDefault(dto.getOrderUUID(), 99);
            if (count != 99) {
                count = count + 1;
                if (count == 3) {
                    order.setStatus("Заказ одобрен.");
                    repository.save(order);
                    dto.setNew(false);
                    dto.setReservedByAllServices(true);
                    dto.setMessage("Курьер в пути");
                    kafkaTemplate.send("newOrder", objectMapper.writeValueAsString(dto));
                    log.info("Отправлена команда на выполнение заказа {}.", dto.getOrderUUID());
                    map.remove(dto.getOrderUUID());
                } else {
                    map.put(dto.getOrderUUID(), count);
                }
            }
        }
    }
}
