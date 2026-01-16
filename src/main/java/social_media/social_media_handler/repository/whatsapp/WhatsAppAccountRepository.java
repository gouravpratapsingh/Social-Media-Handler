package social_media.social_media_handler.repository.whatsapp;

import org.springframework.data.mongodb.repository.MongoRepository;
import social_media.social_media_handler.entity.whatsapp.WhatsAppAccount;
import social_media.social_media_handler.entity.User;
import java.util.Optional;

public interface WhatsAppAccountRepository extends MongoRepository<WhatsAppAccount, String> {
    Optional<WhatsAppAccount> findByUser(User user);
}