package otus.model.dto;

import lombok.Data;

@Data
public class OrderRequest {

    private String name;
    private String value;
    private String city;
}
