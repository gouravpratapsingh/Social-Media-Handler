package social_media.social_media_handler.repository.linkedin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import social_media.social_media_handler.entity.linkedin.LinkedInScheduledPost;
import social_media.social_media_handler.entity.youtube.PostStatus;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface LinkedInScheduledPostRepository extends JpaRepository<LinkedInScheduledPost, Long> {

    List<LinkedInScheduledPost> findByStatusAndScheduledAtLessThanEqual(PostStatus status, LocalDateTime time );
}

