package otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import otus.model.entity.UserProfile;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}
