package social_media.social_media_handler.repository.youtube;

import org.springframework.data.jpa.repository.JpaRepository;
import social_media.social_media_handler.entity.PostStatus;
import social_media.social_media_handler.entity.youtube.YouTubeScheduledPost;

import java.time.LocalDateTime;
import java.util.List;

public interface YouTubeScheduledPostRepository extends JpaRepository<YouTubeScheduledPost, Long> {
    List<YouTubeScheduledPost> findByPlatformAndStatusAndScheduledAtLessThanEqual(String platform, PostStatus status, LocalDateTime time );
}