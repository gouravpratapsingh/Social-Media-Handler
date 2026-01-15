package social_media.social_media_handler.repository.linkedin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import social_media.social_media_handler.entity.linkedin.LinkedInScheduledPost;
import social_media.social_media_handler.entity.PostStatus;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface LinkedInScheduledPostRepository extends MongoRepository<LinkedInScheduledPost, String> {
List<LinkedInScheduledPost> findByStatusAndScheduledAtLessThanEqual(PostStatus status, LocalDateTime time );
}