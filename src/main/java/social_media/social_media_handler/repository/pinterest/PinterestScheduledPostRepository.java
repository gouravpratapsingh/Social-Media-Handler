package social_media.social_media_handler.repository.pinterest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import social_media.social_media_handler.entity.pinterest.PinterestScheduledPost;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PinterestScheduledPostRepository extends MongoRepository<PinterestScheduledPost, String> {
// Fix: Ye method waisa hi hona chahiye jaisa scheduler mein hai
List<PinterestScheduledPost> findByStatusAndScheduledTimeBefore(String status, LocalDateTime time);
}