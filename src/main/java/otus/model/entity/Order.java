package otus.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_uuid")
    private UUID orderUUID;
    @Column(name = "product_uuid")
    private UUID productUUID;
    private int quantity;
    private double cost;
    @Column(name = "user_uuid")
    private UUID userUUID;
    private String userEmail;
    private String status;
    private LocalDate deliveryDate;

    @Override
    public String toString() {
        return "Order{" +
                "orderUUID=" + orderUUID +
                ", productUUID=" + productUUID +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", userUUID=" + userUUID +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
