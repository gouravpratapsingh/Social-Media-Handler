package social_media.social_media_handler.repository.twitter;
import social_media.social_media_handler.entity.twitter.TwitterPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;
public interface TwitterPostRepository
extends MongoRepository<TwitterPost, String> {
List<TwitterPost> findByStatusAndScheduledTimeBefore(
String status,
LocalDateTime time
);
}
