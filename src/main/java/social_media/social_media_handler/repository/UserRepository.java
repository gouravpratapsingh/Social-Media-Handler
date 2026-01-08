package social_media.social_media_handler.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import social_media.social_media_handler.entity.User;

import java.util.Optional;

@SuppressWarnings("NullableProblems")
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    boolean existsByEmail(String email);
}

