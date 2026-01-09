package social_media.social_media_handler.entity.pinterest;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "pinterest_scheduled_posts")
public class PinterestScheduledPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String boardId;
    private String imageUrl;
    private String destinationLink;
    private LocalDateTime scheduledTime;
    private String status; // PENDING, PUBLISHED, FAILED
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}