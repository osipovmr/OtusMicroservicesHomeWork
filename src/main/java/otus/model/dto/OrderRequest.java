package otus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Тело запроса на создание заказа.
 */
@Data
public class OrderRequest {

    @JsonProperty("productUUID")
    private UUID productUUID;
    private String productName;
    private int quantity;
    private double price;
    private LocalDate deliveryDate;
}
