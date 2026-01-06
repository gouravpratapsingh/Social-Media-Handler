package social_media.social_media_handler.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import social_media.social_media_handler.entity.ScheduledPost;

@Repository
public interface ScheduledPostRepository extends JpaRepository<ScheduledPost, Long> {
    List<ScheduledPost> findByStatusAndScheduledTimeBefore(String status, LocalDateTime time);
}