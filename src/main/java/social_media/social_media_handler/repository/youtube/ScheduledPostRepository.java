package social_media.social_media_handler.repository.youtube;

import org.springframework.data.jpa.repository.JpaRepository;
import social_media.social_media_handler.entity.youtube.PostStatus;
import social_media.social_media_handler.entity.youtube.ScheduledPost;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledPostRepository
        extends JpaRepository<ScheduledPost, Long> {

    List<ScheduledPost> findByPlatformAndStatusAndScheduledAtLessThanEqual(
            String platform,
            PostStatus status,
            LocalDateTime time
    );
}
