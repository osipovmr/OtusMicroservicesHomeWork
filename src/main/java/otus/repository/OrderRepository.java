package otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import otus.model.entity.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
