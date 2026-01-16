package social_media.social_media_handler.repository.twitter;

import org.springframework.data.mongodb.repository.MongoRepository;
import social_media.social_media_handler.entity.twitter.TwitterAccount;
import social_media.social_media_handler.entity.User;
import java.util.Optional;

public interface TwitterAccountRepository extends MongoRepository<TwitterAccount, String>{
    Optional<TwitterAccount> findByUser(User user);
}   
