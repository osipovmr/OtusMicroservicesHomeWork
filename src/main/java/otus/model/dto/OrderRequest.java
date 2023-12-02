package otus.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequest {

    private UUID id;
    private String name;
    private String value;
    private String city;
}
