package otus.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateDto {
    @JsonProperty("avatarUri")
    @JsonAlias("avatar_uri")
    private String avatarUri;
    private int age;
}
