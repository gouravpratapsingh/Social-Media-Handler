package social_media.social_media_handler.entity.twitter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;
@Document(collection = "twitter_posts")
public class TwitterPost {
@Id
private String id;
private String tweetId;
private String content;
private String status; // PENDING, POSTED, FAILED
private LocalDateTime scheduledTime;
private LocalDateTime postedAt;
// ✅ GETTERS & SETTERS
public String getId() {
return id;
}
public void setId(String id) {
this.id = id;
}
public String getTweetId() {
return tweetId;
}
public void setTweetId(String tweetId) {
this.tweetId = tweetId;
}
public String getContent() {
return content;
}
public void setContent(String content) {
this.content = content;
}
public String getStatus() {
return status;
}
public void setStatus(String status) {
this.status = status;
}
public LocalDateTime getScheduledTime() {
return scheduledTime;
}
public void setScheduledTime(LocalDateTime scheduledTime) {
this.scheduledTime = scheduledTime;
}
public LocalDateTime getPostedAt() {
return postedAt;
}
public void setPostedAt(LocalDateTime postedAt) {
this.postedAt = postedAt;
}
@jakarta.persistence.PrePersist
public void prePersist() {
if (id == null) {
id = UUID.randomUUID().toString();
}
}
}