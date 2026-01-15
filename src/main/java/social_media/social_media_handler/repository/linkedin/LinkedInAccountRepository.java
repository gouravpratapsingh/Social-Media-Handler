package social_media.social_media_handler.repository.linkedin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.entity.linkedin.LinkedInAccount;
import java.util.Optional;
@Repository
public interface LinkedInAccountRepository extends MongoRepository<LinkedInAccount, String> {
Optional<LinkedInAccount> findByUser(User user);
}