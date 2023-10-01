package otus.model.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
