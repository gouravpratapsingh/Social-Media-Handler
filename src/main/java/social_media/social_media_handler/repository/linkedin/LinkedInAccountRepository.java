package social_media.social_media_handler.repository.linkedin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.entity.linkedin.LinkedInAccount;

import java.util.Optional;

@Repository
public interface LinkedInAccountRepository extends JpaRepository<LinkedInAccount, Long> {

    Optional<LinkedInAccount> findByUser(User user);
}

