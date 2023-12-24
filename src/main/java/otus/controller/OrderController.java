package otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otus.model.dto.OrderRequest;
import otus.service.OrderService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService service;

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestHeader Map<String, String> headers, @RequestBody OrderRequest order) {
        if (!headers.containsKey("x-userid")) {
            return new ResponseEntity<>("AUTHORIZATION IS REQUIRED", HttpStatus.UNAUTHORIZED);
        }
        int userId = Integer.parseInt(headers.get("x-userid"));
        return new ResponseEntity<>(service.createOrder(userId, UUID.fromString(headers.get("x-orderid")), order), HttpStatus.OK);
    }
}
