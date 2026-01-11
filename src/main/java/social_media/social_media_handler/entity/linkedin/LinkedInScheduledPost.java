package social_media.social_media_handler.entity.linkedin;

import jakarta.persistence.*;
import lombok.*;
import social_media.social_media_handler.entity.PostStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "linkedin_scheduled_posts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class LinkedInScheduledPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String mediaPath; // image/video file path

    private LocalDateTime scheduledAt;

    private LocalDateTime postedAt;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(length = 2000)
    private String failureReason;

    private String platformPostUrn;

    @ManyToOne
    @JoinColumn(name = "linkedin_account_id")
    private LinkedInAccount account;
}

