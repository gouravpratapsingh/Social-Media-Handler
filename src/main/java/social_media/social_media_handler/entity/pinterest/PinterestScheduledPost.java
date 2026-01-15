package social_media.social_media_handler.entity.pinterest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
@Document(collection = "pinterest_scheduled_posts")
@Data
public class PinterestScheduledPost {
@Id
private String id;
private String title;
private String description;
private String boardId;
private String imageUrl;
private String destinationLink;
private LocalDateTime scheduledTime;
private String status; // PENDING, PUBLISHED, FAILED
private String errorMessage;
@jakarta.persistence.PrePersist
public void prePersist() {
if (id == null) {
id = UUID.randomUUID().toString();
}
}
}