package social_media.social_media_handler.entity.youtube;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platform;

    private String title;

    @Column(length = 5000)
    private String description;

    private String mediaPath;

    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private String failureReason;

    private LocalDateTime createdAt;
    private LocalDateTime postedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = PostStatus.PENDING;
    }
}
