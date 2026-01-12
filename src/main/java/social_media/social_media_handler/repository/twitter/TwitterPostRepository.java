package social_media.social_media_handler.repository.twitter;

import social_media.social_media_handler.entity.twitter.TwitterPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TwitterPostRepository
        extends JpaRepository<TwitterPost, Long> {

    List<TwitterPost> findByStatusAndScheduledTimeBefore(
            String status,
            LocalDateTime time
    );
}
