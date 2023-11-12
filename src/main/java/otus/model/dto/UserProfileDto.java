package otus.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {
    private Integer id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUri;
    private int age;
}
