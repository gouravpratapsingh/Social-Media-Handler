package social_media.social_media_handler.repository.whatsapp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import social_media.social_media_handler.entity.whatsapp.WhatsAppScheduledPost;

@Repository
public interface WhatsAppScheduledPostRepository extends JpaRepository<WhatsAppScheduledPost, Long> {
    List<WhatsAppScheduledPost> findByStatusAndScheduledTimeBefore(String status, LocalDateTime time);
}