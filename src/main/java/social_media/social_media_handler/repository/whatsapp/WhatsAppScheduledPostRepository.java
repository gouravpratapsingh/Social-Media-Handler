package social_media.social_media_handler.repository.whatsapp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import social_media.social_media_handler.entity.whatsapp.WhatsAppScheduledPost;
@Repository
public interface WhatsAppScheduledPostRepository extends MongoRepository<WhatsAppScheduledPost, String> {
List<WhatsAppScheduledPost> findByStatusAndScheduledTimeBefore(String status, LocalDateTime time);
}