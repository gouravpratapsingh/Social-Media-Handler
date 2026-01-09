package social_media.social_media_handler.repository.pinterest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import social_media.social_media_handler.entity.pinterest.PinterestScheduledPost;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PinterestScheduledPostRepository extends JpaRepository<PinterestScheduledPost, Long> {

    // Fix: Ye method waisa hi hona chahiye jaisa scheduler mein hai
    List<PinterestScheduledPost> findByStatusAndScheduledTimeBefore(String status, LocalDateTime time);
}