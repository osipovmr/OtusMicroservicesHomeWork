package otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import otus.model.dto.OrderRequest;
import otus.model.entity.Order;
import otus.repository.OrderRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository repository;
    private final ModelMapper mapper;

    public String createOrder(int userId, OrderRequest request) {
        Optional<Order> optionalExist = repository.findById(request.getId());
        if (optionalExist.isPresent()) {
            return "Already exists";
        } else {
            Order newOrder = mapper.map(request, Order.class);
            newOrder.setUserId(userId);
            repository.save(newOrder);
            return "New order has been created";
        }
    }
}
