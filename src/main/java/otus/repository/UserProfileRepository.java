package otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import otus.model.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
}
