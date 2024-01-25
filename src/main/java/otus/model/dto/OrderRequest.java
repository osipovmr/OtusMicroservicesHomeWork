package otus.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Тело запроса на создание заказа.
 */
@Data
public class OrderRequest {

    private UUID productUUID;
    private int quantity;
    private int price;
    private LocalDate deliveryDate;
}
