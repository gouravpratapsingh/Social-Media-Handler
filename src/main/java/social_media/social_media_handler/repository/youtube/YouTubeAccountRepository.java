package social_media.social_media_handler.repository.youtube;

import org.springframework.data.jpa.repository.JpaRepository;
import social_media.social_media_handler.entity.youtube.YouTubeAccount;

import java.util.Optional;

public interface YouTubeAccountRepository
        extends JpaRepository<YouTubeAccount, Long> {

    Optional<YouTubeAccount> findByUser_Id(String userId);
}
