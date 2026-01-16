package social_media.social_media_handler.repository.youtube;
import org.springframework.data.mongodb.repository.MongoRepository;
import social_media.social_media_handler.entity.youtube.YouTubeAccount;
import java.util.Optional;
public interface YouTubeAccountRepository extends MongoRepository<YouTubeAccount, String> {
Optional<YouTubeAccount> findByUserId(String userId);
}