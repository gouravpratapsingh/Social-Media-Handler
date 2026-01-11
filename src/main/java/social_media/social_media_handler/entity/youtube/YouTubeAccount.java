package social_media.social_media_handler.entity.youtube;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import social_media.social_media_handler.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "youtube_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouTubeAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String channelId;

    @Column(nullable = false)
    private String channelName;

    @Column(length = 1200, nullable = false)
    private String accessToken;

    @Column(length = 1200, nullable = false)
    private String refreshToken;

    private LocalDateTime tokenExpiry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
